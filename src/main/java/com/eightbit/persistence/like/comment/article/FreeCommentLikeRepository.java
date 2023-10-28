package com.eightbit.persistence.like.comment.article;


import com.eightbit.entity.like.Like;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FreeCommentLikeRepository {

    private final SqlSessionTemplate mybatis;


    public List<String> getReplyLikes(Like like){
        return  mybatis.selectList("FreeCommentLikeMyBatisDAO.getReplyLikes", like);
    }

    public List<String> registerReplyLike(Like like){
        mybatis.insert("FreeCommentLikeMyBatisDAO.insertReplyLike", like);
        return mybatis.selectList("FreeCommentLikeMyBatisDAO.getReplyLikes", like);
    }


    public List<String> removeReplyLike(Like like){
        mybatis.delete("FreeCommentLikeMyBatisDAO.deleteReplyLike", like);
        return mybatis.selectList("FreeCommentLikeMyBatisDAO.getReplyLikes", like);
    }
}
