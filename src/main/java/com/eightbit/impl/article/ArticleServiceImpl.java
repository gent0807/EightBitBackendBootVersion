package com.eightbit.impl.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.like.ArticleLike;
import com.eightbit.entity.like.ReCommentLike;
import com.eightbit.entity.like.CommentLike;
import com.eightbit.entity.recomment.ReComment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.persistence.article.FreeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:upload.properties")
public class ArticleServiceImpl implements ArticleService {

    @Value("${file.dir}")
    private String dir;

    private final FreeRepository freeRepository;

    @Override
    public List<Article> getList() {
        return freeRepository.getList();
    }
    @Override
    public List<Article> getUserArticles(String writer) {
        return freeRepository.getUserArticles(writer);
    }

    @Override
    public List<Comment> getReplies(Comment comment){
        return freeRepository.getReplies(comment);
    }

    @Override
    public List<ReComment> getReComments(ReComment reComment){
        return freeRepository.getReComments(reComment);
    }

    @Override
    public Article getArticle(Article article) {
        return freeRepository.getArticle(article);
    }

    @Override
    public List<UploadFile> getAttachList(UploadFile uploadFile) {
        return freeRepository.getAttachList(uploadFile);
    }

    @Override
    public UploadFile getAttachFile(UploadFile uploadFile){
        return freeRepository.getAttachFile(uploadFile);
    }

    @Override
    public UploadFile getViewFile(UploadFile uploadFile) {
        return freeRepository.getViewFile(uploadFile);
    }

    @Override
    public Comment getReply(Comment comment) {
        return freeRepository.getReply(comment);
    }

    @Override
    public ReComment getReComment(ReComment reComment) {
        return freeRepository.getReComment(reComment);
    }

    @Override
    public List<String> getArticleLikers(Article article) {
        return freeRepository.getArticleLikers(article);
    }

    @Override
    public List<String> getReplyLikers(Comment comment) {
        return freeRepository.getReplyLikers(comment);
    }

    @Override
    public List<String> getReCommentLikers(ReComment reComment) {
        return freeRepository.getReCommentLikers(reComment);
    }

    @Override
    public Integer getReCommentCount(Article article) {
        return freeRepository.getReCommentCount(article);
    }


    @Override
    public Article registerArticle(Article article) {
        freeRepository.registerArticle(article);
        return freeRepository.findWriterAndRegdate(freeRepository.findSeqOfWriter(article));
    }

    @Override
    public void registerArticleShareFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setShareFilePath(writer, storeRegdate, dir);
        createDir(filepath);

        storeShareFiles(writer, regdate, filepath, files, "article_share");

    }

    @Override
    public List<UploadFile> registerArticleViewFiles(String writer, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setViewFilePath(writer, storeRegdate, dir);
        createDir(filepath);

        return storeViewFiles(writer,regdate,filepath,files, "article_view");
    }

    @Override
    public List<UploadFile> registerReplyViewFiles(String replyer, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setReplyViewFilePath(replyer, storeRegdate, dir);
        createDir(filepath);

        return storeViewFiles(replyer, regdate, filepath, files,"reply_view");
    }

    @Override
    public List<UploadFile> registerReCommentViewFiles(String reCommenter, String regdate, List<MultipartFile> files, String dir) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setViewReCommentFilePath(reCommenter, storeRegdate, dir);
        createDir(filepath);

        return storeViewFiles(reCommenter, regdate, filepath, files,"reComment_view");
    }

    public String setShareFilePath(String writer, String regdate, String dir) {
        return dir+writer + "/board/article/" + regdate + "/sharefiles";
    }

    public String setViewFilePath(String writer, String regdate, String dir) {
        return dir+writer + "/board/article/" + regdate + "/viewfiles";
    }

    public String setReplyViewFilePath(String replyer, String regdate, String dir){

        Article article = freeRepository.getOriginWriterAndRegdate(new Comment(replyer,regdate));
        String origin_writer= article.getWriter();
        String origin_regdate= article.getRegdate();

        return dir+origin_writer+"/board/article/"+origin_regdate+"/reply/"+replyer+"/"+regdate+"/viewfiles";
    }

    public String setViewReCommentFilePath(String reCommenter, String regdate, String dir){
        Comment comment = freeRepository.getOriginReplyerAndRegdate(new ReComment(reCommenter, regdate));
        String origin_replyer= comment.getReplyer();
        String origin_reply_regdate= comment.getRegdate();

        Article article = freeRepository.getOriginWriterAndRegdate(new Comment(origin_replyer,origin_reply_regdate));
        String origin_writer= article.getWriter();
        String origin_regdate= article.getRegdate();

        return dir+origin_writer+"/board/article/"+origin_regdate+"/reply/"+origin_replyer+"/"+origin_reply_regdate+"/reComment/"+reCommenter+"/"+regdate+"/viewfiles";
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

    public void storeShareFiles(String writer, String regdate, String filepath, List<MultipartFile> files, String type) throws IOException {

        for (MultipartFile mf : files) {

            if(!(mf.isEmpty())) {
                UploadFile sharefile = storeFile(mf, writer, regdate, filepath);
                freeRepository.registerFile(sharefile, type);
            }
        }
    }

    public List<UploadFile> storeViewFiles(String writer, String regdate, String filepath, List<MultipartFile> files, String type) throws IOException {

        List<UploadFile> viewfiles=new ArrayList<>();

        for (MultipartFile mf : files) {

            if(!(mf.isEmpty())) {
                UploadFile viewfile = storeFile(mf, writer, regdate, filepath);
                viewfile.setId(freeRepository.registerFile(viewfile, type));
                viewfiles.add(viewfile);
            }
        }
        return viewfiles;

    }

    public UploadFile storeFile(MultipartFile mf, String writer, String regdate, String filepath) throws IOException {
        System.out.println("mf :"+mf);
        String originFilename = mf.getOriginalFilename();
        System.out.println("originFilename:"+originFilename);
        String storeFilename = createStoreFilename(originFilename);
        System.out.println("storeFilename:"+storeFilename);
        File f = new File(filepath + "/"+storeFilename);

        mf.transferTo(f);

        return new UploadFile(writer, regdate, storeFilename, originFilename);
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
    public Comment registerReply(Comment comment){
        return  freeRepository.registerReply(comment);
    }
    @Override
    public ReComment registerReComment(ReComment reComment) {
        return freeRepository.registerReComment(reComment);
    }

    @Override
    public void registerArticleView(ArticleView articleView) {
        freeRepository.registerArticleView(articleView);
    }

    @Override
    public List<String> registerArticleLike(ArticleLike articleLike) {
        return freeRepository.registerArticleLike(articleLike);
    }

    @Override
    public List<String> registerReplyLike(CommentLike commentLike) {
        return freeRepository.registerReplyLike(commentLike);
    }

    @Override
    public List<String> registerReCommentLike(ReCommentLike reCommentLike) {
        return freeRepository.registerReCommentLike(reCommentLike);
    }

    @Override
    public void modifyArticle(Article article) {
        freeRepository.modifyArticle(article);
    }
    @Override
    public void modifyReply(Comment comment) {
        freeRepository.modifyReply(comment);
    }
    @Override
    public void modifyReComment(ReComment reComment) {
        freeRepository.modifyReComment(reComment);
    }

    @Override
    public void modifyAbuseArticle(Article article) {
        freeRepository.modifyAbuseArticle(article);
    }

    @Override
    public void modify19Article(Article article) {
        freeRepository.modify19Article(article);
    }

    @Override
    public void modifyIncoporateArticle(Article article) {
        freeRepository.modifyIncoporateArticle(article);
    }

    @Override
    public void modifyAbuseReply(Comment comment) {
        freeRepository.modifyAbuseReply(comment);
    }

    @Override
    public void modify19Reply(Comment comment) {
        freeRepository.modify19Reply(comment);
    }

    @Override
    public void modifyIncoporateReply(Comment comment) {
        freeRepository.modifyIncoporateReply(comment);
    }

    @Override
    public void modifyAbuseReComment(ReComment reComment) {
        freeRepository.modifyAbuseReComment(reComment);
    }

    @Override
    public void modify19ReComment(ReComment reComment) {
        freeRepository.modify19ReComment(reComment);
    }

    @Override
    public void modifyIncoporateReComment(ReComment reComment) {
        freeRepository.modifyIncoporateReComment(reComment);
    }

    @Override
    public void removeArticle(Article article) {
        if(freeRepository.removeArticle(article)){
            removeFilesAndFolder(article.getWriter(), article.getRegdate());
        }
    }

    @Override
    public void removeArticleShareFile(UploadFile uploadFile) {
        if(freeRepository.removeArticleShareFile(uploadFile)){
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
    public void removeReply(Comment comment) {
        freeRepository.removeReply(comment);
    }
    @Override
    public void removeReComment(ReComment reComment) {
        freeRepository.removeReComment(reComment);
    }

    @Override
    public List<String> removeArticleLike(ArticleLike articleLike) {
        return freeRepository.removeArticleLike(articleLike);
    }

    @Override
    public List<String> removeReplyLike(CommentLike commentLike) {
        return freeRepository.removeReplyLike(commentLike);
    }

    @Override
    public List<String> removeReCommentLike(ReCommentLike reCommentLike) {
        return freeRepository.removeReCommentLike(reCommentLike);
    }
}

