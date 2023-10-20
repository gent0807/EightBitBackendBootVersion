package com.eightbit.biz.board.vo;

import lombok.Data;

@Data
public class UploadFile {
    private int id;
    private String uploader;
    private String regdate;
    private String storeFilename;
    private String uploadFilename;

    public UploadFile(String writer, String regdate, String storeFileName, String originFilename) {
        this.uploader=writer;
        this.regdate=regdate;
        this.storeFilename=storeFileName;
        this.uploadFilename=originFilename;
    }
}
