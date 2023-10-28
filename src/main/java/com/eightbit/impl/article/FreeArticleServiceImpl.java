package com.eightbit.impl.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.persistence.article.FreeArticleRepository;
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
public class FreeArticleServiceImpl implements ArticleService {

    @Value("${file.dir}")
    private String dir;

    private final FreeArticleRepository freeArticleRepository;

    @Override
    public List<Article> getList() {
        return freeArticleRepository.getList();
    }
    @Override
    public List<Article> getUserArticles(String writer) {
        return freeArticleRepository.getUserArticles(writer);
    }

    @Override
    public Article registerArticle(Article article) {
        freeArticleRepository.registerArticle(article);
        return freeArticleRepository.findWriterAndRegdate(freeArticleRepository.findSeqOfWriter(article));
    }

    @Override
    public void modifyArticle(Article article) {
        freeArticleRepository.modifyArticle(article);
    }


    @Override
    public void removeArticle(Article article) {
        if(freeArticleRepository.removeArticle(article)){
            removeArticleFilesAndFolder(article.getWriter(), article.getRegdate());
        }
    }

    @Override
    public UploadFile getViewFile(UploadFile uploadFile) {
        return freeArticleRepository.getViewFile(uploadFile);
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

        Article article = freeArticleRepository.getOriginWriterAndRegdate(new Comment(replyer,regdate));
        String origin_writer= article.getWriter();
        String origin_regdate= article.getRegdate();

        return dir+origin_writer+"/board/article/"+origin_regdate+"/reply/"+replyer+"/"+regdate+"/viewfiles";
    }

    public String setViewReCommentFilePath(String reCommenter, String regdate, String dir){
        Comment comment = freeArticleRepository.getOriginReplyerAndRegdate(new Comment(reCommenter, regdate));
        String origin_replyer= comment.getAuthor();
        String origin_reply_regdate= comment.getRegdate();

        Article article = freeArticleRepository.getOriginWriterAndRegdate(new Comment(origin_replyer,origin_reply_regdate));
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
                freeArticleRepository.registerFile(sharefile, type);
            }
        }
    }

    public List<UploadFile> storeViewFiles(String writer, String regdate, String filepath, List<MultipartFile> files, String type) throws IOException {

        List<UploadFile> viewfiles=new ArrayList<>();

        for (MultipartFile mf : files) {

            if(!(mf.isEmpty())) {
                UploadFile viewfile = storeFile(mf, writer, regdate, filepath);
                viewfile.setId(freeArticleRepository.registerFile(viewfile, type));
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
    public void removeArticleShareFile(UploadFile uploadFile) {
        if(freeArticleRepository.removeArticleShareFile(uploadFile)){
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

    public void removeArticleFilesAndFolder(String writer, String regdate){
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



}

