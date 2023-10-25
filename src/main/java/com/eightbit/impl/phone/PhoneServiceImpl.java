package com.eightbit.impl.phone;

import com.eightbit.inter.phone.PhoneService;
import com.eightbit.persistence.phone.PhoneRepository;
import com.eightbit.persistence.user.UserRepository;
import com.eightbit.entity.phone.Phone;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Primary
public class PhoneServiceImpl implements PhoneService {


    private final PhoneRepository phoneRepository;

    private final BCryptPasswordEncoder encoder;
    private int authNumber;

    public void makeRandomNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        int checkNum = r.nextInt(888888) + 111111;
        System.out.println("인증번호 : " + checkNum);
        authNumber = checkNum;
    }
    public String phoneSend(String phoneNumber){
        Phone phone =new Phone();
        System.out.println(phoneNumber);
        makeRandomNumber();
        String api_key = "NCSZ9O48UPYJVYUA";
        String api_secret = "BAGI384GJO7ST4UNDQQ17GPHH9ULN1NH";
        Message coolsms=new Message(api_key,api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01056417730");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[8bit]" +
                "안녕하세요. 8bit입니다. 요청하신 인증번호는" + "["+authNumber+"]" + "입니다. 감사합니다."); // 문자 내용 입력
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println("핸드폰 인증번호 전송 성공");
            System.out.println(obj.toString());
            phone.setAuthNum(encoder.encode(Integer.toString(authNumber)));
            System.out.println(phone.getAuthNum());
            String check= phoneRepository.alreadyPhoneTempCheck(phoneNumber);
            System.out.println(check);
            if(check.equals("no")){
                phone.setPhoneNum(encoder.encode(phoneNumber));
                return phoneRepository.insertTempPhone(phone);
            }
            else{
                phone.setPhoneNum(check);
                return phoneRepository.updateTempPhone(phone);
            }
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
            String errormsg="인증번호 전송에 실패했습니다";
            e.printStackTrace();
            return errormsg;
        }

    }



}
