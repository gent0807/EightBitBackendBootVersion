package com.eightbit.persistence.user;


import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.LoginResult;
import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import com.eightbit.util.file.FolderAndFileManger;
import com.eightbit.util.user.ProfileManager;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@PropertySources({@PropertySource("classpath:auth.properties"), @PropertySource("classpath:upload.properties")})
@RequiredArgsConstructor
@Primary
public class UserRepository {


    private final SqlSessionTemplate mybatis;

    private final BCryptPasswordEncoder encoder;

    private final ProfileManager profileManager;

    private final FolderAndFileManger folderAndFileManger;

    private Long expiredMs= 1000*60*60l;

    public String alreadyNickRegisterCheck(String nickname){
        String alreadyNickRegister="no";
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user: userList){
            if(user.getNickname().equals(nickname)){
                alreadyNickRegister="yes";
                break;
            }
        }
        return alreadyNickRegister;
    }
    public String alreadyPasswordUsingCheck(User userVO){
        String alreadyPasswordUsing="no";
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user: userList){
            if(encoder.matches(userVO.getEmail(),user.getEmail())){
                if(encoder.matches(userVO.getPassword(), user.getPassword())){
                    alreadyPasswordUsing="yes";
                    return alreadyPasswordUsing;
                }
            }
        }
        return alreadyPasswordUsing;
    }

    public String alreadyEmailRegisterCheck(String email){
        String alreadyEmailRegister="no";
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user: userList){
            if(encoder.matches(email, user.getEmail())){
                alreadyEmailRegister="yes";
                break;
            }
        }
        return alreadyEmailRegister;
    }

    public String getUserProfileImagePath(String nickname){
        return mybatis.selectOne("UserMyBatisDAO.getUserProfileImagePath", nickname);
    }

    public String getUserRole(String nickname){
        return mybatis.selectOne("UserMyBatisDAO.getRole", nickname);
    }

    public String getUserEmail(String nickname){
        return mybatis.selectOne("UserMyBatisDAO.getEmail", nickname);
    }
    public String getPassword(String nickname){
        return mybatis.selectOne("UserMyBatisDAO", nickname);
    }
    public String getAccessToken(String writer){
        return mybatis.selectOne("UserMyBatisDAO.getAccessToken", writer);
    }

    public String getRefreshToken(String writer) { return mybatis.selectOne("UserMyBatisDAO.getRefreshToken", writer);}

    public String insertUser(User user) throws IOException {
        mybatis.insert("UserMyBatisDAO.insertUser", user);
        return "OK";
    }

    public User findByNickname(String username){
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user: userList){
            if(user.getNickname().equals(username)){
                return user;
            }
        }
        return null;
    }
    public boolean insertProfileImg(User user){
        List<Temp> tempList =mybatis.selectList("EmailMyBatisDAO.getTempList");
        for(Temp temp: tempList){
            if(encoder.matches(user.getEmail(), temp.getEmail())){

                mybatis.delete("EmailMyBatisDAO.deleteTempRow", temp.getEmail());

                System.out.println("이메일 인증 테이블에서 삭제");

                user.setEmail(encoder.encode(user.getEmail()));
                user.setPassword(encoder.encode(user.getPassword()));

                if(profileManager.storeProfileImage(user)){
                    System.out.println("스토어 프로필 이미지 저장 성공");
                    return true;
                }


            }
        }
        return false;
    }
    public LoginResult login(User userVO){
        LoginResult loginResult=new LoginResult();
        loginResult.setState("no");
        loginResult.setUser(null);
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        String email=userVO.getEmail();
        String password=userVO.getPassword();
        for(User user: userList){
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

    public String updateUserPassword(User userVO){
        List<User> userVOList=mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user:userVOList){
            if(encoder.matches(userVO.getEmail(),user.getEmail())){
                user.setPassword(encoder.encode(userVO.getPassword()));
                mybatis.update("UserMyBatisDAO.updateUserPassword", user);
            }
        }
        return "OK";
    }

    public void updateToken(TokenInfo tokenInfo){
        mybatis.update("UserMyBatisDAO.updateToken", tokenInfo);
    }


    public void deleteUser(String nickname){
        mybatis.delete("UserMyBatisDAO.deleteUser", nickname);
        folderAndFileManger.removeUserFilesAndFolder(nickname);
    }



}
