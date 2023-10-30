package com.eightbit.entity.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    private int id;
    private String uploader;
    private String regdate;
    private String storeFilename;
    private String uploadFilename;
    private int depth;
    private String contentType;
    private String storeType;

    public UploadFile(String uploader, String regdate, String contentType, int depth){
        this.uploader=uploader;
        this.regdate=regdate;
        this.contentType=contentType;
        this.depth=depth;
    }

    public UploadFile(String uploader, String regdate, String storeFilename, String originFilename) {
        this.uploader=uploader;
        this.regdate=regdate;
        this.storeFilename=storeFilename;
        this.uploadFilename=originFilename;
    }
}
