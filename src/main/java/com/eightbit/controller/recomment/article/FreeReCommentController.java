package com.eightbit.controller.recomment.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;

import com.eightbit.entity.file.UploadFile;
import com.eightbit.persistence.comment.article.FreeCommentRepository;
import com.eightbit.persistence.recomment.article.FreeReCommentRepository;
import com.eightbit.impl.token.TokenManager;
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
@RequestMapping(value = "/ReComments/free/*")
@RequiredArgsConstructor
@Slf4j
public class FreeReCommentController {

    private final FreeCommentRepository freeCommentRepository;

    private final FreeReCommentRepository freeReCommentRepository;

    private final TokenManager tokenManager;

    private final FolderAndFileManger folderAndFileManger;

    @GetMapping(value="/reComments") //ReComments/free/reComments
    public ResponseEntity<List<Comment>> getReComments(@RequestParam String original_replyer, @RequestParam String original_regdate, Comment reComment){
        reComment.setOriginal_author(original_replyer);
        reComment.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(freeReCommentRepository.getReComments(reComment));
    }

    @GetMapping(value = "/reComments/count")
    public ResponseEntity<Integer> getReCommentCount(@RequestParam String writer, @RequestParam String regdate, Article article){
        article.setWriter(writer);
        article.setRegdate(regdate);
        return ResponseEntity.ok().body(freeReCommentRepository.getReCommentCount(article));
    }
    @PostMapping(value="/reComment") //ReComments/free/reComment
    @Transactional
    public ResponseEntity<Comment> insertReComment(HttpServletRequest request, String token, @RequestBody Comment reComment){
        if(tokenManager.checkAccessToken(request, token, reComment.getAuthor())){
            return ResponseEntity.ok().body(freeReCommentRepository.registerReComment(reComment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/reComment") //ReComments/free/reComment
    @Transactional
    public ResponseEntity<String> updateReComment(HttpServletRequest request, String token,
                                                  @RequestParam String reCommenter, @RequestParam String regdate, @RequestBody Comment reComment){
        if(tokenManager.checkAccessToken(request, token,reCommenter)){
            reComment.setAuthor(reCommenter);
            reComment.setRegdate(regdate);
            freeReCommentRepository.modifyReComment(reComment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

    @DeleteMapping(value="/reComment/{reCommenter}/{regdate}/{role}") //ReComments/article/free/reComment
    @Transactional
    public ResponseEntity<String> deleteReComment(HttpServletRequest request, String token,
                                                  @PathVariable("reCommenter") String reCommenter,@PathVariable("regdate") String regdate, @PathVariable("role") String role, Comment reComment){

        if(tokenManager.checkAccessToken(request, token, reCommenter) || role.equals("ADMIN")){
            reComment.setAuthor(reCommenter);
            reComment.setRegdate(regdate);
            freeReCommentRepository.removeReComment(reComment);
            Comment comment=freeCommentRepository.getOriginWriterAndRegdate(new UploadFile(reComment.getOriginal_author(), reComment.getOriginal_regdate()));
            folderAndFileManger.removeReCommentFilesAndFolder(reComment.getAuthor(), reComment.getRegdate(), reComment.getOriginal_author(), reComment.getOriginal_regdate(), comment.getOriginal_author(), comment.getOriginal_regdate(), "article","free", "viewfiles");
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");

    }

}
