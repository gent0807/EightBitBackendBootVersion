package com.eightbit.controller.phone;

import com.eightbit.entity.phone.Phone;
import com.eightbit.inter.phone.PhoneService;
import com.eightbit.persistence.phone.PhoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Phone/*")
@RequiredArgsConstructor
@Slf4j
public class PhoneController {
    private final PhoneService phoneService;
    private final PhoneRepository phoneRepository;

    @PostMapping(value = "/authNum")
    public ResponseEntity<String> sendAuthNumToPhone(@RequestBody Phone phone){
        return ResponseEntity.ok().body(phoneService.phoneSend(phone.getPhoneNum()));
    }

    @PostMapping(value="/check/authNum")
    public ResponseEntity<String> checkRightPhoneAuthNum(@RequestBody Phone phone){
        return ResponseEntity.ok().body(phoneRepository.checkRightPhoneAuthNum(phone));
    }

    @DeleteMapping(value="/phone")
    public String deletePhoneNum(@RequestBody Phone phone){
        return phoneRepository.deletePhoneNum(phone.getPhoneNum());
    }

}
