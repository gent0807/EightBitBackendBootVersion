package com.eightbit.persistence.file;

import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final SqlSessionTemplate mybatis;
    public List<UploadFile> getFileList(UploadFile uploadFile){
        return mybatis.selectList("FileMyBatisDAO.getFileList", uploadFile);
    }
    public UploadFile getFile(UploadFile uploadFile){
        return mybatis.selectOne("FileMyBatisDAO.getFile", uploadFile);
    }

    public void registerFile(UploadFile uploadFile){
        mybatis.insert("FileMyBatisDAO.insertFile", uploadFile);
    }

    public boolean removeFile(UploadFile uploadFile){
        mybatis.delete("FileMyBatisDAO.deleteFile", uploadFile);
        return true;
    }
}
