package com.eightbit.util.user;

import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class ProfileSaver {


    @Value("${file.dir}")
    private String filepath;

    private final SqlSessionTemplate mybatis;

    private final BCryptPasswordEncoder encoder;
    public boolean saveProfile(User user){
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
                    return true;
                }


            }
        }
        return false;
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
}
