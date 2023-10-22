package com.eightbit.biz.board.impl;

import com.eightbit.biz.board.inter.BoardService;
import com.eightbit.biz.board.persistence.BoardMyBatisDAO;
import com.eightbit.biz.board.vo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:upload.properties")
public class BoardServiceImpl implements BoardService {

    @Value("${file.dir}")
    private String dir;

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
    public UploadFile getAttachFile(UploadFile uploadFile){
        return boardMyBatisDAO.getAttachFile(uploadFile);
    }

    @Override
    public UploadFile getViewFile(UploadFile uploadFile) {
        return boardMyBatisDAO.getViewFile(uploadFile);
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
        String storeRegdate=regdate.replace(":","");
        String filepath=setPath(writer, storeRegdate, dir);
        System.out.println("enter boardservice register ArticleShareFiles, filepath : "+ filepath );
        createDir(filepath);

        storeFiles(writer, regdate, filepath, files);

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


    public void storeFiles(String writer, String regdate, String filepath, List<MultipartFile> files) throws IOException {


        for (MultipartFile mf : files) {

            if(!(mf.isEmpty())) {
                System.out.println("mf :"+mf);
                String originFilename = mf.getOriginalFilename();
                System.out.println("originFilename:"+originFilename);
                String storeFilename = createStoreFilename(originFilename);
                System.out.println("storeFilename:"+storeFilename);
                File f = new File(filepath + "/"+storeFilename);

                mf.transferTo(f);

                UploadFile sharefile = new UploadFile(writer, regdate, storeFilename, originFilename);

                boardMyBatisDAO.registerArticleShareFile(sharefile);
            }
        }


    }


    public String createStoreFilename(String originFilename) {
        String ext=extracted(originFilename);

        String uuid= UUID.randomUUID().toString();

        return uuid+"."+ext;
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
    public void modifyArticle(BoardVO boardVO) {
        boardMyBatisDAO.modifyArticle(boardVO);
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
    public void removeArticle(BoardVO boardVO) {
        if(boardMyBatisDAO.removeArticle(boardVO)){
            removeFilesAndFolder(boardVO.getWriter(), boardVO.getRegdate());
        }
    }

    @Override
    public void removeArticleShareFile(UploadFile uploadFile) {
        if(boardMyBatisDAO.removeArticleShareFile(uploadFile)){
            String regdate=uploadFile.getRegdate().replace(":","");
            String filepath=dir+uploadFile.getUploader()+"/board/article/"+regdate+"/sharefiles";

            File folder=new File(filepath);

            if(folder.exists()){
                File targetfile=new File(filepath+"/"+uploadFile.getStoreFilename());

                if(targetfile.isFile()){
                    targetfile.delete();
                }

                File[] list=folder.listFiles();

                if(list.length==0){
                    File parentFoler=new File(dir+uploadFile.getUploader()+"/board/article/"+regdate);
                    folder.delete();
                    parentFoler.delete();
                }
            }


        }
    }

    public void removeFilesAndFolder(String writer, String regdate){
        regdate=regdate.replace(":","");
        String filepath1=dir+writer+"/board/article/"+regdate+"/sharefiles";
        String filepath2=dir+writer+"/board/article/"+regdate;
        File folder1=new File(filepath1);
        File folder2=new File(filepath2);
        if(folder1.exists()){
            File[] folder_list = folder1.listFiles();

            for (int j = 0; j < folder_list.length; j++) {
                folder_list[j].delete(); //파일 삭제
            }

            folder1.delete();
            folder2.delete();
        }
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

