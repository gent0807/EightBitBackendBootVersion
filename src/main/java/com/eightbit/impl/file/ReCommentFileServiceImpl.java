package com.eightbit.impl.file;

import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.ReCommentFileService;
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
public class ReCommentFileServiceImpl implements ReCommentFileService {

    @Value("${file.dir}")
    private String dir;

    private final FolderAndFileManger folderAndFileManger;

    public List<UploadFile> registerReCommentFiles(String reCommenter, String regdate, String original_commenter, String original_comment_regdate,
                                                   String original_writer, String original_write_regdate,
                                                   List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException {
        List<UploadFile>  uploadFiles=new ArrayList<>();
        for(MultipartFile mf : files){
            if(!(mf.isEmpty())){
                UploadFile uploadFile = folderAndFileManger.storeReCommentFile(mf,reCommenter,regdate, original_commenter, original_comment_regdate, original_writer, original_write_regdate,dir, boardType, contentType, fileType);
                uploadFiles.add(uploadFile);
            }
        }
        return uploadFiles;
    }

    @Override
    public void removeReCommentFile(UploadFile uploadFile, String original_commenter, String original_comment_regdate, String original_writer, String original_write_regdate, String boardType, String contentType, String fileType){
        folderAndFileManger.removeReCommentFile(uploadFile, original_commenter, original_comment_regdate, original_writer, original_write_regdate, boardType, contentType, fileType);
    }

}
