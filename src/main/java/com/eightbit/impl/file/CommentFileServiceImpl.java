package com.eightbit.impl.file;

import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.CommentFileService;
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
public class CommentFileServiceImpl implements CommentFileService {

    @Value("${file.dir}")
    private String dir;

    private final FolderAndFileManger folderAndFileManger;
    public List<UploadFile> registerCommentFiles(String commenter, String regdate, String original_writer, String original_regdate, List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException {
        List<UploadFile>  uploadFiles=new ArrayList<>();
        for(MultipartFile mf : files){
            if(!(mf.isEmpty())){
                UploadFile uploadFile = folderAndFileManger.storeCommentFile(mf, commenter, regdate, original_writer, original_regdate, dir, boardType, contentType, fileType);
                uploadFiles.add(uploadFile);
            }
        }
        return uploadFiles;
    }


    @Override
    public void removeCommentFile(UploadFile uploadFile, String original_writer, String original_regdate, String boardType, String contentType, String fileType){
        folderAndFileManger.removeCommentFile(uploadFile, original_writer, original_regdate,boardType, contentType, fileType);
    }


}
