package com.eightbit.persistence.file.view.comment.article;

import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeCommentViewFileRepository {

    private final SqlSessionTemplate mybatis;

    public List<UploadFile> getViewFileList(UploadFile uploadFile){
        return mybatis.selectList("FreeCommentViewFileMyBatisDAO.getViewFileList", uploadFile);
    }
    public UploadFile getViewFile(UploadFile uploadFile){
        return mybatis.selectOne("FreeCommentViewFileMyBatisDAO.getViewFile", uploadFile);
    }

    public void registerCommentViewFile(UploadFile file){
        mybatis.insert("FreeCommentViewFileMyBatisDAO.insertArticleViewFile", file);
    }

    public boolean removeCommentViewFile(UploadFile file){
        mybatis.delete("FreeCommentViewFileMyBatisDAO.deleteArticleViewFile", file);
        return true;
    }
}
