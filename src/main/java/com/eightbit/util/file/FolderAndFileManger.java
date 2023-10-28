package com.eightbit.util.file;


import com.eightbit.entity.file.UploadFile;
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


    @Value("${file.dir}")
    private String dir;

    public String setBoardFilePath(String writer, String regdate, String dir, String boardType, String conetentType, String fileType) {
        return dir+writer + "/"+boardType+"/"+conetentType+"/"+ regdate +"/"+fileType;
    }

    public boolean createDir(File folder){
        if(!folder.exists()){
            return folder.mkdirs();
        }
        return false;
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



    public UploadFile storeBoardFile(MultipartFile mf, String writer, String regdate, String dir, String boardType, String contentType, String fileType) throws IOException {
        String storeRegdate=regdate.replace(":","");
        String filepath=setBoardFilePath(writer, storeRegdate, dir, boardType, contentType, fileType);
        createDir(new File(filepath));

        System.out.println("mf :"+mf);
        String originFilename = mf.getOriginalFilename();
        System.out.println("originFilename:"+originFilename);
        String storeFilename = createStoreFilename(originFilename);
        System.out.println("storeFilename:"+storeFilename);
        File f = new File(filepath + "/"+storeFilename);

        mf.transferTo(f);

        return new UploadFile(writer, regdate, storeFilename, originFilename);
    }

    public void removeBoardFile(UploadFile uploadFile, String boardType, String contentType, String fileType){

        String regdate = uploadFile.getRegdate().replace(":", "");
        String filepath = dir + uploadFile.getUploader() + "/"+boardType+"/"+contentType+"/"+regdate +"/"+fileType ;

        File folder = new File(filepath);

        if (folder.exists()) {
            File targetfile = new File(filepath + "/" + uploadFile.getStoreFilename());

            if (targetfile.isFile()) {
                targetfile.delete();
            }

            File[] list = folder.listFiles();

            if (list.length == 0) {
                File parentFoler = new File(dir + uploadFile.getUploader() + "/"+boardType+"/"+contentType+"/"+ regdate);
                folder.delete();
                parentFoler.delete();
            }
        }
    }


    public void removeBoardFilesAndFolder(String writer, String regdate, String boardType, String contentType, String fileType){
        regdate=regdate.replace(":","");
        String filepath1=dir+writer+"/"+boardType+"/"+contentType+"/"+regdate+"/"+fileType;
        String filepath2=dir+writer+"/"+boardType+"/"+contentType+"/"+regdate;
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
