package com.eightbit.impl.file;


import com.eightbit.entity.file.UploadFile;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class FileInstaller {

    private final FolderAndFileManger folderAndFileManger;

    public List<UploadFile> registerFiles(String uploader, String regdate, List<MultipartFile> files,
                                          String contentType, String storeType, int depth) throws IOException {
        List<UploadFile> uploadFiles=new ArrayList<>();
        for (MultipartFile mf : files) {
            if (!(mf.isEmpty())) {
                UploadFile uploadFile = folderAndFileManger.storeFile(mf, uploader, regdate, contentType,storeType, depth);
                uploadFiles.add(uploadFile);
            }
        }
        return uploadFiles;
    }


    public void removeFile(UploadFile uploadFile) {
        folderAndFileManger.removeFile(uploadFile);
    }
}

