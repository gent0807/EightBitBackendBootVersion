package com.eightbit.impl.file;

import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.BoardFileService;
import com.eightbit.persistence.file.attach.article.FreeArticleAttachFileRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    @Value("${file.dir}")
    private String dir;

    private final FolderAndFileManger folderAndFileManger;



    @Override
    public List<UploadFile> registerBoardFiles(String writer, String regdate, List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException {
        List<UploadFile> uploadFiles=new ArrayList<>();
        for (MultipartFile mf : files) {
            if (!(mf.isEmpty())) {
                UploadFile uploadFile = folderAndFileManger.storeBoardFile(mf, writer, regdate, dir, boardType, contentType, fileType);
                uploadFiles.add(uploadFile);
            }
        }
        return uploadFiles;
    }

    @Override
    public void removeBoardFile(UploadFile uploadFile, String boardType, String contentType, String fileType) {
        folderAndFileManger.removeBoardFile(uploadFile, boardType, contentType, fileType);
    }



}

