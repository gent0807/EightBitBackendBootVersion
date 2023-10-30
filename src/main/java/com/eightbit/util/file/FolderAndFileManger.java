package com.eightbit.util.file;


import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.persistence.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:upload.properties")
public class FolderAndFileManger {
    CommentRepository commentRepository;


    @Value("${file.dir}")
    private String dir;


    public String setFilePath(String uploader, String regdate, String contentType, String storeType, int depth) {
        if(depth==1){
            return dir+uploader+"/"+contentType+"/"+regdate+"/"+storeType;
        }
        else if(depth==2){
            Comment comment=commentRepository.getOriginAuthorAndRegdateFromUploadFile(new UploadFile(uploader, regdate, contentType,depth));
            return dir+comment.getOriginal_author()+"/"+contentType+"/"+comment.getOriginal_regdate()+"/comment/"+uploader+"/"+regdate+"/"+storeType;
        }
        if(depth==3){
            Comment comment=commentRepository.getOriginAuthorAndRegdateFromUploadFile(new UploadFile(uploader, regdate, contentType,depth));
            String original_commenter=comment.getOriginal_author();
            String original_comment_regdate=comment.getOriginal_regdate();
            comment=commentRepository.getOriginAuthorAndRegdateFromComment(comment);
            return dir+comment.getOriginal_author()+
                    "/"+contentType+"/"+comment.getOriginal_regdate()+"/comment/"+original_commenter+"/"+original_comment_regdate+"/reComment/"
                    +uploader+"/"+regdate+"/"+storeType;
        }
        return null;
    }



    public boolean createDir(File folder){
        if(!folder.exists()){
            return folder.mkdirs();
        }
        return false;
    }


    public UploadFile storeFile(MultipartFile mf, String uploader, String regdate, String contentType, String storeType, int depth) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setFilePath(uploader, storeRegdate, contentType, storeType, depth);
        createDir(new File(filepath));

        System.out.println("mf :"+mf);
        String originFilename = mf.getOriginalFilename();
        System.out.println("originFilename:"+originFilename);
        String storeFilename = createStoreFilename(originFilename);
        System.out.println("storeFilename:"+storeFilename);
        File f = new File(filepath + "/"+storeFilename);

        mf.transferTo(f);

        return new UploadFile(uploader, regdate, storeFilename, originFilename);
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

    public void removeFile(UploadFile uploadFile){
        String storeRegdate = uploadFile.getRegdate().replace(":", "");
        String filePath=setFilePath(uploadFile.getUploader(), storeRegdate, uploadFile.getContentType(), uploadFile.getStoreType(), uploadFile.getDepth());

        File folder = new File(filePath);

        if (folder.exists()) {
            File targetfile = new File(filePath + "/" + uploadFile.getStoreFilename());

            if (targetfile.isFile()) {
                targetfile.delete();
            }

            File[] list = folder.listFiles();

            if (list.length == 0) {
                folder.delete();
            }
        }
    }



    public void removeUserFilesAndFolder(String nickname){
        String filepath=dir+nickname;

        File folder = new File(filepath);

        if (folder.exists()) {
            File[] folder_list = folder.listFiles();

            for (int j = 0; j < folder_list.length; j++) {
                folder_list[j].delete(); //파일 삭제
            }

            folder.delete();
        }
    }

    public static void deleteFolder(String path) {

        File folder = new File(path);
        try {
            if(folder.exists()){
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int i = 0; i < folder_list.length; i++) {
                    if(folder_list[i].isFile()) {
                        folder_list[i].delete();

                    }else {
                        deleteFolder(folder_list[i].getPath()); //재귀함수호출

                    }
                    folder_list[i].delete();
                }
                folder.delete(); //폴더 삭제
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

    }


    public void removeFilesAndFolder(String uploader, String regdate, String contentType, int depth){
        regdate=regdate.replace(":","");
        String filePath=setFilePath(uploader, regdate, contentType, null, depth);
        deleteFolder(filePath);
    }





}
