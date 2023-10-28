package com.eightbit.controller.like.article;


import com.eightbit.entity.like.Like;
import com.eightbit.persistence.like.article.FreeArticleLikeRepository;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/Likes/article/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeArticleLikeController {

    private final FreeArticleLikeRepository freeArticleLikeRepository;

    private final TokenManager tokenManager;

    @GetMapping(value = "/likes") //Likes/article/free/likes
    public ResponseEntity<List<String>> getArticleLikes(@RequestParam String writer, @RequestParam String regdate, Like like){
        like.setAuthor(writer);
        like.setRegdate(regdate);
        return ResponseEntity.ok().body(freeArticleLikeRepository.getArticleLikers(like));
    }


    @PostMapping(value = "/like") //Likes/article/free/like
    @Transactional
    public ResponseEntity<List<String>> insertArticleLike(HttpServletRequest request, String token, @RequestBody Like like){
        if(tokenManager.checkAccessToken(request, token, like.getLiker())){
            return ResponseEntity.ok().body(freeArticleLikeRepository.registerArticleLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @DeleteMapping(value = "/like/{liker}/{writer}/{regdate}") //Likes/article/free/like
    @Transactional
    public ResponseEntity<List<String>> deleteArticleLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String writer, @PathVariable String regdate, Like like){
        if(tokenManager.checkAccessToken(request, token, liker)){
            like.setLiker(liker);
            like.setAuthor(writer);
            like.setRegdate(regdate);
            return ResponseEntity.ok().body(freeArticleLikeRepository.removeArticleLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }
}


