package com.eightbit.persistence.like;

import com.eightbit.entity.like.Like;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LikeRepository {

    private final SqlSessionTemplate mybatis;

    public List<String> getLikers(Like like){
        return mybatis.selectList("LikeMyBatisDAO.getLikers", like);
    }



    public List<String> registerLike(Like like){
        mybatis.insert("LikeMyBatisDAO.insertLike", like);
        return mybatis.selectList("LikeMyBatisDAO.getLikers", like);
    }



    public List<String> removeLike(Like like){
        mybatis.delete("LikeMyBatisDAO.deleteLike", like);
        return mybatis.selectList("LikeMyBatisDAO.getLikers", like);
    }

}
