package com.eightbit.persistence.like.recomment.article;


import com.eightbit.entity.like.Like;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FreeReCommentLikeRepository {

    private final SqlSessionTemplate mybatis;
    public List<String> getReCommentLikes(Like like){
        System.out.println("like" + like);
        return mybatis.selectList("FreeReCommentLikeMyBatisDAO.getReCommentLikes", like);
    }

    public List<String> registerReCommentLike(Like like){
        mybatis.insert("FreeReCommentLikeMyBatisDAO.insertReCommentLike", like);
        return mybatis.selectList("FreeReCommentLikeMyBatisDAO.getReCommentLikes", like);
    }


    public List<String> removeReCommentLike(Like like){
        mybatis.delete("FreeReCommentLikeMyBatisDAO.deleteReCommentLike", like);
        return mybatis.selectList("FreeReCommentLikeMyBatisDAO.getReCommentLikes", like);
    }
}
