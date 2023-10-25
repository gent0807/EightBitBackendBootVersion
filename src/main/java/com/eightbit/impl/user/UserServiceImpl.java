package com.eightbit.impl.user;

import com.eightbit.entity.user.LoginResult;
import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import com.eightbit.inter.user.UserService;
import com.eightbit.persistence.user.UserRepository;
import com.eightbit.util.token.JwtTokenProvider;
import com.eightbit.util.token.TokenManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:auth.properties")
//@MyUserServiceImpl
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenManager tokenManager;

    private final JwtTokenProvider jwtTokenProvider;



    public TokenInfo login(User userVO){
        LoginResult loginResult= userRepository.login(userVO);
        String loginState=loginResult.getState();
        User user=loginResult.getUser();
        TokenInfo tokenInfo;
        if(loginState.equals("no")||loginState.equals("emailok")){
            tokenInfo=new TokenInfo(loginState, null,null,null,null, null, 0);
            return tokenInfo;
        }
        else if(loginState.equals("allok")){
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getNickname(),user.getPassword());
            Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            tokenInfo = jwtTokenProvider.generateToken(authentication, loginState, user.getNickname(), user.getRole(),user.getPoint());
            userRepository.updateToken(tokenInfo);
            return tokenInfo;
        }
        return tokenInfo=null;
    }

    @Override
    public ResponseCookie createCookie(TokenInfo tokenInfo) {

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 24)
                .domain("localhost")
                .build();

        String refreshToken = tokenInfo.getRefreshToken();

        if (refreshToken != null) {
            refreshToken = encoder.encode(refreshToken);
            responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .domain("localhost")
                    .build();
            tokenInfo.setRefreshToken(null);
        }

        return responseCookie;
    }

    @Override
    public void resetToken(HttpServletRequest request, String token, String nickname, TokenInfo tokenInfo) {
        if(tokenManager.checkAccessToken(request, token, nickname)){
            tokenInfo.setNickName(nickname);
            userRepository.updateToken(tokenInfo);
        }
    }


    @Override
    public String checkToken(HttpServletRequest request, String token,String nickname, String key) {
        if(tokenManager.validateAccessToken(request, token, nickname, key).equals("expired") ){
            if(tokenManager.checkAccessToken(request, token, nickname)){
                if(tokenManager.validateRefreshToken(request, token, nickname, key).equals("valid")){
                    if(tokenManager.checkRefreshToken(request,token,nickname)){
                        return "possible to update token";
                    }
                    else {
                        return "refreshtoken not matched user";
                    }
                }
                else if(tokenManager.validateRefreshToken(request, token, nickname, key).equals("expired")){
                    return "refreshtoken expired";
                }
                else{
                    return "refreshtoken invalid";
                }
            }
            else{
                return "accesstoken not matched user";
            }
        }
        else if(tokenManager.validateAccessToken(request, token, nickname, key).equals("valid") ){
            return"accesstoken valid";
        }
        return "invalid";
    }



    @Override
    public TokenInfo updateToken(TokenInfo tokenInfo) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(tokenInfo.getNickName(),userRepository.getPassword(tokenInfo.getNickName()));
        Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        tokenInfo = jwtTokenProvider.generateToken(authentication, tokenInfo.getLoginState(), tokenInfo.getNickName(), tokenInfo.getRole(), tokenInfo.getPoint());
        return tokenInfo;
    }

    @Override
    public String getAccessToken(String writer) {
        return userRepository.getAccessToken(writer);
    }





}
