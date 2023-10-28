package com.eightbit.entity.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile {
    private int id;
    private String uploader;
    private String regdate;
    private String storeFilename;
    private String uploadFilename;

    public UploadFile(String uploader, String regdate){
        this.uploader=uploader;
        this.regdate=regdate;
    }

    public UploadFile(String uploader, String regdate, String storeFilename, String originFilename) {
        this.uploader=uploader;
        this.regdate=regdate;
        this.storeFilename=storeFilename;
        this.uploadFilename=originFilename;
    }
}
