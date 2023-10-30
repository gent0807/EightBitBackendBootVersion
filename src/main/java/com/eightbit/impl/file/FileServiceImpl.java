package com.eightbit.impl.file;

import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.FileService;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class FileServiceImpl implements FileService {


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


    public boolean removeFile(UploadFile uploadFile) {
        return folderAndFileManger.removeFile(uploadFile);
    }
}
