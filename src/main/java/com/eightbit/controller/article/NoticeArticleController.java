package com.eightbit.controller.article;


import com.eightbit.entity.article.Article;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.article.FreeArticleRepository;
import com.eightbit.persistence.article.NoticeArticleRepository;
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
@RequestMapping("/Articles/notice/*")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class NoticeArticleController {

    private final NoticeArticleRepository noticeArticleRepository;

    private final TokenManager tokenManager;

    @GetMapping(value = "/articles")
    public ResponseEntity<List<Article>> getList(){
        return ResponseEntity.ok().body(noticeArticleRepository.getList());
    }


    @GetMapping(value="/article")
    public ResponseEntity<Article> getArticle(@RequestParam String viewer, @RequestParam String writer, @RequestParam String regdate,
                                              @RequestParam String contentType, ArticleView articleView){
        articleView.setViewer(viewer);
        articleView.setWriter(writer);
        articleView.setRegdate(regdate);
        articleView.setContentType(contentType);
        return ResponseEntity.ok().body(noticeArticleRepository.getArticle(articleView));
    }

    @GetMapping(value="/user/articles")
    public ResponseEntity<List<Article>> getUserArticles(@RequestParam String writer){
        return ResponseEntity.ok().body(noticeArticleRepository.getUserArticles(writer));
    }

    @PostMapping(value = "/article")
    @Transactional
    public ResponseEntity<Article> insertArticle(HttpServletRequest request, String token, @RequestBody Article article){

        if(tokenManager.checkAccessToken(request, token, article.getWriter())){
            return ResponseEntity.ok().body(noticeArticleRepository.registerArticle(article));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/article")
    @Transactional
    public void updateArticle(HttpServletRequest request, String token, @RequestParam("writer") String writer, @RequestParam("regdate") String regdate, @RequestBody Article article){
        if(tokenManager.checkAccessToken(request,token,writer)){
            article.setWriter(writer);
            article.setRegdate(regdate);
            noticeArticleRepository.modifyArticle(article);
        }
    }

    @DeleteMapping(value = "/article/{writer}/{regdate}/{role}")
    @Transactional
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("writer") String writer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, Article article){
        if(tokenManager.checkAccessToken(request,  token, writer)|| role.equals("ADMIN")){
            article.setWriter(writer);
            article.setRegdate(regdate);
            noticeArticleRepository.removeArticle(article);
        }
    }

}
