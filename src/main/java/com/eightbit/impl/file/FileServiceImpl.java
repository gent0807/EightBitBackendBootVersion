package com.eightbit.impl.file;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.FileService;
import com.eightbit.persistence.file.attach.article.FreeArticleAttachFileRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.dir}")
    private String dir;

    private final FolderAndFileManger folderAndFileManger;

    private final FreeArticleAttachFileRepository freeArticleAttachFileRepository;

    @Override
    public List<UploadFile> registerBoardFiles(String writer, String regdate, List<MultipartFile> files, String dir, String boardType, String contentType, String fileType) throws IOException {
        List<UploadFile> sharefiles=new ArrayList<>();
        for (MultipartFile mf : files) {
            if (!(mf.isEmpty())) {
                UploadFile sharefile = folderAndFileManger.storeBoardFile(mf, writer, regdate, dir, boardType, contentType, fileType);
                sharefiles.add(sharefile);
            }
        }
        return sharefiles;
    }

    @Override
    public void removeBoardFile(UploadFile uploadFile, String boardType, String contentType, String fileType) {
        folderAndFileManger.removeBoardFile(uploadFile, boardType, contentType, fileType);
    }



    @Override
    public List<UploadFile> registerReplyViewFiles(String replyer, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate = regdate.replace(":", "");
        String filepath = setReplyViewFilePath(replyer, storeRegdate, dir);
        createDir(filepath);

        return storeViewFiles(replyer, regdate, filepath, files, "reply_view");
    }

    @Override
    public List<UploadFile> registerReCommentViewFiles(String reCommenter, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate = regdate.replace(":", "");
        String filepath = setViewReCommentFilePath(reCommenter, storeRegdate, dir);
        createDir(filepath);

        return storeViewFiles(reCommenter, regdate, filepath, files, "reComment_view");
    }

    public String setShareFilePath(String writer, String regdate, String dir) {
        return dir + writer + "/board/article/" + regdate + "/sharefiles";
    }

    public String setViewFilePath(String writer, String regdate, String dir) {
        return dir + writer + "/board/article/" + regdate + "/viewfiles";
    }

    public String setReplyViewFilePath(String replyer, String regdate, String dir) {

        Article article = freeArticleRepository.getOriginWriterAndRegdate(new Comment(replyer, regdate));
        String origin_writer = article.getWriter();
        String origin_regdate = article.getRegdate();

        return dir + origin_writer + "/board/article/" + origin_regdate + "/reply/" + replyer + "/" + regdate + "/viewfiles";
    }

    public String setViewReCommentFilePath(String reCommenter, String regdate, String dir) {
        Comment comment = freeArticleRepository.getOriginReplyerAndRegdate(new Comment(reCommenter, regdate));
        String origin_replyer = comment.getAuthor();
        String origin_reply_regdate = comment.getRegdate();

        Article article = freeArticleRepository.getOriginWriterAndRegdate(new Comment(origin_replyer, origin_reply_regdate));
        String origin_writer = article.getWriter();
        String origin_regdate = article.getRegdate();

        return dir + origin_writer + "/board/article/" + origin_regdate + "/reply/" + origin_replyer + "/" + origin_reply_regdate + "/reComment/" + reCommenter + "/" + regdate + "/viewfiles";
    }

}

