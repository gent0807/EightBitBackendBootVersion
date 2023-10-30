package com.eightbit.persistence.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.read.Read;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {
    private final SqlSessionTemplate mybatis;


    public List<Article> getArticleList(String contentType){
        return mybatis.selectList("ArticleMyBatisDAO.getArticleList", contentType);
    }

    public List<Article> getUserArticles(Article article){
        return mybatis.selectList("ArticleMyBatisDAO.getUserArticles", article);
    }


    public Article getArticle(Read read){

        if(!read.getReader().isEmpty()){

            Integer userArticleViewCount=mybatis.selectOne("ReadMyBatisDAO.getView", read);

            if(userArticleViewCount.intValue()>0){
                mybatis.update("ReadMyBatisDAO.updateView", read);
            }

            else if(userArticleViewCount.intValue()==0){
                mybatis.insert("ReadMyBatisDAO.insertView", read);
            }
        }

        mybatis.update("ArticleMyBatisDAO.updateArticleVisitCnt", read);

        return mybatis.selectOne("ArticleMyBatisDAO.getArticle", read);
    }

    public Article registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
        mybatis.insert("ArticleMyBatisDAO.insertArticle", article);
        return  mybatis.selectOne("ArticleMyBatisDAO.findWriterAndRegdate",mybatis.selectOne("ArticleMyBatisDAO.selectSeqOfWriter", article));
    }

    public void modifyArticle(Article article) {
        mybatis.update("ArticleMyBatisDAO.updateArticle", article);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("ArticleMyBatisDAO.deleteArticle", article);
        return true;
    }
}
