package com.eightbit.persistence.like.article;

import com.eightbit.entity.like.Like;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FreeArticleLikeRepository {

    private final SqlSessionTemplate mybatis;

    public List<String> getArticleLikers(Like like){
        return mybatis.selectList("FreeArticleLikeMyBatisDAO.getArticleLikers", like);
    }



    public List<String> registerArticleLike(Like like){
        mybatis.insert("FreeArticleLikeMyBatisDAO.insertArticleLike", like);
        return mybatis.selectList("FreeArticleLikeMyBatisDAO.getArticleLikers", like);
    }



    public List<String> removeArticleLike(Like like){
        mybatis.delete("FreeArticleLikeMyBatisDAO.deleteArticleLike", like);
        return mybatis.selectList("FreeArticleLikeMyBatisDAO.getArticleLikers", like);
    }

}
