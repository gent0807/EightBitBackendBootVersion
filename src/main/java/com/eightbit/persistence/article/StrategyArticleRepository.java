package com.eightbit.persistence.article;


import com.eightbit.entity.article.Article;
import com.eightbit.entity.view.ArticleView;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StrategyArticleRepository {
    private final SqlSessionTemplate mybatis;

    public List<Article> getList(){
        return mybatis.selectList("StrategyArticleMyBatisDAO.getArticleList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("StrategyArticleMyBatisDAO.getUserArticles", writer);
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

        mybatis.update("StrategyArticleMyBatisDAO.updateArticleVisitCnt", articleView);

        return mybatis.selectOne("StrategyArticleMyBatisDAO.getArticle", articleView);
    }


    public Integer getTotalCommentCount(Article article){
        return mybatis.selectOne("StrategyArticleMyBatisDAO.getTotalCommentCount", article);
    }

    public Article registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
        mybatis.insert("StrategyArticleMyBatisDAO.insertArticle", article);
        return  mybatis.selectOne("StrategyArticleMyBatisDAO.findWriterAndRegdate",mybatis.selectOne("StrategyArticleMyBatisDAO.selectSeqOfWriter", article));
    }


    public void modifyArticle(Article article) {
        mybatis.update("StrategyArticleMyBatisDAO.updateArticle", article);
    }


    public boolean removeArticle(Article article){
        mybatis.delete("StrategyArticleMyBatisDAO.deleteArticle", article);
        return true;
    }




}
