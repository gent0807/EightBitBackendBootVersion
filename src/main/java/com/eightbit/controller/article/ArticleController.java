package com.eightbit.controller.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.read.Read;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.article.ArticleRepository;
import com.eightbit.util.file.FolderAndFileManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Articles/*")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class ArticleController {
    private final ArticleRepository articleRepository;

    private final TokenManager tokenManager;

    private final FolderAndFileManger folderAndFileManger;

    @GetMapping(value = "/articles")
    public ResponseEntity<List<Article>> getArticleList(@RequestParam String contentType){
        return ResponseEntity.ok().body(articleRepository.getArticleList(contentType));
    }


    @GetMapping(value="/article")
    public ResponseEntity<Article> getArticle(@RequestParam String viewer, @RequestParam String writer, @RequestParam String regdate,
                                              @RequestParam String contentType, Read read){
        read.setReader(viewer);
        read.setMaster(writer);
        read.setRegdate(regdate);
        read.setContentType(contentType);
        return ResponseEntity.ok().body(articleRepository.getArticle(read));
    }

    @GetMapping(value="/user/articles")
    public ResponseEntity<List<Article>> getUserArticles(@RequestParam String writer, @RequestParam String contentType, Article article){
        article.setWriter(writer);
        article.setContentType(contentType);
        return ResponseEntity.ok().body(articleRepository.getUserArticles(article));
    }


    @PostMapping(value = "/article")
    @Transactional
    public ResponseEntity<Article> insertArticle(HttpServletRequest request, String token, @RequestBody Article article){

        if(tokenManager.checkAccessToken(request, token, article.getWriter())){
            return ResponseEntity.ok().body(articleRepository.registerArticle(article));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/article")
    @Transactional
    public void updateArticle(HttpServletRequest request, String token, @RequestParam("writer") String writer, @RequestParam("regdate") String regdate, @RequestBody Article article){
        if(tokenManager.checkAccessToken(request,token,writer)){
            article.setWriter(writer);
            article.setRegdate(regdate);
            articleRepository.modifyArticle(article);
        }
    }

    @DeleteMapping(value = "/article/{writer}/{regdate}/{role}/{contentType}")
    @Transactional
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("writer") String writer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, @PathVariable("contentType") String contentType, Article article){
        if(tokenManager.checkAccessToken(request,  token, writer)|| role.equals("ADMIN")){
            article.setWriter(writer);
            article.setRegdate(regdate);
            article.setContentType(contentType);
            articleRepository.removeArticle(article);
            folderAndFileManger.removeFilesAndFolder(article.getWriter(), article.getRegdate(),contentType, 1);
        }
    }


}
