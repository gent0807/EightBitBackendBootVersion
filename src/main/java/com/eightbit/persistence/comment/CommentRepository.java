package com.eightbit.persistence.comment;

import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final SqlSessionTemplate mybatis;


    public List<Comment> getComments(Comment comment){
        return mybatis.selectList("CommentMyBatisDAO.getComments", comment);
    }

    public Integer getReCommentCount(Comment comment){
        return mybatis.selectOne("CommentMyBatisDAO.getReCommentCount", comment);
    }

    public Comment getOriginAuthorAndRegdateFromUploadFile(UploadFile uploadFile){
        return mybatis.selectOne("CommentMyBatisDAO.getOriginAuthorAndRegdateFromUploadFile", uploadFile);
    }
    public Comment getOriginAuthorAndRegdateFromComment(Comment comment){
        return mybatis.selectOne("CommentMyBatisDAO.getOriginAuthorAndRegdateFromComment", comment);
    }



    public Comment registerComment(Comment comment){
        mybatis.insert("CommentMyBatisDAO.insertComment", comment);
        mybatis.update("UserMyBatisDAO.updatePointByComment",comment);
        return mybatis.selectOne("CommentMyBatisDAO.getCommentRegdateAndUpdatedate", mybatis.selectOne("CommentMyBatisDAO.getSeqOfComment", comment.getAuthor()));
    }


    public void modifyComment(Comment comment){
        mybatis.update("CommentMyBatisDAO.updateComment", comment);
    }



    public void removeComment(Comment comment){
        mybatis.delete("CommentMyBatisDAO.deleteComment", comment);
    }
}
