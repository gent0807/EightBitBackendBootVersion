package com.eightbit.controller.comment.article;

import com.eightbit.entity.comment.Comment;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.inter.comment.CommentService;
import com.eightbit.persistence.comment.article.FreeCommentRepository;
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
@RequestMapping(value = "/Comments/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeCommentController {

    private final FreeCommentRepository freeCommentRepository;

    private final TokenManager tokenManager;

    @GetMapping(value="/comments")
    public ResponseEntity<List<Comment>> getReplies(@RequestParam String original_writer, @RequestParam String original_regdate, Comment comment) {
        comment.setOriginal_author(original_writer);
        comment.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(freeCommentRepository.getReplies(comment));
    }


    @PostMapping(value="/comment")
    @Transactional
    public ResponseEntity<Comment> insertReply(HttpServletRequest request, String token, @RequestBody Comment comment){
        if(tokenManager.checkAccessToken(request, token, comment.getAuthor())){
            return ResponseEntity.ok().body(freeCommentRepository.registerReply(comment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PatchMapping(value="/comment")
    @Transactional
    public ResponseEntity<String> updateReply(HttpServletRequest request, String token,
                                              @RequestParam String replyer, @RequestParam String regdate, @RequestBody Comment comment){

        if(tokenManager.checkAccessToken(request,token,replyer)){
            comment.setAuthor(replyer);
            comment.setRegdate(regdate);
            freeCommentRepository.modifyReply(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

    @DeleteMapping(value="/comment/{replyer}/{regdate}/{role}") //Comments/free/comment
    @Transactional
    public ResponseEntity<String> deleteReply(HttpServletRequest request, String token,
                                              @PathVariable("replyer") String replyer, @PathVariable("regdate") String regdate,
                                              @PathVariable("role") String role, Comment comment) {
        if(tokenManager.checkAccessToken(request, token, replyer) || role.equals("ADMIN")){
            comment.setAuthor(replyer);
            comment.setRegdate(regdate);
            freeCommentRepository.removeReply(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

}
