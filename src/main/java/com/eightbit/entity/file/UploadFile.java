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

    public UploadFile(String writer, String regdate, String storeFilename, String originFilename) {
        this.uploader=writer;
        this.regdate=regdate;
        this.storeFilename=storeFilename;
        this.uploadFilename=originFilename;
    }
}
