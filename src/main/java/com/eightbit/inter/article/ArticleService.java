package com.eightbit.inter.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    public List<Article> getList();
    public List<Article> getUserArticles(String writer);

    public List<UploadFile> getAttachList(UploadFile uploadFile);
    public UploadFile getAttachFile(UploadFile uploadFile);
    public UploadFile getViewFile(UploadFile uploadFile);
    public Article registerArticle(Article article);
    public void registerArticleShareFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException;
    public List<UploadFile> registerArticleViewFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException;
    public List<UploadFile> registerReplyViewFiles(String replyer, String regdate, List<MultipartFile> files, String dir) throws  IOException;
    public List<UploadFile> registerReCommentViewFiles(String reCommenter, String regdate, List<MultipartFile> files, String dir) throws  IOException;
    public void modifyArticle(Article article);
    public void removeArticle(Article article);
    public void removeArticleShareFile(UploadFile uploadFile);

}
