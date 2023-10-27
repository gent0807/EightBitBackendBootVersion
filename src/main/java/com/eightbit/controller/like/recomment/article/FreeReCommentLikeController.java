package com.eightbit.controller.like.recomment.article;

import com.eightbit.entity.like.Like;
import com.eightbit.persistence.like.recomment.article.FreeReCommentLikeRepository;
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
@RequestMapping(value = "/Likes/reComment/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeReCommentLikeController {


    private final FreeReCommentLikeRepository freeReCommentLikeRepository;

    private final TokenManager tokenManager;


    @GetMapping(value = "/likes") //Likes/reComment/free/likes
    public ResponseEntity<List<String>> getReCommentLikes(@RequestParam String reCommenter, @RequestParam String regdate, Like like){
        like.setAuthor(reCommenter);
        like.setRegdate(regdate);
        return ResponseEntity.ok().body(freeReCommentLikeRepository.getReCommentLikes(like));
    }

    @PostMapping(value = "/like") //Likes/reComment/free/like
    @Transactional
    public ResponseEntity<List<String>> insertReCommentLike(HttpServletRequest request, String token, @RequestBody Like like){
        if(tokenManager.checkAccessToken(request, token, like.getLiker())){
            return ResponseEntity.ok().body(freeReCommentLikeRepository.registerReCommentLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }
    @DeleteMapping(value = "/like/{liker}/{reCommenter}/{regdate}") //Likes/reComment/free/like
    @Transactional
    public ResponseEntity<List<String>> deleteReCommentLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                            @PathVariable String reCommenter, @PathVariable String regdate, Like like){
        if(tokenManager.checkAccessToken(request, token, liker)){
            like.setLiker(liker);
            like.setAuthor(reCommenter);
            like.setRegdate(regdate);
            return ResponseEntity.ok().body(freeReCommentLikeRepository.removeReCommentLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

}
