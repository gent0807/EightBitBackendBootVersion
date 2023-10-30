package com.eightbit.controller.comment;

import com.eightbit.entity.comment.Comment;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.comment.CommentRepository;
import com.eightbit.util.file.FolderAndFileManger;
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
@RequestMapping(value = "/Comments/*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentRepository commentRepository;

    private final TokenManager tokenManager;

    private final FolderAndFileManger folderAndFileManger;


    @GetMapping(value="/comments")
    public ResponseEntity<List<Comment>> getComments(@RequestParam String original_author, @RequestParam String original_regdate,
                                                     @RequestParam String contentType, @RequestParam int depth, Comment comment) {
        comment.setOriginal_author(original_author);
        comment.setOriginal_regdate(original_regdate);
        comment.setContentType(contentType);
        comment.setDepth(depth);
        return ResponseEntity.ok().body(commentRepository.getComments(comment));
    }

    @GetMapping(value="/count")
    public ResponseEntity<Integer> getReCommentCount(@RequestParam String writer, @RequestParam String regdate, Comment comment) {
        comment.setOriginal_author(writer);
        comment.setOriginal_regdate(regdate);
        return ResponseEntity.ok().body(commentRepository.getReCommentCount(comment));
    }


    @PostMapping(value="/comment")
    @Transactional
    public ResponseEntity<Comment> insertComment(HttpServletRequest request, String token, @RequestBody Comment comment){
        if(tokenManager.checkAccessToken(request, token, comment.getAuthor())){
            return ResponseEntity.ok().body(commentRepository.registerComment(comment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PatchMapping(value="/comment")
    @Transactional
    public ResponseEntity<String> updateComment(HttpServletRequest request, String token,
                                              @RequestParam String replyer, @RequestParam String regdate, @RequestBody Comment comment){

        if(tokenManager.checkAccessToken(request,token,replyer)){
            comment.setAuthor(replyer);
            comment.setRegdate(regdate);
            commentRepository.modifyComment(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

    @DeleteMapping(value="/comment/{commenter}/{regdate}/{contentType}/{depth}/{role}") //Comments/free/comment
    @Transactional
    public ResponseEntity<String> deleteComment(HttpServletRequest request, String token,
                                              @PathVariable("commenter") String commenter, @PathVariable("regdate") String regdate,
                                              @PathVariable("role") String role, @PathVariable("contentType") String contentType, @PathVariable("depth") int depth,
                                              Comment comment) {
        if(tokenManager.checkAccessToken(request, token, commenter) || role.equals("ADMIN")){
            comment.setAuthor(commenter);
            comment.setRegdate(regdate);
            comment.setContentType(contentType);
            comment.setDepth(depth);
            folderAndFileManger.removeFilesAndFolder(comment.getAuthor(), comment.getRegdate(), contentType, depth);
            commentRepository.removeComment(comment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

}
