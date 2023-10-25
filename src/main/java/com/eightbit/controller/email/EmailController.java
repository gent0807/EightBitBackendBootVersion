package com.eightbit.controller.email;

import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.User;
import com.eightbit.inter.email.EmailService;
import com.eightbit.persistence.email.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Email/*")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;
    private final EmailRepository emailRepository;


    @PostMapping(value="/check/authNum")
    public ResponseEntity<String> checkRightAuthNum(@RequestBody Temp temp){
        return ResponseEntity.ok().body(emailRepository.checkRightAuthNum(temp));
    }

    @PostMapping(value = "/authNum")
    public ResponseEntity<String> sendAuthNumToEmail(@RequestBody User user){

        return ResponseEntity.ok().body(emailService.mailSend(user.getEmail()));
    }



}
