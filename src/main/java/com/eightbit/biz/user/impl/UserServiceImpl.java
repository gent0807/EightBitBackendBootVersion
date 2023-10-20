package com.eightbit.biz.user.impl;

import com.eightbit.biz.user.inter.UserService;
import com.eightbit.biz.user.persistence.UserMyBatisDAO;
import com.eightbit.biz.user.util.JwtTokenProvider;
import com.eightbit.biz.user.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:auth.properties")
//@MyUserServiceImpl
public class UserServiceImpl implements UserService {





    private final UserMyBatisDAO userMyBatisDAO;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    public UserVO getUserProfileImagePath(String nickname){
        return userMyBatisDAO.getUserProfileImagePath(nickname);
    }

    public String getUserRole(String nickname){
        return userMyBatisDAO.getUserRole(nickname);
    }

    public String getPassword(String nickname){
        return userMyBatisDAO.getPassword(nickname);
    }

    public String alreadyEmailRegisterCheck(String email){
        return userMyBatisDAO.alreadyEmailRegisterCheck(email);
    }
    public String alreadyNickRegisterCheck(String nickname){
        return userMyBatisDAO.alreadyNickRegisterCheck(nickname);
    }
    public String alreadyPasswordUsingCheck(UserVO userVO){
        return userMyBatisDAO.alreadyPasswordUsingCheck(userVO);
    }
    public String insertUser(UserVO userVO) throws IOException {
        userVO.setPassword(encoder.encode(userVO.getPassword()));
        if(userMyBatisDAO.insertUser(userVO)){

            return "OK";
        }
        return "NO";
    }


    public TokenInfo login(UserVO userVO){
        LoginResult loginResult= userMyBatisDAO.login(userVO);
        String loginState=loginResult.getState();
        UserVO user=loginResult.getUser();
        TokenInfo tokenInfo;
        if(loginState.equals("no")||loginState.equals("emailok")){
            tokenInfo=new TokenInfo(loginState, null,null,null,null, null,null, 0);
            return tokenInfo;
        }
        else if(loginState.equals("allok")){
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getNickname(),user.getPassword());
            Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            tokenInfo = jwtTokenProvider.generateToken(authentication, loginState, user.getNickname(), user.getRole(),user.getProfileImgPath(),user.getPoint());
            updateToken(tokenInfo);
            return tokenInfo;
        }
        return tokenInfo=null;
    }

    public String updateUserPw(UserVO userVO){
        userVO.setPassword(encoder.encode(userVO.getPassword()));
        return userMyBatisDAO.updateUserPw(userVO);
    }

    @Override
    public String getAccessToken(String writer) {
        return userMyBatisDAO.getAccessToken(writer);
    }

    public String getRefreshToken(String writer){return userMyBatisDAO.getRefreshToken(writer);}
    public PointVO updatePoint(PointVO pointVO){
        return userMyBatisDAO.updatePoint(pointVO);
    }


    public void updateToken(TokenInfo tokenInfo){
        userMyBatisDAO.updateToken(tokenInfo);
    }
    public void deleteUser(String param){
        userMyBatisDAO.deleteUser(param);
    }

    public String deletePhoneNum(String phoneNum){
        return userMyBatisDAO.deletePhoneNum(phoneNum);
    }

    public String checkRightAuthNum(TempVO tempVO){
        return userMyBatisDAO.checkRightAuthNum(tempVO);
    }

    public String checkRightPhoneAuthNum(PhoneVO phoneVO){
        return userMyBatisDAO.checkRightPhoneAuthNum(phoneVO);
    }


    @PostConstruct
    public void init(){
        System.out.println("UserServiceImpl 의존 주입 완료");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("UserServiceImpl 소멸");
    }
}
