package com.eightbit.impl.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.persistence.article.FreeArticleRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Primary
@PropertySource("classpath:upload.properties")
public class FreeArticleServiceImpl implements ArticleService {

    private final FreeArticleRepository freeArticleRepository;

    private final FolderAndFileManger folderAndFileManger;

    @Override
    public List<Article> getList() {
        return freeArticleRepository.getList();
    }
    @Override
    public List<Article> getUserArticles(String writer) {
        return freeArticleRepository.getUserArticles(writer);
    }

    @Override
    public Article registerArticle(Article article) {
        freeArticleRepository.registerArticle(article);
        return freeArticleRepository.findWriterAndRegdate(freeArticleRepository.findSeqOfWriter(article));
    }

    @Override
    public void modifyArticle(Article article) {
        freeArticleRepository.modifyArticle(article);
    }


    @Override
    public void removeArticle(Article article) {
        if(freeArticleRepository.removeArticle(article)){
            folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(),"article","free", "sharefiles");
            folderAndFileManger.removeBoardFilesAndFolder(article.getWriter(), article.getRegdate(), "article", "free", "viewfiles");
        }
    }

}

