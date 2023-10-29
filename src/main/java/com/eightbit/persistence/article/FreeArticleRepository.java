package com.eightbit.persistence.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeArticleRepository {

    private final SqlSessionTemplate mybatis;

    private final FolderAndFileManger folderAndFileManger;
    public List<Article> getList(){
        return mybatis.selectList("FreeArticleMyBatisDAO.getArticleList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("FreeArticleMyBatisDAO.getUserArticles", writer);
    }


    public Article getArticle(ArticleView articleView){

        if(!articleView.getViewer().isEmpty()){

            Integer userArticleViewCount=mybatis.selectOne("ArticleViewMyBatisDAO.getArticleView", articleView);

            if(userArticleViewCount.intValue()>0){
                mybatis.update("ArticleViewMyBatisDAO.updateArticleView", articleView);
            }

            else if(userArticleViewCount.intValue()==0){
                mybatis.insert("ArticleViewMyBatisDAO.insertArticleView", articleView);
            }
        }

        mybatis.update("FreeArticleMyBatisDAO.updateArticleVisitCnt", articleView);

        return mybatis.selectOne("FreeArticleMyBatisDAO.getArticle", articleView);
    }

    public Integer getTotalCommentCount(Article article){
        return mybatis.selectOne("FreeArticleMyBatisDAO.getTotalCommentCount", article);
    }

    public Article registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
        mybatis.insert("FreeArticleMyBatisDAO.insertArticle", article);
        return  mybatis.selectOne("FreeArticleMyBatisDAO.findWriterAndRegdate",mybatis.selectOne("FreeArticleMyBatisDAO.selectSeqOfWriter", article));
    }

    public void modifyArticle(Article article) {
        mybatis.update("FreeArticleMyBatisDAO.updateArticle", article);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("FreeArticleMyBatisDAO.deleteArticle", article);
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(),"article","free", "sharefiles");
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(), "article", "free", "viewfiles");
        return true;
    }


}
