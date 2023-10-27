package com.eightbit.controller.user;


import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import com.eightbit.inter.user.UserService;
import com.eightbit.persistence.user.UserRepository;
import com.eightbit.util.token.TokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Users/*")
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:upload.properties")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final TokenManager tokenManager;

    @PostMapping(value = "/check/email/already")
    public ResponseEntity<String> alreadyEmailRegisterCheck(@RequestBody User user){
        return ResponseEntity.ok().body(userRepository.alreadyEmailRegisterCheck(user.getEmail()));
    }

    @PostMapping(value = "/check/nick/already")
    public ResponseEntity<String> alreadyNickRegisterCheck(@RequestBody User user){
        return ResponseEntity.ok().body(userRepository.alreadyNickRegisterCheck(user.getNickname()));
    }

    @PostMapping(value = "/check/password/already")
    public ResponseEntity<String> alreadyPasswordUsingCheck(@RequestBody User user){
        return ResponseEntity.ok().body(userRepository.alreadyPasswordUsingCheck(user));
    }

    @ResponseBody
    @GetMapping(value = "/profileImg/{user}")
    public Resource getUserProfileImg(@PathVariable String user, @Value("${file.dir}") String filepath) throws MalformedURLException {
        if(user==null){
            return new UrlResource("file:"+filepath+"default.jpg");
        }
        String imagePath=userRepository.getUserProfileImagePath(user);
        return new UrlResource("file:"+filepath+user+"/profileImage/"+imagePath);
    }

    @GetMapping(value = "/role")
    public ResponseEntity<String> getUserRole(@RequestParam String nickname){
        return ResponseEntity.ok().body(userRepository.getUserRole(nickname));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<String> insertUser(@RequestBody User user) throws IOException {
        return ResponseEntity.ok().body(userService.insertUser(user));
    }

    @PostMapping(value = "/check/login")
    public ResponseEntity<TokenInfo> login(@RequestBody User user){

        /*

        TokenInfo tokenInfo=userService.login(userVO);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userService.createCookie(tokenInfo).toString()).body(tokenInfo);

        */

        return ResponseEntity.ok().body(userService.login(user));
    }


    @PatchMapping(value = "/password")
    public String updateUserPassword(@RequestBody User user){
        return userRepository.updateUserPassword(user);
    }


    @PatchMapping(value = "/token/reset/{nickname}")
    public void resetToken(HttpServletRequest request, String token,
                           @PathVariable String nickname, TokenInfo tokenInfo){

        userService.resetToken(request, token, nickname, tokenInfo);
    }

    @PatchMapping(value="/token/{nickname}")
    public ResponseEntity<String> updateToken(HttpServletRequest request, @Value("${jwt.secret}") String key, @RequestBody TokenInfo tokenInfo, String token, @PathVariable String nickname){

        String result=userService.checkToken(request, key, token, nickname);

        if(result.equals("possible to update token")){
            tokenInfo=userService.updateToken(tokenInfo);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userService.createCookie(tokenInfo).toString()).body(tokenInfo.getAccessToken());
        }
        else {
            return ResponseEntity.ok().body(result);
        }


    }


    @DeleteMapping(value = "/{nickname}/{role}")
    public void deleteUser(HttpServletRequest request, String token, @PathVariable String role, @PathVariable String nickname){
        if(tokenManager.checkAccessToken(request,  token, nickname) || role.equals("ADMIN")) {
            userRepository.deleteUser(nickname);
        }
    }



}
