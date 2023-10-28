package com.eightbit.util.user;

import com.eightbit.entity.email.Temp;
import com.eightbit.entity.user.User;
import com.eightbit.util.file.FolderManger;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class ProfileManager {

    @Value("${file.dir}")
    private String filepath;

    private final FolderManger folderManger;

    public boolean storeProfileImage(User user){

        File folder= new File(filepath+ user.getNickname()+"/profileImage");

        boolean folderCreated= folderManger.createDir(folder);

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
