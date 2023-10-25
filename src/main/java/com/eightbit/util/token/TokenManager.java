package com.eightbit.util.token;

import com.eightbit.entity.user.TokenInfo;
import com.eightbit.persistence.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final UserRepository userRepository;

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
