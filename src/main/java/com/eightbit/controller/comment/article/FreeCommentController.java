package com.eightbit.controller.comment.article;

import com.eightbit.entity.comment.Comment;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.util.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/Comments/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeCommentController {

    private final ArticleService articleService;

    private final TokenManager tokenManager;

    @GetMapping(value="/comments")
    public ResponseEntity<List<Comment>> getReplies(@RequestParam String original_writer, @RequestParam String original_regdate, Comment comment) {
        comment.setOriginal_writer(original_writer);
        comment.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(articleService.getReplies(comment));
    }

    @GetMapping(value = "/comment")
    public ResponseEntity<Comment> getReply(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReply(comment));
    }

    @PostMapping(value="/comment")
    public ResponseEntity<Comment> insertReply(HttpServletRequest request, String token, @RequestBody Comment comment){
        if(tokenManager.checkAccessToken(request, token, comment.getReplyer())){
            return ResponseEntity.ok().body(articleService.registerReply(comment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PatchMapping(value="/comment")
    public ResponseEntity<String> updateReply(HttpServletRequest request, String token,
                                              @RequestParam String replyer, @RequestParam String regdate, @RequestBody Comment comment){

        if(tokenManager.checkAccessToken(request,token,replyer)){
            comment.setReplyer(replyer);
            comment.setRegdate(regdate);
            articleService.modifyReply(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }


    @PatchMapping(value="/report/reply/abuse") //Comments/free/report/abuse
    public void reportAbuseReply(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        articleService.modifyAbuseReply(comment);
    }
    @PatchMapping(value="/report/reply/19") //Comments/free/report/19
    public void report19Reply(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        articleService.modify19Reply(comment);
    }
    @PatchMapping(value="/report/reply/incoporate") //Comments/free/report/incoporate
    public void reportIncopoateReply(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        articleService.modifyIncoporateReply(comment);
    }


    @DeleteMapping(value="/article/reply/{replyer}/{regdate}/{role}") //Comments/free/comment
    public ResponseEntity<String> deleteReply(HttpServletRequest request, String token,
                                              @PathVariable("replyer") String replyer, @PathVariable("regdate") String regdate,
                                              @PathVariable("role") String role, Comment comment) {
        if(tokenManager.checkAccessToken(request, token, replyer) || role.equals("ADMIN")){
            comment.setReplyer(replyer);
            comment.setRegdate(regdate);
            articleService.removeReply(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

}
