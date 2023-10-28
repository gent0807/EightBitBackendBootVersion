package com.eightbit.persistence.file.view.recomment.article;

import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeReCommentViewFileRepository {
    private final SqlSessionTemplate mybatis;

    public List<UploadFile> getViewFileList(UploadFile uploadFile){
        return mybatis.selectList("FreeReCommentViewFileMyBatisDAO.getViewFileList", uploadFile);
    }
    public UploadFile getViewFile(UploadFile uploadFile){
        return mybatis.selectOne("FreeReCommentViewFileMyBatisDAO.getViewFile", uploadFile);
    }

    public void registerReCommentViewFile(UploadFile file){
        mybatis.insert("FreeReCommentViewFileMyBatisDAO.insertArticleViewFile", file);
    }

    public boolean removeReCommentViewFile(UploadFile file){
        mybatis.delete("FreeReCommentViewFileMyBatisDAO.deleteArticleViewFile", file);
        return true;
    }
}
