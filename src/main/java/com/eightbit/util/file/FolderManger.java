package com.eightbit.util.file;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class FolderManger {

    @Value("${file.dir}")
    private String filepath;

    public boolean createDir(File folder){
        if(!folder.exists()){
            return folder.mkdirs();
        }
        return false;
    }


}
