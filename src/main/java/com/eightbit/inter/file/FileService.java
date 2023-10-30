package com.eightbit.inter.file;

import com.eightbit.entity.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileService {
    public List<UploadFile> registerFiles(String uploader, String regdate, List<MultipartFile> files,
                                          String contentType, String storeType, int depth) throws IOException;

    public boolean removeFile(UploadFile uploadFile);

}
