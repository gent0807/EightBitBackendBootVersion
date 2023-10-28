package com.eightbit.impl.user;

import com.eightbit.entity.user.LoginResult;
import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import com.eightbit.inter.user.UserService;
import com.eightbit.persistence.user.UserRepository;
import com.eightbit.util.token.JwtTokenProvider;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:auth.properties")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
    public String insertUser(User user) throws IOException {
        if(userRepository.insertProfileImg(user)){
            return userRepository.insertUser(user);
        }
        return "NO";
    }

}
