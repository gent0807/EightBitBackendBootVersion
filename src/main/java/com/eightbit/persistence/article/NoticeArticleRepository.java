package com.eightbit.persistence.article;


import com.eightbit.entity.article.Article;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeArticleRepository {

    private final SqlSessionTemplate mybatis;

    private final FolderAndFileManger folderAndFileManger;

    public List<Article> getList(){
        return mybatis.selectList("NoticeArticleMyBatisDAO.getFreeArticleList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("NoticeArticleMyBatisDAO.getUserArticles", writer);
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

        mybatis.update("NoticeArticleMyBatisDAO.updateArticleVisitCnt", articleView);

        return mybatis.selectOne("NoticeArticleMyBatisDAO.getArticle", articleView);
    }

    public Article registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
        mybatis.insert("NoticeArticleMyBatisDAO.insertArticle", article);
        return  mybatis.selectOne("NoticeArticleMyBatisDAO.findWriterAndRegdate",mybatis.selectOne("NoticeArticleMyBatisDAO.selectSeqOfWriter", article));
    }

    public void modifyArticle(Article article) {
        mybatis.update("NoticeArticleMyBatisDAO.updateArticle", article);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("NoticeArticleMyBatisDAO.deleteArticle", article);
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(),"article","free", "sharefiles");
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(), "article", "free", "viewfiles");
        return true;
    }
}
