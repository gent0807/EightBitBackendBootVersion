package com.eightbit.persistence.user;

import com.eightbit.entity.point.Point;
import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.LoginResult;
import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
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
public class UserRepository {

    @Value("${file.dir}")
    private String filepath;

    private final SqlSessionTemplate mybatis;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;
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

    public String getPassword(String nickname){
        return mybatis.selectOne("UserMyBatisDAO", nickname);
    }
    public String getAccessToken(String writer){
        return mybatis.selectOne("UserMyBatisDAO.getAccessToken", writer);
    }

    public String getRefreshToken(String writer) { return mybatis.selectOne("UserMyBatisDAO.getRefreshToken", writer);}

    public String insertUser(User user) throws IOException {
        List<Temp> tempList =mybatis.selectList("EmailMyBatisDAO.getTempList");
        for(Temp temp: tempList){
            if(encoder.matches(user.getEmail(), temp.getEmail())){
                System.out.println("회원 등록 작업 시작");

                mybatis.delete("EmailMyBatisDAO.deleteTempRow", temp.getEmail());

                System.out.println("이메일 인증 테이블에서 삭제");

                user.setEmail(encoder.encode(user.getEmail()));
                user.setPassword(encoder.encode(user.getPassword()));

                if(storeProfileImage(user)){
                    System.out.println("스토어 프로필 이미지 저장 성공");
                    mybatis.insert("UserMyBatisDAO.insertUser", user);
                    return "OK";
                }


            }
        }
        return "NO";
    }

    public boolean storeProfileImage(User user){
        System.out.println("프로필 이미지 저장 시작");

        File folder= new File(filepath+ user.getNickname()+"/profileImage");

        boolean folderCreated= createDir(folder);

        if(folderCreated){
            System.out.println("폴더 생성");
        }
        else if(folderCreated==false){
            System.out.println("폴더 생성 X");
        }

        File file=new File(filepath+"default.jpg");

        File newFile=new File(filepath+ user.getNickname()+"/profileImage/default.jpg");

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
    public User findByNickname(String username){
        List<User> userList =mybatis.selectList("UserMyBatisDAO.getUserList");
        for(User user: userList){
            if(user.getNickname().equals(username)){
                return user;
            }
        }
        return null;
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

    public Point updatePoint(Point point){
        mybatis.update("UserMyBatisDAO.updatePoint", point);
        return mybatis.selectOne("UserMyBatisDAO.getPoint", point.getNickname());
    }


    public void deleteUser(String email){
        mybatis.delete("UserMyBatisDAO.deleteUser", email);
    }






}
