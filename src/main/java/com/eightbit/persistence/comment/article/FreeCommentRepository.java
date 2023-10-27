package com.eightbit.persistence.comment.article;

import com.eightbit.entity.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeCommentRepository {

    private final SqlSessionTemplate mybatis;
    public List<Comment> getReplies(Comment comment){
        return mybatis.selectList("FreeCommentMyBatisDAO.getArticleReplies", comment);
    }


    public Comment registerReply(Comment comment){
        mybatis.insert("FreeCommentMyBatisDAO.insertReply", comment);
        mybatis.update("UserMyBatisDAO.updatePointByComment",comment);
        return mybatis.selectOne("FreeCommentMyBatisDAO.getReplyRegdateAndUpdatedate", mybatis.selectOne("FreeCommentMyBatisDAO.getSeqOfReply", comment.getAuthor()));
    }


    public void modifyReply(Comment comment){
        mybatis.update("FreeCommentMyBatisDAO.updateReply", comment);
    }



    public void removeReply(Comment comment){
        mybatis.delete("FreeCommentMyBatisDAO.deleteReply", comment);
    }
}
