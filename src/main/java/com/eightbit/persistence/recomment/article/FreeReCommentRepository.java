package com.eightbit.persistence.recomment.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.persistence.comment.article.FreeCommentRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeReCommentRepository {

    private final SqlSessionTemplate mybatis;

    private final FreeCommentRepository freeCommentRepository;

    private FolderAndFileManger folderAndFileManger;
    public List<Comment> getReComments(Comment reComment){
        return mybatis.selectList("FreeReCommentMyBatisDAO.getReComments", reComment);
    }

    public Integer getReCommentCount(Article article){
        return mybatis.selectOne("FreeReCommentMyBatisDAO.getReCommentCount", article);
    }

    public Comment getOriginCommenterAndRegdate(UploadFile uploadFile){
        return mybatis.selectOne("FreeReCommentMyBatisDAO.getOriginCommenterAndRegdate", uploadFile);
    }


    public Comment registerReComment(Comment reComment){
        mybatis.insert("FreeReCommentMyBatisDAO.insertReComment", reComment);
        mybatis.update("UserMyBatisDAO.updatePointByComment", reComment);
        return mybatis.selectOne("FreeReCommentMyBatisDAO.getReCommentRegdateAndUpdatedate", mybatis.selectOne("FreeReCommentMyBatisDAO.getSeqOfReComment", reComment.getAuthor()));
    }


    public void modifyReComment(Comment reComment){
        mybatis.update("FreeReCommentMyBatisDAO.updateReComment", reComment);
    }


    public void removeReComment(Comment reComment) {
        Comment comment=freeCommentRepository.getOriginWriterAndRegdate(new UploadFile(reComment.getOriginal_author(), reComment.getOriginal_regdate()));
        mybatis.delete("FreeReCommentMyBatisDAO.deleteReComment", reComment);
        folderAndFileManger.removeReCommentFilesAndFolder(reComment.getAuthor(), reComment.getRegdate(), reComment.getOriginal_author(), reComment.getOriginal_regdate(), comment.getOriginal_author(), comment.getOriginal_regdate(), "article","free", "viewfiles");
    }

}
