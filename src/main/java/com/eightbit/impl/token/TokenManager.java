package com.eightbit.impl.token;

import com.eightbit.entity.user.TokenInfo;
import com.eightbit.persistence.user.UserRepository;
import com.eightbit.util.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final BCryptPasswordEncoder encoder;

    private final JwtTokenProvider jwtTokenProvider;


    public void resetToken(HttpServletRequest request, String token, String nickname, TokenInfo tokenInfo) {
        if(checkAccessToken(request, token, nickname)){
            tokenInfo.setNickName(nickname);
            userRepository.updateToken(tokenInfo);
        }
    }

    public String checkToken(HttpServletRequest request, String token,String nickname, String key) {
        if(validateAccessToken(request, token, nickname, key).equals("expired") ){
            if(checkAccessToken(request, token, nickname)){
                if(validateRefreshToken(request, token, nickname, key).equals("valid")){
                    if(checkRefreshToken(request,token,nickname)){
                        return "possible to update token";
                    }
                    else {
                        return "refreshtoken not matched user";
                    }
                }
                else if(validateRefreshToken(request, token, nickname, key).equals("expired")){
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
        else if(validateAccessToken(request, token, nickname, key).equals("valid") ){
            return"accesstoken valid";
        }
        return "invalid";
    }

    public TokenInfo updateToken(TokenInfo tokenInfo) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(tokenInfo.getNickName(),userRepository.getPassword(tokenInfo.getNickName()));
        Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        tokenInfo = jwtTokenProvider.generateToken(authentication, tokenInfo.getLoginState(), tokenInfo.getNickName(), tokenInfo.getRole(), tokenInfo.getPoint());
        return tokenInfo;
    }

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

    public boolean checkAccessToken(HttpServletRequest request, String token, String writer){
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            token=bearerToken.substring(7);
        }

        return token.equals(userRepository.getAccessToken(writer));
    }

    public boolean checkRefreshToken(HttpServletRequest request,  String token, String writer){
        Cookie[] cookies  = request.getCookies();

        for(Cookie cookie:cookies) {
            if(cookie.getName().equals("refreshToken")) {
                token=cookie.getValue();
            }
        }

        return token.equals(userRepository.getRefreshToken(writer));
    }

    public String validateAccessToken(HttpServletRequest request, String token, String nickname, String key){
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            token=bearerToken.substring(7);
        }

        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return "valid";
        }
        catch (ExpiredJwtException e){
            return "expired";
        }
    }
    public String validateRefreshToken(HttpServletRequest request, String token, String nickname, String key){
        Cookie[] cookies  = request.getCookies();

        for(Cookie cookie:cookies) {
            if(cookie.getName().equals("refreshToken")) {
                token=cookie.getValue();
            }
        }
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return "valid";
        }
        catch (ExpiredJwtException e){
            return "expired";
        }
    }


}
