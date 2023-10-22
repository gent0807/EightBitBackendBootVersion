package com.eightbit.biz.board.persistence;

import com.eightbit.biz.board.vo.*;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardMyBatisDAO {

    private final SqlSessionTemplate mybatis;

    public List<BoardVO> getList(){
        return mybatis.selectList("BoardMyBatisDAO.getBoardList");
    }

    public List<BoardVO> getUserArticles(String writer){
        return mybatis.selectList("BoardMyBatisDAO.getUserArticles", writer);
    }


    public List<ReplyVO> getReplies(ReplyVO replyVO){
        return mybatis.selectList("BoardMyBatisDAO.getArticleReplies", replyVO);
    }

    public List<ReCommentVO> getReComments(ReCommentVO reCommentVO){
        return mybatis.selectList("BoardMyBatisDAO.getReplyReComments", reCommentVO);
    }

    public BoardVO getArticle(BoardVO boardVO){
        mybatis.update("BoardMyBatisDAO.updateArticleVisitCnt", boardVO);
        return mybatis.selectOne("BoardMyBatisDAO.getArticle", boardVO);
    }
    public List<UploadFile> getAttachList(UploadFile uploadFile){
        return mybatis.selectList("BoardMyBatisDAO.getAttachList", uploadFile);
    }
    public UploadFile getAttachFile(UploadFile uploadFile){
        return mybatis.selectOne("BoardMyBatisDAO.getAttachFile", uploadFile);
    }

    public UploadFile getViewFile(UploadFile uploadFile){
        return mybatis.selectOne("BoardMyBatisDAO.getViewFile", uploadFile);
    }
    public ReplyVO getReply(ReplyVO replyVO){
        return  mybatis.selectOne("BoardMyBatisDAO.getReply", replyVO);
    }
    public ReCommentVO getReComment(ReCommentVO reCommentVO){
        return mybatis.selectOne("BoardMyBatisDAO.getReComment", reCommentVO);
    }
    public List<String> getArticleLikers(BoardVO boardVO){
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", boardVO);
    }
    public List<String> getReplyLikers(ReplyVO replyVO){
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", replyVO);
    }
    public List<String> getReCommentLikers(ReCommentVO reCommentVO){
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reCommentVO);
    }

    public Integer getReCommentCount(BoardVO boardVO){
        return mybatis.selectOne("BoardMyBatisDAO.getReCommentCount", boardVO);
    }
    public void registerArticle(BoardVO boardVO){
        mybatis.insert("BoardMyBatisDAO.insertArticle", boardVO);
    }

    public void registerArticleShareFile(UploadFile sharefile){
        mybatis.insert("BoardMyBatisDAO.insertArticleFileShare", sharefile);
    }
    public ReplyVO registerReply(ReplyVO replyVO){
        mybatis.insert("BoardMyBatisDAO.insertReply", replyVO);
        return mybatis.selectOne("BoardMyBatisDAO.getReplyRegdateAndUpdatedate", mybatis.selectOne("BoardMyBatisDAO.getSeqOfReply",replyVO.getReplyer()));
    }

    public ReCommentVO registerReComment(ReCommentVO reCommentVO){
        mybatis.insert("BoardMyBatisDAO.insertReComment", reCommentVO);
        return mybatis.selectOne("BoardMyBatisDAO.getReCommentRegdateAndUpdatedate", mybatis.selectOne("BoardMyBatisDAO.getSeqOfReComment", reCommentVO.getReCommenter()));
    }

    public void registerArticleView(ArticleViewVO articleViewVO){
        Integer userArticleViewCount=mybatis.selectOne("BoardMyBatisDAO.getArticleView", articleViewVO);
        if(userArticleViewCount.intValue()>0){
            mybatis.update("BoardMyBatisDAO.updateArticleView", articleViewVO);
        }
        else if(userArticleViewCount.intValue()==0){
            mybatis.insert("BoardMyBatisDAO.insertArticleView", articleViewVO);
        }

    }
    public List<String> registerArticleLike(ArticleLikeVO articleLikeVO){
        mybatis.insert("BoardMyBatisDAO.insertArticleLike", articleLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", articleLikeVO);
    }

    public List<String> registerReplyLike(ReplyLikeVO replyLikeVO){
        mybatis.insert("BoardMyBatisDAO.insertReplyLike", replyLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", replyLikeVO);
    }

    public List<String> registerReCommentLike(ReCommentLikeVO reCommentLikeVO){
        mybatis.insert("BoardMyBatisDAO.insertReCommentLike", reCommentLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reCommentLikeVO);
    }

    public void modifyArticle(BoardVO boardVO) {
        mybatis.update("BoardMyBatisDAO.updateArticle", boardVO);
    }

    public void modifyReply(ReplyVO replyVO){
        mybatis.update("BoardMyBatisDAO.updateReply", replyVO);
    }

    public void modifyReComment(ReCommentVO reCommentVO){
        mybatis.update("BoardMyBatisDAO.updateReComment", reCommentVO);
    }

    public void modifyAbuseArticle(BoardVO boardVO){
        mybatis.update("BoardMyBatisDAO.updateAbuseArticle", boardVO);
    }

    public void modify19Article(BoardVO boardVO){
        mybatis.update("BoardMyBatisDAO.update19Article", boardVO);
    }
    public void modifyIncoporateArticle(BoardVO boardVO){
        mybatis.update("BoardMyBatisDAO.updateIncoporateArticle", boardVO);
    }

    public void modifyAbuseReply(ReplyVO replyVO){
        mybatis.update("BoardMyBatisDAO.updateAbuseReply", replyVO);
    }
    public void modify19Reply(ReplyVO replyVO){
        mybatis.update("BoardMyBatisDAO.update19Reply", replyVO);
    }
    public void modifyIncoporateReply(ReplyVO replyVO){
        mybatis.update("BoardMyBatisDAO.updateIncoporateReply", replyVO);
    }

    public void modifyAbuseReComment(ReCommentVO reCommentVO){
        mybatis.update("BoardMyBatisDAO.updateAbuseReComment", reCommentVO);
    }

    public void modify19ReComment(ReCommentVO reCommentVO){
        mybatis.update("BoardMyBatisDAO.update19ReComment", reCommentVO);
    }

    public void modifyIncoporateReComment(ReCommentVO reCommentVO){
        mybatis.update("BoardMyBatisDAO.updateIncoporateReComment", reCommentVO);
    }

    public boolean removeArticle(BoardVO boardVO){
        mybatis.delete("BoardMyBatisDAO.deleteArticle", boardVO);
        return true;
    }
    public boolean removeArticleShareFile(UploadFile shareFile){
        mybatis.delete("BoardMyBatisDAO.deleteArticleShareFile",shareFile);
        return true;
    }

    public void removeReply(ReplyVO replyVO){
        mybatis.delete("BoardMyBatisDAO.deleteReply", replyVO);
    }
    public void removeReComment(ReCommentVO reCommentVO) {
        mybatis.delete("BoardMyBatisDAO.deleteReComment", reCommentVO);
    }

    public List<String> removeArticleLike(ArticleLikeVO articleLikeVO){
        mybatis.delete("BoardMyBatisDAO.deleteArticleLike",articleLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", articleLikeVO);
    }
    public List<String> removeReplyLike(ReplyLikeVO replyLikeVO){
        mybatis.delete("BoardMyBatisDAO.deleteReplyLike", replyLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", replyLikeVO);
    }

    public List<String> removeReCommentLike(ReCommentLikeVO reCommentLikeVO){
        mybatis.delete("BoardMyBatisDAO.deleteReCommentLike", reCommentLikeVO);
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reCommentLikeVO);
    }

    public BoardVO findSeqOfWriter(BoardVO boardVO){
        return mybatis.selectOne("BoardMyBatisDAO.selectSeqOfWriter", boardVO);
    }
    public BoardVO findWriterAndRegdate(BoardVO boardVO){
        return mybatis.selectOne("BoardMyBatisDAO.findWriterAndRegdate", boardVO);
    }

}
