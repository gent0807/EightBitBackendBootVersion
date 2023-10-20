package com.eightbit.biz.board.impl;

import com.eightbit.biz.board.inter.BoardService;
import com.eightbit.biz.board.persistence.BoardMyBatisDAO;
import com.eightbit.biz.board.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class BoardServiceImpl implements BoardService {

    private final BoardMyBatisDAO boardMyBatisDAO;

    @Override
    public List<BoardVO> getList() {
        return boardMyBatisDAO.getList();
    }
    @Override
    public List<BoardVO> getUserArticles(String writer) {
        return boardMyBatisDAO.getUserArticles(writer);
    }

    @Override
    public List<ReplyVO> getReplies(ReplyVO replyVO){
        return boardMyBatisDAO.getReplies(replyVO);
    }

    @Override
    public List<ReCommentVO> getReComments(ReCommentVO reCommentVO){
        return boardMyBatisDAO.getReComments(reCommentVO);
    }

    @Override
    public BoardVO getArticle(BoardVO boardVO) {
        return boardMyBatisDAO.getArticle(boardVO);
    }

    @Override
    public List<UploadFile> getAttachList(UploadFile uploadFile) {
        return boardMyBatisDAO.getAttachList(uploadFile);
    }

    @Override
    public UploadFile getUploadFile(UploadFile uploadFile){
        return boardMyBatisDAO.getUploadFile(uploadFile);
    }
    @Override
    public ReplyVO getReply(ReplyVO replyVO) {
        return boardMyBatisDAO.getReply(replyVO);
    }

    @Override
    public ReCommentVO getReComment(ReCommentVO reCommentVO) {
        return boardMyBatisDAO.getReComment(reCommentVO);
    }

    @Override
    public List<String> getArticleLikers(BoardVO boardVO) {
        return boardMyBatisDAO.getArticleLikers(boardVO);
    }

    @Override
    public List<String> getReplyLikers(ReplyVO replyVO) {
        return boardMyBatisDAO.getReplyLikers(replyVO);
    }

    @Override
    public List<String> getReCommentLikers(ReCommentVO reCommentVO) {
        return boardMyBatisDAO.getReCommentLikers(reCommentVO);
    }

    @Override
    public Integer getReCommentCount(BoardVO boardVO) {
        return boardMyBatisDAO.getReCommentCount(boardVO);
    }


    @Override
    public BoardVO registerArticle(BoardVO boardVO) {
        boardMyBatisDAO.registerArticle(boardVO);
        return boardMyBatisDAO.findWriterAndRegdate(boardMyBatisDAO.findSeqOfWriter(boardVO));
    }

    @Override
    public void registerArticleShareFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String filepath=setPath(writer, regdate, dir);

        createDir(filepath);

        List<UploadFile> sharefiles=storeFiles(writer, regdate, filepath, files);

        if(sharefiles.size()>0){
            boardMyBatisDAO.registerArticleShareFiles(sharefiles);
        }
    }


    public String setPath(String writer, String regdate, String dir) {
        return dir+writer + "/board/article/" + regdate + "/sharefiles";
    }


    public void createDir(String path) {
        File folder=new File(path);

        if (!folder.exists()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }


    public List<UploadFile> storeFiles(String writer, String regdate, String filepath, List<MultipartFile> files) throws IOException {

        List<UploadFile> sharefiles = new ArrayList<>();

        for (MultipartFile mf : files) {

            if(!(mf.isEmpty())) {

                String originFilename = mf.getOriginalFilename();
                String storeFileName = createStoreFilename(originFilename);

                File f = new File(filepath + "/"+storeFileName);

                mf.transferTo(f);


                UploadFile uploadFile = new UploadFile(writer, regdate, storeFileName, originFilename);

                sharefiles.add(uploadFile);
            }
        }

        return sharefiles;

    }


    public String createStoreFilename(String originFilename) {
        String ext=extracted(originFilename);

        String uuid= UUID.randomUUID().toString();

        return uuid+ext;
    }

    private String extracted(String originFilename){
        int pos=originFilename.lastIndexOf(".");
        return originFilename.substring(pos+1);
    }

    @Override
    public ReplyVO registerReply(ReplyVO replyVO){
        return  boardMyBatisDAO.registerReply(replyVO);
    }
    @Override
    public ReCommentVO registerReComment(ReCommentVO reCommentVO) {
        return boardMyBatisDAO.registerReComment(reCommentVO);
    }

    @Override
    public void registerArticleView(ArticleViewVO articleViewVO) {
        boardMyBatisDAO.registerArticleView(articleViewVO);
    }

    @Override
    public List<String> registerArticleLike(ArticleLikeVO articleLikeVO) {
        return boardMyBatisDAO.registerArticleLike(articleLikeVO);
    }

    @Override
    public List<String> registerReplyLike(ReplyLikeVO replyLikeVO) {
        return boardMyBatisDAO.registerReplyLike(replyLikeVO);
    }

    @Override
    public List<String> registerReCommentLike(ReCommentLikeVO reCommentLikeVO) {
        return boardMyBatisDAO.registerReCommentLike(reCommentLikeVO);
    }

    @Override
    public void modify(BoardVO boardVO) {
        boardMyBatisDAO.modify(boardVO);
    }
    @Override
    public void modifyReply(ReplyVO replyVO) {
        boardMyBatisDAO.modifyReply(replyVO);
    }
    @Override
    public void modifyReComment(ReCommentVO reCommentVO) {
        boardMyBatisDAO.modifyReComment(reCommentVO);
    }

    @Override
    public void modifyAbuseArticle(BoardVO boardVO) {
        boardMyBatisDAO.modifyAbuseArticle(boardVO);
    }

    @Override
    public void modify19Article(BoardVO boardVO) {
        boardMyBatisDAO.modify19Article(boardVO);
    }

    @Override
    public void modifyIncoporateArticle(BoardVO boardVO) {
        boardMyBatisDAO.modifyIncoporateArticle(boardVO);
    }

    @Override
    public void modifyAbuseReply(ReplyVO replyVO) {
        boardMyBatisDAO.modifyAbuseReply(replyVO);
    }

    @Override
    public void modify19Reply(ReplyVO replyVO) {
        boardMyBatisDAO.modify19Reply(replyVO);
    }

    @Override
    public void modifyIncoporateReply(ReplyVO replyVO) {
        boardMyBatisDAO.modifyIncoporateReply(replyVO);
    }

    @Override
    public void modifyAbuseReComment(ReCommentVO reCommentVO) {
        boardMyBatisDAO.modifyAbuseReComment(reCommentVO);
    }

    @Override
    public void modify19ReComment(ReCommentVO reCommentVO) {
        boardMyBatisDAO.modify19ReComment(reCommentVO);
    }

    @Override
    public void modifyIncoporateReComment(ReCommentVO reCommentVO) {
        boardMyBatisDAO.modifyIncoporateReComment(reCommentVO);
    }

    @Override
    public void remove(BoardVO boardVO) {
        boardMyBatisDAO.remove(boardVO);
    }
    @Override
    public void removeReply(ReplyVO replyVO) {
        boardMyBatisDAO.removeReply(replyVO);
    }
    @Override
    public void removeReComment(ReCommentVO reCommentVO) {
        boardMyBatisDAO.removeReComment(reCommentVO);
    }

    @Override
    public List<String> removeArticleLike(ArticleLikeVO articleLikeVO) {
        return boardMyBatisDAO.removeArticleLike(articleLikeVO);
    }

    @Override
    public List<String> removeReplyLike(ReplyLikeVO replyLikeVO) {
        return boardMyBatisDAO.removeReplyLike(replyLikeVO);
    }

    @Override
    public List<String> removeReCommentLike(ReCommentLikeVO reCommentLikeVO) {
        return boardMyBatisDAO.removeReCommentLike(reCommentLikeVO);
    }
}

