package com.eightbit.persistence.file.attach.article;

import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StrategyArticleAttachFileRepository {
    private final SqlSessionTemplate mybatis;
    public List<UploadFile> getAttachFileList(UploadFile uploadFile){
        return mybatis.selectList("StrategyArticleAttachFileMyBatisDAO.getAttachFileList", uploadFile);
    }
    public UploadFile getAttachFile(UploadFile uploadFile){
        return mybatis.selectOne("StrategyArticleAttachFileMyBatisDAO.getAttachFile", uploadFile);
    }

    public void registerArticleShareFile(UploadFile file){
        mybatis.insert("StrategyArticleAttachFileMyBatisDAO.insertArticleShareFile", file);
    }

    public boolean removeArticleShareFile(UploadFile file){
        mybatis.delete("StrategyArticleAttachFileMyBatisDAO.deleteArticleShareFile", file);
        return true;
    }
}
