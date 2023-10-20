package com.eightbit.biz.user.persistence;

import com.eightbit.biz.user.vo.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Repository
@PropertySources({@PropertySource("classpath:auth.properties"), @PropertySource("classpath:upload.properties")})
@RequiredArgsConstructor
@Primary
public class UserMyBatisDAO {

    @Value("${file.dir}")
    private String filepath;

    private final SqlSessionTemplate mybatis;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs= 1000*60*60l;

    public UserVO getUserProfileImagePath(String nickname){
        return mybatis.selectOne("UserMyBatisDAO.getUserProfileImagePath", nickname);
    }

    public String getUserRole(String nickname){
        return mybatis.selectOne("UserMyBatisDAO.getRole", nickname);
    }

    public String getPassword(String nickname){
        return mybatis.selectOne("UserMyBatisDAO", nickname);
    }
    public String getAccessToken(String writer){
        return mybatis.selectOne("UserMyBatisDAO.getAccessToken", writer);
    }

    public String getRefreshToken(String writer) { return mybatis.selectOne("UserMyBatisDAO.getRefreshToken", writer);}
    public String alreadyEmailRegisterCheck(String email){
        String alreadyEmailRegister="no";
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(UserVO user:userVOList){
            if(encoder.matches(email, user.getEmail())){
                alreadyEmailRegister="yes";
                break;
            }
        }
        return alreadyEmailRegister;
    }

    public String alreadyEmailTempCheck(String email){
        String alreadyEmailRegister="no";
        List<TempVO> tempVOList=mybatis.selectList("UserMyBatisDAO.getTempList");
        for(TempVO temp:tempVOList){
            if(encoder.matches(email, temp.getEmail())){
                return temp.getEmail();
            }
        }
        return alreadyEmailRegister;
    }

    public String alreadyPhoneTempCheck(String phoneNumber){
        String alreadyPhoneRegister="no";
        List<PhoneVO> phoneVOList=mybatis.selectList("UserMyBatisDAO.getPhoneList");
        for(PhoneVO phone:phoneVOList){
            if(encoder.matches(phoneNumber, phone.getPhoneNum()))
            {
                    return phone.getPhoneNum();
            }
        }

        return alreadyPhoneRegister;
    }

    public String alreadyNickRegisterCheck(String nickname){
        String alreadyNickRegister="no";
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(UserVO user:userVOList){
            if(user.getNickname().equals(nickname)){
                alreadyNickRegister="yes";
                break;
            }
        }
        return alreadyNickRegister;
    }
    public String alreadyPasswordUsingCheck(UserVO userVO){
        String alreadyPasswordUsing="no";
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(UserVO user:userVOList){
            if(encoder.matches(userVO.getEmail(),user.getEmail())){
                String key=mybatis.selectOne("UserMyBatisDAO.getPassword", user.getEmail());
                if(encoder.matches(userVO.getPassword(), key)){
                    return alreadyPasswordUsing="yes";
                }
            }
        }
        return alreadyPasswordUsing;
    }
    public String checkRightAuthNum(TempVO tempVO){
        List<TempVO> tempVOList=mybatis.selectList("UserMyBatisDAO.getTempList");
        for(TempVO temp:tempVOList){
            if(encoder.matches(tempVO.getEmail(), temp.getEmail())){
                String auth_key=mybatis.selectOne("UserMyBatisDAO.getAuthNum", temp.getEmail());
                if(encoder.matches(tempVO.getAuthNum(),auth_key)){
                    return "yes";
                }
            }
        }
        return "no";

    }

    public String checkRightPhoneAuthNum(PhoneVO phoneVO){
        List<PhoneVO> phoneVOList=mybatis.selectList("UserMyBatisDAO.getPhoneList");
        for(PhoneVO phone:phoneVOList){
            if(encoder.matches(phoneVO.getPhoneNum(), phone.getPhoneNum())){
                String auth_key=mybatis.selectOne("UserMyBatisDAO.getPhoneAuthNum", phone.getPhoneNum());
                if(encoder.matches(phoneVO.getAuthNum(),auth_key)){
                    return "yes";
                }
            }
        }
        return "no";

    }
    public boolean insertUser(UserVO userVO) throws IOException {
        List<TempVO> tempVOList=mybatis.selectList("UserMyBatisDAO.getTempList");
        for(TempVO temp:tempVOList){
            if(encoder.matches(userVO.getEmail(), temp.getEmail())){
                System.out.println("회원 등록 작업 시작");

                mybatis.delete("UserMyBatisDAO.deleteTempRow", temp.getEmail());

                System.out.println("이메일 인증 테이블에서 삭제");

                userVO.setEmail(encoder.encode(userVO.getEmail()));

                if(storeProfileImage(userVO)){
                    System.out.println("스토어 프로필 이미지 저장 성공");
                    mybatis.insert("UserMyBatisDAO.insertUser", userVO);
                    return true;
                }


            }
        }
        return false;
    }

    public boolean storeProfileImage(UserVO userVO){
        System.out.println("프로필 이미지 저장 시작");

        File folder= new File(filepath+userVO.getNickname()+"/profileImage");

        boolean folderCreated= createDir(folder);

        if(folderCreated){
            System.out.println("폴더 생성");
        }
        else if(folderCreated==false){
            System.out.println("폴더 생성 X");
        }

        File file=new File(filepath+"default.jpg");

        File newFile=new File(filepath+userVO.getNickname()+"/profileImage/default.jpg");

        try {
            fileCopyAndPaste(file, newFile);
        } catch (IOException e) {
            System.out.println("프로필 이미지 저장 실패");
            return false;
        }

        return true;
    }

    public boolean createDir(File folder){
        if(!folder.exists()){
            return folder.mkdirs();
        }
        return false;
    }

    public void fileCopyAndPaste(File file, File newFile) throws IOException {
        FileInputStream input = new FileInputStream(file);
        FileOutputStream output = new FileOutputStream(newFile);

        byte[] buf = new byte[1024];

        int readData;
        while ((readData = input.read(buf)) > 0) {
            output.write(buf, 0, readData);
        }

        input.close();
        output.close();
    }

    public String insertTempUser(TempVO tempVO){
        mybatis.insert("UserMyBatisDAO.insertTempUser", tempVO);
        return "OK";
    }

    public String insertTempPhone(PhoneVO phoneVO){
        mybatis.insert("UserMyBatisDAO.insertPhone", phoneVO);
        return "OK";
    }
    public UserVO findByNickname(String username){
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(UserVO user:userVOList){
            if(user.getNickname().equals(username)){
                return user;
            }
        }
        return null;
    }
    public LoginResult login(UserVO userVO){
        LoginResult loginResult=new LoginResult();
        loginResult.setState("no");
        loginResult.setUser(null);
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        String email=userVO.getEmail();
        String password=userVO.getPassword();
        for(UserVO user:userVOList){
            if(encoder.matches(email, user.getEmail())){
                loginResult.setState("emailok");
                String key=mybatis.selectOne("UserMyBatisDAO.getUserPassword", user.getEmail());
                if(encoder.matches(password,key)){
                    loginResult.setState("allok");
                    loginResult.setUser(user);
                    break;
                }
                else{
                    break;
                }
            }
        }
        return loginResult;
    }

    public String updateUserPw(UserVO userVO){
        List<UserVO> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(UserVO user:userVOList){
            if(encoder.matches(userVO.getEmail(),user.getEmail())){
                userVO.setEmail(user.getEmail());
                mybatis.update("UserMyBatisDAO.updateUserPw", userVO);
            }
        }
        return "OK";
    }
    public void updateToken(TokenInfo tokenInfo){
        mybatis.update("UserMyBatisDAO.updateToken", tokenInfo);
    }
    public PointVO updatePoint(PointVO pointVO){
        mybatis.update("UserMyBatisDAO.updatePoint", pointVO);
        return mybatis.selectOne("UserMyBatisDAO.getPoint",pointVO.getNickname());
    }

    public String updateTempAuthNum(TempVO tempVO){
        mybatis.update("UserMyBatisDAO.updateTempAuthNum", tempVO);
        return "OK";
    }

    public String updateTempPhone(PhoneVO phoneVO){
        mybatis.update("UserMyBatisDAO.updatePhone", phoneVO);
        return "OK";
    }

    public void deleteUser(String email){
        mybatis.delete("UserMyBatisDAO.deleteUser", email);
    }

    public String deletePhoneNum(String phoneNum){
        List<PhoneVO> phoneVOList=mybatis.selectList("UserMyBatisDAO.getPhoneList");
        for(PhoneVO phone:phoneVOList){
            if(encoder.matches(phoneNum, phone.getPhoneNum())){
                mybatis.delete("UserMyBatisDAO.deletePhoneRow", phone.getPhoneNum());
                return "success";
            }
        }
        return "fail";
    }




}
