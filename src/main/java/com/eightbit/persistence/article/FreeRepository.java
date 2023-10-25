package com.eightbit.persistence.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.like.ArticleLike;
import com.eightbit.entity.like.ReCommentLike;
import com.eightbit.entity.like.CommentLike;
import com.eightbit.entity.recomment.ReComment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FreeRepository {

    private final SqlSessionTemplate mybatis;

    public List<Article> getList(){
        return mybatis.selectList("BoardMyBatisDAO.getBoardList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("BoardMyBatisDAO.getUserArticles", writer);
    }

    public Article getOriginWriterAndRegdate(Comment comment){
        return mybatis.selectOne("BoardMyBatisDAO.getOriginWriterAndRegdate", comment);
    }

    public Comment getOriginReplyerAndRegdate(ReComment reComment){
        return mybatis.selectOne("BoardMyBatisDAO.getOriginReplyerAndRegdate", reComment);
    }


    public List<Comment> getReplies(Comment comment){
        return mybatis.selectList("BoardMyBatisDAO.getArticleReplies", comment);
    }

    public List<ReComment> getReComments(ReComment reComment){
        return mybatis.selectList("BoardMyBatisDAO.getReplyReComments", reComment);
    }

    public Article getArticle(Article article){
        mybatis.update("BoardMyBatisDAO.updateArticleVisitCnt", article);
        return mybatis.selectOne("BoardMyBatisDAO.getArticle", article);
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
    public Comment getReply(Comment comment){
        return  mybatis.selectOne("BoardMyBatisDAO.getReply", comment);
    }
    public ReComment getReComment(ReComment reComment){
        return mybatis.selectOne("BoardMyBatisDAO.getReComment", reComment);
    }
    public List<String> getArticleLikers(Article article){
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", article);
    }
    public List<String> getReplyLikers(Comment comment){
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", comment);
    }
    public List<String> getReCommentLikers(ReComment reComment){
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reComment);
    }

    public Integer getReCommentCount(Article article){
        return mybatis.selectOne("BoardMyBatisDAO.getReCommentCount", article);
    }
    public void registerArticle(Article article){
        mybatis.insert("BoardMyBatisDAO.insertArticle", article);
    }


    public Integer registerFile(UploadFile file, String type){
        if(type.equals("article_share")){
            mybatis.insert("BoardMyBatisDAO.insertArticleShareFile", file);
            return null;
        }
        else if(type.equals("article_view")){
            mybatis.insert("BoardMyBatisDAO.insertArticleViewFile", file);
            return mybatis.selectOne("BoardMyBatisDAO.getArticleViewFileId",file);
        }
        else if(type.equals("reply_view")){
            mybatis.insert("BoardMyBatisDAO.insertReplyViewFile", file);
            return mybatis.selectOne("BoardMyBatisDAO.getReplyViewFileId",file);
        }
        else if(type.equals("reComment_view")){
            mybatis.insert("BoardMyBatisDAO.insertReCommentViewFile", file);
            return mybatis.selectOne("BoardMyBatisDAO.getReCommentViewFileId",file);
        }
        return null;
    }

    public Comment registerReply(Comment comment){
        mybatis.insert("BoardMyBatisDAO.insertReply", comment);
        return mybatis.selectOne("BoardMyBatisDAO.getReplyRegdateAndUpdatedate", mybatis.selectOne("BoardMyBatisDAO.getSeqOfReply", comment.getReplyer()));
    }

    public ReComment registerReComment(ReComment reComment){
        mybatis.insert("BoardMyBatisDAO.insertReComment", reComment);
        return mybatis.selectOne("BoardMyBatisDAO.getReCommentRegdateAndUpdatedate", mybatis.selectOne("BoardMyBatisDAO.getSeqOfReComment", reComment.getReCommenter()));
    }

    public void registerArticleView(ArticleView articleView){
        Integer userArticleViewCount=mybatis.selectOne("BoardMyBatisDAO.getArticleView", articleView);
        if(userArticleViewCount.intValue()>0){
            mybatis.update("BoardMyBatisDAO.updateArticleView", articleView);
        }
        else if(userArticleViewCount.intValue()==0){
            mybatis.insert("BoardMyBatisDAO.insertArticleView", articleView);
        }

    }
    public List<String> registerArticleLike(ArticleLike articleLike){
        mybatis.insert("BoardMyBatisDAO.insertArticleLike", articleLike);
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", articleLike);
    }

    public List<String> registerReplyLike(CommentLike commentLike){
        mybatis.insert("BoardMyBatisDAO.insertReplyLike", commentLike);
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", commentLike);
    }

    public List<String> registerReCommentLike(ReCommentLike reCommentLike){
        mybatis.insert("BoardMyBatisDAO.insertReCommentLike", reCommentLike);
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reCommentLike);
    }

    public void modifyArticle(Article article) {
        mybatis.update("BoardMyBatisDAO.updateArticle", article);
    }

    public void modifyReply(Comment comment){
        mybatis.update("BoardMyBatisDAO.updateReply", comment);
    }

    public void modifyReComment(ReComment reComment){
        mybatis.update("BoardMyBatisDAO.updateReComment", reComment);
    }

    public void modifyAbuseArticle(Article article){
        mybatis.update("BoardMyBatisDAO.updateAbuseArticle", article);
    }

    public void modify19Article(Article article){
        mybatis.update("BoardMyBatisDAO.update19Article", article);
    }
    public void modifyIncoporateArticle(Article article){
        mybatis.update("BoardMyBatisDAO.updateIncoporateArticle", article);
    }

    public void modifyAbuseReply(Comment comment){
        mybatis.update("BoardMyBatisDAO.updateAbuseReply", comment);
    }
    public void modify19Reply(Comment comment){
        mybatis.update("BoardMyBatisDAO.update19Reply", comment);
    }
    public void modifyIncoporateReply(Comment comment){
        mybatis.update("BoardMyBatisDAO.updateIncoporateReply", comment);
    }

    public void modifyAbuseReComment(ReComment reComment){
        mybatis.update("BoardMyBatisDAO.updateAbuseReComment", reComment);
    }

    public void modify19ReComment(ReComment reComment){
        mybatis.update("BoardMyBatisDAO.update19ReComment", reComment);
    }

    public void modifyIncoporateReComment(ReComment reComment){
        mybatis.update("BoardMyBatisDAO.updateIncoporateReComment", reComment);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("BoardMyBatisDAO.deleteArticle", article);
        return true;
    }
    public boolean removeArticleShareFile(UploadFile shareFile){
        mybatis.delete("BoardMyBatisDAO.deleteArticleShareFile",shareFile);
        return true;
    }

    public void removeReply(Comment comment){
        mybatis.delete("BoardMyBatisDAO.deleteReply", comment);
    }
    public void removeReComment(ReComment reComment) {
        mybatis.delete("BoardMyBatisDAO.deleteReComment", reComment);
    }

    public List<String> removeArticleLike(ArticleLike articleLike){
        mybatis.delete("BoardMyBatisDAO.deleteArticleLike", articleLike);
        return mybatis.selectList("BoardMyBatisDAO.getArticleLikers", articleLike);
    }
    public List<String> removeReplyLike(CommentLike commentLike){
        mybatis.delete("BoardMyBatisDAO.deleteReplyLike", commentLike);
        return mybatis.selectList("BoardMyBatisDAO.getReplyLikers", commentLike);
    }

    public List<String> removeReCommentLike(ReCommentLike reCommentLike){
        mybatis.delete("BoardMyBatisDAO.deleteReCommentLike", reCommentLike);
        return mybatis.selectList("BoardMyBatisDAO.getReCommentLikers", reCommentLike);
    }

    public Article findSeqOfWriter(Article article){
        return mybatis.selectOne("BoardMyBatisDAO.selectSeqOfWriter", article);
    }
    public Article findWriterAndRegdate(Article article){
        return mybatis.selectOne("BoardMyBatisDAO.findWriterAndRegdate", article);
    }

}
