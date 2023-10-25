package com.eightbit.persistence.email;

import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.User;
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
public class EmailRepository {
    private final SqlSessionTemplate mybatis;
    private final BCryptPasswordEncoder encoder;

    public String alreadyEmailTempCheck(String email){
        String alreadyEmailRegister="no";
        List<Temp> tempList =mybatis.selectList("EmailMyBatisDAO.getTempList");
        for(Temp temp: tempList){
            if(encoder.matches(email, temp.getEmail())){
                return temp.getEmail();
            }
        }
        return alreadyEmailRegister;
    }


    public String insertTempUser(Temp temp){
        mybatis.insert("EmailMyBatisDAO.insertTempUser", temp);
        return "OK";
    }



    public String updateTempAuthNum(Temp temp){
        mybatis.update("EmailMyBatisDAO.updateTempAuthNum", temp);
        return "OK";
    }



    public String checkRightAuthNum(Temp tempVO){
        List<Temp> tempList =mybatis.selectList("EmailMyBatisDAO.getTempList");
        for(Temp temp: tempList){
            if(encoder.matches(tempVO.getEmail(), temp.getEmail())){
                String auth_key=mybatis.selectOne("EmailMyBatisDAO.getAuthNum", temp.getEmail());
                if(encoder.matches(tempVO.getAuthNum(),auth_key)){
                    return "yes";
                }
            }
        }
        return "no";

    }
}
