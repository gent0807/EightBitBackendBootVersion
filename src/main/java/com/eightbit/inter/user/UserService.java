package com.eightbit.inter.user;

import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

        public String getAccessToken(String writer);
        public TokenInfo login(User user);
        public ResponseCookie createCookie(TokenInfo tokenInfo);
        public void resetToken(HttpServletRequest request,String token,String nickname, TokenInfo tokenInfo);
        public String checkToken(HttpServletRequest request,String token,String nickname, String key);

        public TokenInfo updateToken(TokenInfo tokenInfo);

}
