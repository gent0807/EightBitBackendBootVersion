package com.eightbit.inter.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.like.ArticleLike;
import com.eightbit.entity.like.ReCommentLike;
import com.eightbit.entity.like.CommentLike;
import com.eightbit.entity.recomment.ReComment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    public List<Article> getList();
    public List<Article> getUserArticles(String writer);
    public List<Comment> getReplies(Comment comment);
    public List<ReComment> getReComments(ReComment reComment);
    public Article getArticle(Article article);
    public List<UploadFile> getAttachList(UploadFile uploadFile);
    public UploadFile getAttachFile(UploadFile uploadFile);
    public UploadFile getViewFile(UploadFile uploadFile);
    public Comment getReply(Comment comment);
    public ReComment getReComment(ReComment reComment);
    public List<String> getArticleLikers(Article article);
    public List<String> getReplyLikers(Comment comment);
    public List<String> getReCommentLikers(ReComment reComment);
    public Integer getReCommentCount(Article article);
    public Article registerArticle(Article article);
    public Comment registerReply(Comment comment);
    public ReComment registerReComment(ReComment reComment);
    public void registerArticleView(ArticleView articleView);
    public List<String> registerArticleLike(ArticleLike articleLike);
    public List<String> registerReplyLike(CommentLike commentLike);
    public List<String> registerReCommentLike(ReCommentLike reCommentLike);
    public void registerArticleShareFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException;
    public List<UploadFile> registerArticleViewFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException;
    public List<UploadFile> registerReplyViewFiles(String replyer, String regdate, List<MultipartFile> files, String dir) throws  IOException;
    public List<UploadFile> registerReCommentViewFiles(String reCommenter, String regdate, List<MultipartFile> files, String dir) throws  IOException;
    public void modifyArticle(Article article);
    public void modifyReply(Comment comment);
    public void modifyReComment(ReComment reComment);

    public void removeArticle(Article article);
    public void removeArticleShareFile(UploadFile uploadFile);
    public void removeReply(Comment comment);
    public void removeReComment(ReComment reComment);
    public List<String> removeArticleLike(ArticleLike articleLike);
    public List<String> removeReplyLike(CommentLike commentLike);
    public List<String> removeReCommentLike(ReCommentLike reCommentLike);
}
