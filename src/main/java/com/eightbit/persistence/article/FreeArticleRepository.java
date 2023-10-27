package com.eightbit.persistence.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeArticleRepository {

    private final SqlSessionTemplate mybatis;

    public List<Article> getList(){
        return mybatis.selectList("BoardMyBatisDAO.getFreeArticleList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("BoardMyBatisDAO.getUserArticles", writer);
    }

    public Article getOriginWriterAndRegdate(Comment comment){
        return mybatis.selectOne("BoardMyBatisDAO.getOriginWriterAndRegdate", comment);
    }

    public Comment getOriginReplyerAndRegdate(Comment reComment){
        return mybatis.selectOne("BoardMyBatisDAO.getOriginReplyerAndRegdate", reComment);
    }


    public Article getArticle(ArticleView articleView){
        if(articleView.getViewer()!=null){

            Integer userArticleViewCount=mybatis.selectOne("ArticleViewMyBatisDAO.getArticleView", articleView);

            if(userArticleViewCount.intValue()>0){
                mybatis.update("ArticleViewMyBatisDAO.updateArticleView", articleView);
            }

            else if(userArticleViewCount.intValue()==0){
                mybatis.insert("ArticleViewMyBatisDAO.insertArticleView", articleView);
            }
        }

        mybatis.update("BoardMyBatisDAO.updateArticleVisitCnt", articleView);

        return mybatis.selectOne("BoardMyBatisDAO.getArticle", articleView);
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


    public void registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
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
    public void modifyArticle(Article article) {
        mybatis.update("BoardMyBatisDAO.updateArticle", article);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("BoardMyBatisDAO.deleteArticle", article);
        return true;
    }
    public boolean removeArticleShareFile(UploadFile shareFile){
        mybatis.delete("BoardMyBatisDAO.deleteArticleShareFile",shareFile);
        return true;
    }


    public Article findSeqOfWriter(Article article){
        return mybatis.selectOne("BoardMyBatisDAO.selectSeqOfWriter", article);
    }
    public Article findWriterAndRegdate(Article article){
        return mybatis.selectOne("BoardMyBatisDAO.findWriterAndRegdate", article);
    }

}
