package com.eightbit.inter.file;

import com.eightbit.entity.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReCommentFileService {

    public List<UploadFile> registerReCommentFiles(String reCommenter, String regdate, String original_commenter, String original_comment_regdate,
                                                   String original_writer, String original_write_regdate, List<MultipartFile> files, String dir,
                                                   String boardType, String contentType, String fileType) throws IOException;
    public void removeReCommentFile(UploadFile uploadFile, String original_commenter, String original_comment_regdate,
                                    String original_writer, String original_write_regdate,
                                    String boardType, String contentType, String fileType);
}
