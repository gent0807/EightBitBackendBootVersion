package com.eightbit.view_controller.user_controller;

import com.eightbit.biz.user.inter.MailSendService;
import com.eightbit.biz.user.inter.PhoneSendService;
import com.eightbit.biz.user.inter.UserService;
import com.eightbit.biz.user.vo.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    private final MailSendService mailSendService;


    private final PhoneSendService phoneSendService;


    @ResponseBody
    @GetMapping(value = "/profileImg/{user}")
    public Resource getUserProfileImg(@PathVariable String user, @Value("${file.dir}") String filepath) throws MalformedURLException {
        UserVO userVO=userService.getUserProfileImagePath(user);
        return new UrlResource("file:"+filepath+user+"/profileImage/"+userVO.getProfileImgPath());
    }

    @GetMapping(value = "/role")
    public ResponseEntity<String> getUserRole(@RequestParam String nickname){
        return ResponseEntity.ok().body(userService.getUserRole(nickname));
    }

    @PostMapping(value = "/check/email/already")
    public ResponseEntity<String> alreadyEmailRegisterCheck(@RequestBody UserVO userVO){
        return ResponseEntity.ok().body(userService.alreadyEmailRegisterCheck(userVO.getEmail()));
    }

    @PostMapping(value = "/check/nick/already")
    public ResponseEntity<String> alreadyNickRegisterCheck(@RequestBody UserVO userVO){
        return ResponseEntity.ok().body(userService.alreadyNickRegisterCheck(userVO.getNickname()));
    }

    @PostMapping(value = "/check/password/already")
    public ResponseEntity<String> alreadyPasswordUsingCheck(@RequestBody UserVO userVO){
        return ResponseEntity.ok().body(userService.alreadyPasswordUsingCheck(userVO));
    }

    @PostMapping(value="/check/authkey")
    public ResponseEntity<String> checkRightAuthNum(@RequestBody TempVO tempVO){
        return ResponseEntity.ok().body(userService.checkRightAuthNum(tempVO));
    }

    @PostMapping(value="/check/phonekey")
    public ResponseEntity<String> checkRightPhoneAuthNum(@RequestBody PhoneVO phoneVO){
        return ResponseEntity.ok().body(userService.checkRightPhoneAuthNum(phoneVO));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<String> insertUser(@RequestBody UserVO userVO) throws IOException {
        return ResponseEntity.ok().body(userService.insertUser(userVO));
    }

    @PostMapping(value = "/check/login")
    public ResponseEntity<TokenInfo> login(@RequestBody UserVO userVO){
        /*
        TokenInfo tokenInfo=userService.login(userVO);

        ResponseCookie responseCookie=ResponseCookie.from("refreshToken","")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*60*24)
                .domain("localhost")
                .build();

        String refreshToken = tokenInfo.getRefreshToken();

        if(refreshToken!=null){
            refreshToken=encoder.encode(refreshToken);
            responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60*60*24)
                    .domain("localhost")
                    .build();
            tokenInfo.setRefreshToken(null);
        }


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(tokenInfo);*/

        return ResponseEntity.ok().body(userService.login(userVO));
    }

    @PutMapping(value = "/password")
    public String updateUserPw(@RequestBody UserVO userVO){
        return userService.updateUserPw(userVO);
    }

    @PatchMapping(value = "/token/reset/{nickname}")
    public void resetToken(HttpServletRequest request, String token,
                           @PathVariable String nickname, TokenInfo tokenInfo){
        if(checkAccessToken(request,  token, nickname)){
            tokenInfo.setNickName(nickname);
            userService.updateToken(tokenInfo);
        }
    }

    @PatchMapping(value="/token/{nickname}")
    public ResponseEntity<String> updateToken(HttpServletRequest request, @Value("${jwt.secret}") String key, String token, TokenInfo tokenInfo, @PathVariable String nickname){
        if(validateAccessToken(request, token, nickname, key).equals("expired") ){
            if(checkAccessToken(request, token, nickname)){
                if(validateRefreshToken(request, token, nickname, key).equals("valid")){
                    if(checkRefreshToken(request,token,nickname)){
                        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(nickname,userService.getPassword(nickname));
                        Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                        String authorities = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));

                        long now = (new Date()).getTime();
                        Date accessTokenExpiresIn = new Date(now + 86400000);
                        Date refreshTokenExpiresIn = new Date(now + 86400000);

                        String accessToken = Jwts.builder()
                                .setSubject(authentication.getName())
                                .claim("auth", authorities)
                                .setExpiration(accessTokenExpiresIn)
                                .signWith(SignatureAlgorithm.HS256, key)
                                .compact();


                        String refreshToken = Jwts.builder()
                                .setExpiration(refreshTokenExpiresIn)
                                .signWith(SignatureAlgorithm.HS256, key)
                                .compact();

                        ResponseCookie responseCookie = ResponseCookie.from("refreshToken",refreshToken)
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(60*60*24)
                                .domain("localhost")
                                .build();

                        tokenInfo.setNickName(nickname);
                        tokenInfo.setAccessToken(accessToken);
                        tokenInfo.setRefreshToken(refreshToken);

                        tokenInfo.setRefreshToken(null);

                        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(accessToken);
                    }
                    else {
                        return ResponseEntity.ok().body("refreshtoken not matched user");
                    }
                }
                else if(validateRefreshToken(request, token, nickname, key).equals("expired")){
                    return ResponseEntity.ok().body("refreshtoken expired");
                }
                else{
                    return ResponseEntity.ok().body("refreshtoken invalid");
                }
            }
            else{
                return ResponseEntity.ok().body("accesstoken not matched user");
            }
        }
        else if(validateAccessToken(request, token, nickname, key).equals("valid") ){
            return ResponseEntity.ok().body("accesstoken valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PatchMapping(value = "/point/up")
    public ResponseEntity<PointVO> pointUp(@RequestParam String writer, @RequestParam int point, PointVO pointVO){
        pointVO.setNickname(writer);
        pointVO.setPoint(point);
        return ResponseEntity.ok().body(userService.updatePoint(pointVO));
    }

    @DeleteMapping(value = "/{nickname}/{role}")
    public void deleteUser(HttpServletRequest request, String token, @PathVariable String role, @PathVariable String nickname){
        if(checkAccessToken(request,  token, nickname) || role.equals("ADMIN")) {
            userService.deleteUser(nickname);
        }
    }

    @DeleteMapping(value="/phone")
    public String deletePhoneNum(@RequestBody PhoneVO phoneVO){
        return userService.deletePhoneNum(phoneVO.getPhoneNum());
    }

    @PostMapping(value = "/authkey/email")
    public ResponseEntity<String> sendAuthNumToEmail(@RequestBody UserVO userVO){

        return ResponseEntity.ok().body(mailSendService.mailSend(userVO.getEmail()));
    }

    @PostMapping(value = "/authkey/phone")
    public ResponseEntity<String> sendAuthNumToPhone(@RequestBody PhoneVO phoneVO){
        return ResponseEntity.ok().body(phoneSendService.phoneSend(phoneVO.getPhoneNum()));
    }

    public boolean checkAccessToken(HttpServletRequest request,  String token, String writer){
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            token=bearerToken.substring(7);
        }

        return token.equals(userService.getAccessToken(writer));
    }

    public boolean checkRefreshToken(HttpServletRequest request,  String token, String writer){
        Cookie[] cookies  = request.getCookies();

        for(Cookie cookie:cookies) {
            if(cookie.getName().equals("refreshToken")) {
                token=cookie.getValue();
            }
        }

        return token.equals(userService.getRefreshToken(writer));
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
