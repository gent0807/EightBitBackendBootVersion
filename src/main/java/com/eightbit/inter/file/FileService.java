package com.eightbit.inter.file;

import com.eightbit.entity.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    public List<UploadFile> registerBoardFiles(String writer, String regdate, List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException;

    public void removeBoardFile(UploadFile uploadFile, String boardType, String contentType, String fileType);


}
