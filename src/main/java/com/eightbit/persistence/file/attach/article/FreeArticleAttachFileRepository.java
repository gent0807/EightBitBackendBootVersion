package com.eightbit.persistence.file.attach.article;

import com.eightbit.entity.uploadfile.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class FreeArticleAttachFileRepository {

    private final SqlSessionTemplate mybatis;
    public List<UploadFile> getAttachList(UploadFile uploadFile){
        return mybatis.selectList("BoardMyBatisDAO.getAttachList", uploadFile);
    }
    public UploadFile getAttachFile(UploadFile uploadFile){
        return mybatis.selectOne("BoardMyBatisDAO.getAttachFile", uploadFile);
    }
}
