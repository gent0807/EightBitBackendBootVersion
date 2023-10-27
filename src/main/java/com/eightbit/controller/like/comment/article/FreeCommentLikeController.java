package com.eightbit.controller.like.comment.article;

import com.eightbit.entity.like.Like;
import com.eightbit.persistence.like.comment.article.FreeCommentLikeRepository;
import com.eightbit.util.token.TokenManager;
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
@RequestMapping(value = "/Likes/comment/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeCommentLikeController {


    private final FreeCommentLikeRepository freeCommentLikeRepository;

    private final TokenManager tokenManager;

    @GetMapping(value = "/likes") //Likes/comment/free/likes
    public ResponseEntity<List<String>> getReplyLikers(@RequestParam String replyer, @RequestParam String regdate, Like like){
        like.setAuthor(replyer);
        like.setRegdate(regdate);
        return ResponseEntity.ok().body(freeCommentLikeRepository.getReplyLikers(like));
    }


    @PostMapping(value = "//like") //Likes/comment/free/like
    @Transactional
    public ResponseEntity<List<String>> insertReplyLike(HttpServletRequest request, String token, @RequestBody Like like){
        if(tokenManager.checkAccessToken(request, token, like.getLiker())){
            return ResponseEntity.ok().body(freeCommentLikeRepository.registerReplyLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @DeleteMapping(value = "//like/{liker}/{replyer}/{regdate}") //Likes/comment/free/like
    @Transactional
    public ResponseEntity<List<String>> deleteReplyLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                        @PathVariable String replyer, @PathVariable String regdate, Like like){
        if(tokenManager.checkAccessToken(request, token, liker)){
            like.setLiker(liker);
            like.setAuthor(replyer);
            like.setRegdate(regdate);
            return ResponseEntity.ok().body(freeCommentLikeRepository.removeReplyLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


}
