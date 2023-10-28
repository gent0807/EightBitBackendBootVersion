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
        return mybatis.selectList("BoardMyBatisDAO.getFreeArticleList");
    }

    public List<Article> getUserArticles(String writer){
        return mybatis.selectList("BoardMyBatisDAO.getUserArticles", writer);
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

    public Article registerArticle(Article article){
        mybatis.update("UserMyBatisDAO.updatePointByArticle", article);
        mybatis.insert("BoardMyBatisDAO.insertArticle", article);
        return  mybatis.selectOne("BoardMyBatisDAO.findWriterAndRegdate",mybatis.selectOne("BoardMyBatisDAO.selectSeqOfWriter", article));
    }

    public void modifyArticle(Article article) {
        mybatis.update("BoardMyBatisDAO.updateArticle", article);
    }

    public boolean removeArticle(Article article){
        mybatis.delete("BoardMyBatisDAO.deleteArticle", article);
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(),"article","free", "sharefiles");
        folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(), "article", "free", "viewfiles");
        return true;
    }


}
