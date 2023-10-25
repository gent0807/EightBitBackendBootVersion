package com.eightbit.persistence.phone;

import com.eightbit.entity.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Primary
public class PhoneRepository {
    private final SqlSessionTemplate mybatis;
    private final BCryptPasswordEncoder encoder;

    public String checkRightPhoneAuthNum(Phone phoneVO){
        List<Phone> phoneList =mybatis.selectList("PhoneMyBatisDAO.getPhoneList");
        for(Phone phone: phoneList){
            if(encoder.matches(phoneVO.getPhoneNum(), phone.getPhoneNum())){
                String auth_key=mybatis.selectOne("PhoneMyBatisDAO.getPhoneAuthNum", phone.getPhoneNum());
                if(encoder.matches(phoneVO.getAuthNum(),auth_key)){
                    return "yes";
                }
            }
        }
        return "no";
    }

    public String alreadyPhoneTempCheck(String phoneNumber){
        String alreadyPhoneRegister="no";
        List<Phone> phoneList =mybatis.selectList("PhoneMyBatisDAO.getPhoneList");
        for(Phone phone: phoneList){
            if(encoder.matches(phoneNumber, phone.getPhoneNum()))
            {
                return phone.getPhoneNum();
            }
        }

        return alreadyPhoneRegister;
    }

    public String insertTempPhone(Phone phone){
        mybatis.insert("PhoneMyBatisDAO.insertPhone", phone);
        return "OK";
    }

    public String updateTempPhone(Phone phone){
        mybatis.update("PhoneMyBatisDAO.updatePhone", phone);
        return "OK";
    }

    public String deletePhoneNum(String phoneNum){
        List<Phone> phoneList =mybatis.selectList("PhoneMyBatisDAO.getPhoneList");
        for(Phone phone: phoneList){
            if(encoder.matches(phoneNum, phone.getPhoneNum())){
                mybatis.delete("PhoneMyBatisDAO.deletePhoneRow", phone.getPhoneNum());
                return "success";
            }
        }
        return "fail";
    }


}
