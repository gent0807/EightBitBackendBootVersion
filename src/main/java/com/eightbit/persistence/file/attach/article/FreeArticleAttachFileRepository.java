package com.eightbit.persistence.file.attach.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class FreeArticleAttachFileRepository {

    private final SqlSessionTemplate mybatis;
    public List<UploadFile> getAttachFileList(UploadFile uploadFile){
        return mybatis.selectList("FreeArticleAttachFileMyBatisDAO.getAttachFileList", uploadFile);
    }
    public UploadFile getAttachFile(UploadFile uploadFile){
        return mybatis.selectOne("FreeArticleAttachFileMyBatisDAO.getAttachFile", uploadFile);
    }

    public void registerArticleShareFile(UploadFile file){
        mybatis.insert("FreeArticleAttachFileMyBatisDAO.insertArticleShareFile", file);
    }

    public boolean removeArticleShareFile(UploadFile file){
        mybatis.delete("FreeArticleAttachFileMyBatisDAO.deleteArticleShareFile", file);
        return true;
    }
}
