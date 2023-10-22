package com.eightbit.biz.board.inter;

import com.eightbit.biz.board.vo.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface BoardService {
    public List<BoardVO> getList();
    public List<BoardVO> getUserArticles(String writer);
    public List<ReplyVO> getReplies(ReplyVO replyVO);
    public List<ReCommentVO> getReComments(ReCommentVO reCommentVO);
    public BoardVO getArticle(BoardVO boardVO);
    public List<UploadFile> getAttachList(UploadFile uploadFile);
    public UploadFile getAttachFile(UploadFile uploadFile);
    public UploadFile getViewFile(UploadFile uploadFile);
    public ReplyVO getReply(ReplyVO replyVO);
    public ReCommentVO getReComment(ReCommentVO reCommentVO);
    public List<String> getArticleLikers(BoardVO boardVO);
    public List<String> getReplyLikers(ReplyVO replyVO);
    public List<String> getReCommentLikers(ReCommentVO reCommentVO);
    public Integer getReCommentCount(BoardVO boardVO);
    public BoardVO registerArticle(BoardVO boardVO);
    public ReplyVO registerReply(ReplyVO replyVO);
    public ReCommentVO registerReComment(ReCommentVO reCommentVO);
    public void registerArticleView(ArticleViewVO articleViewVO);
    public List<String> registerArticleLike(ArticleLikeVO articleLikeVO);
    public List<String> registerReplyLike(ReplyLikeVO replyLikeVO);
    public List<String> registerReCommentLike(ReCommentLikeVO reCommentLikeVO);
    public void registerArticleShareFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException;
    public String registerArticleViewFiles(String writer, String regdate, List<MultipartFile> files, String dir);
    public void modifyArticle(BoardVO boardVO);
    public void modifyReply(ReplyVO replyVO);
    public void modifyReComment(ReCommentVO reCommentVO);
    public void modifyAbuseArticle(BoardVO boardVO);
    public void modify19Article(BoardVO boardVO);
    public void modifyIncoporateArticle(BoardVO boardVO);
    public void modifyAbuseReply(ReplyVO replyVO);
    public void modify19Reply(ReplyVO replyVO);
    public void modifyIncoporateReply(ReplyVO replyVO);
    public void modifyAbuseReComment(ReCommentVO reCommentVO);
    public void modify19ReComment(ReCommentVO reCommentVO);
    public void modifyIncoporateReComment(ReCommentVO reCommentVO);
    public void removeArticle(BoardVO boardVO);
    public void removeArticleShareFile(UploadFile uploadFile);
    public void removeReply(ReplyVO replyVO);
    public void removeReComment(ReCommentVO reCommentVO);
    public List<String> removeArticleLike(ArticleLikeVO articleLikeVO);
    public List<String> removeReplyLike(ReplyLikeVO replyLikeVO);
    public List<String> removeReCommentLike(ReCommentLikeVO reCommentLikeVO);
}
