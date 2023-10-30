package com.eightbit.controller.like;

import com.eightbit.entity.like.Like;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.like.LikeRepository;
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
@RequestMapping(value = "/Likes/*")
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeRepository likeRepository;

    private final TokenManager tokenManager;

    @GetMapping(value = "/likes")
    public ResponseEntity<List<String>> getLikes(@RequestParam String master, @RequestParam String regdate,
                                                        @RequestParam String contentType, @RequestParam int depth,
                                                        Like like){
        like.setMaster(master);
        like.setRegdate(regdate);
        like.setContentType(contentType);
        like.setDepth(depth);
        return ResponseEntity.ok().body(likeRepository.getLikers(like));
    }


    @PostMapping(value = "/like")
    @Transactional
    public ResponseEntity<List<String>> insertLike(HttpServletRequest request, String token, @RequestBody Like like){
        if(tokenManager.checkAccessToken(request, token, like.getLiker())){
            System.out.println(like);
            return ResponseEntity.ok().body(likeRepository.registerLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @DeleteMapping(value = "/like/{liker}/{writer}/{regdate}/{contentType}/{depth}")
    @Transactional
    public ResponseEntity<List<String>> deleteLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String writer, @PathVariable String regdate,
                                                          @PathVariable String contentType, @PathVariable int depth,
                                                          Like like){
        if(tokenManager.checkAccessToken(request, token, liker)){
            like.setLiker(liker);
            like.setMaster(writer);
            like.setRegdate(regdate);
            like.setContentType(contentType);
            like.setDepth(depth);
            return ResponseEntity.ok().body(likeRepository.removeLike(like));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

}
