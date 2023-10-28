package com.eightbit.controller.token;

import com.eightbit.entity.user.TokenInfo;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/Users/token/**")
@RequiredArgsConstructor
public class TokenController {

    private final TokenManager tokenManager;
    @PatchMapping(value = "/reset/{nickname}")
    public void resetToken(HttpServletRequest request, String token,
                           @PathVariable String nickname, TokenInfo tokenInfo){

        tokenManager.resetToken(request, token, nickname, tokenInfo);
    }

    @PatchMapping(value="/{nickname}")
    public ResponseEntity<String> updateToken(HttpServletRequest request, @Value("${jwt.secret}") String key, @RequestBody TokenInfo tokenInfo, String token, @PathVariable String nickname){

        String result=tokenManager.checkToken(request, key, token, nickname);

        if(result.equals("possible to update token")){
            tokenInfo=tokenManager.updateToken(tokenInfo);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenManager.createCookie(tokenInfo).toString()).body(tokenInfo.getAccessToken());
        }
        else {
            return ResponseEntity.ok().body(result);
        }

    }
}
