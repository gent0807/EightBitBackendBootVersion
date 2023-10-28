package com.eightbit.persistence.file.view.article;

import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class FreeArticleViewFileRepository {

    private final SqlSessionTemplate mybatis;

    public List<UploadFile> getViewFileList(UploadFile uploadFile){
        return mybatis.selectList("FreeArticleViewFileMyBatisDAO.getViewFileList", uploadFile);
    }
    public UploadFile getViewFile(UploadFile uploadFile){
        return mybatis.selectOne("FreeArticleViewFileMyBatisDAO.getViewFile", uploadFile);
    }

    public void registerArticleViewFile(UploadFile file){
        mybatis.insert("FreeArticleViewFileMyBatisDAO.insertArticleViewFile", file);
    }

    public boolean removeArticleViewFile(UploadFile file){
        mybatis.delete("FreeArticleViewFileMyBatisDAO.deleteArticleViewFile", file);
        return true;
    }
}
