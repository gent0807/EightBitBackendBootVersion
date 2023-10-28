package com.eightbit.inter.file;

import com.eightbit.entity.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentFileService {
    public List<UploadFile> registerCommentFiles(String commenter, String regdate, String oiriginal_writer, String original_regdate, List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException;
    public void removeCommentFile(UploadFile uploadFile, String original_writer, String original_regdate, String boardType, String contentType, String fileType);
}
