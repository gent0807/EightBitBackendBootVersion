package com.eightbit.controller.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.like.ArticleLike;
import com.eightbit.entity.like.ReCommentLike;
import com.eightbit.entity.like.CommentLike;
import com.eightbit.entity.recomment.ReComment;
import com.eightbit.entity.uploadfile.UploadFile;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.util.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Board/*") //Articles/free/*
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class FreeArticleController {

    private final ArticleService articleService;

    private final TokenManager tokenManager;

    @GetMapping(value = "/articles")
    public ResponseEntity<List<Article>> getList(){
        return ResponseEntity.ok().body(articleService.getList());
    }


    @GetMapping(value="/article")
    public ResponseEntity<Article> getArticle(@RequestParam String writer, @RequestParam String regdate, Article article){
        article.setWriter(writer);
        article.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getArticle(article));
    }

    @GetMapping(value="/user/articles")
    public ResponseEntity<List<Article>> getUserArticles(@RequestParam String writer){
        return ResponseEntity.ok().body(articleService.getUserArticles(writer));
    }

    @GetMapping(value="/article/replies") //Comments/free/comments
    public ResponseEntity<List<Comment>> getReplies(@RequestParam String original_writer, @RequestParam String original_regdate, Comment comment) {
        comment.setOriginal_writer(original_writer);
        comment.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(articleService.getReplies(comment));
    }

    @GetMapping(value = "/article/reply") //Comments/free/comment
    public ResponseEntity<Comment> getReply(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReply(comment));
    }


    @GetMapping(value="/article/reply/reComments") //ReComments/free/reComments
    public ResponseEntity<List<ReComment>> getReComments(@RequestParam String original_replyer, @RequestParam String original_regdate, ReComment reComment){
        reComment.setOriginal_replyer(original_replyer);
        reComment.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(articleService.getReComments(reComment));
    }

    @GetMapping(value = "/article/reply/reComment") //ReComments/free/reComment
    public ResponseEntity<ReComment> getReComment(@RequestParam String reCommenter, @RequestParam String regdate, ReComment reComment){
        reComment.setReCommenter(reCommenter);
        reComment.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReComment(reComment));
    }

    @GetMapping(value="/article/attaches/{writer}/{regdate}") //Files/attach/article/free/attatches
    public ResponseEntity<List<UploadFile>> getAttachList(@PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
        uploadFile.setUploader(writer);
        uploadFile.setRegdate(regdate);
        return  ResponseEntity.ok().body(articleService.getAttachList(uploadFile));
    }

    @GetMapping(value = "/article/attach/{id}/{uploader}/{regdate}")  //Files/attach/article/free/attatch
    public ResponseEntity<Resource> downloadAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                                       @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile= articleService.getAttachFile(uploadFile);
        String storeFilename=uploadFile.getStoreFilename();
        String uploadFilename=uploadFile.getUploadFilename();
        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";

        regdate=regdate.replace(":","");

        UrlResource resource=new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/sharefiles/"+storeFilename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/article/view/{id}/{uploader}/{regdate}") //Files/view/article/free/view
    public Resource downloadViewFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                     @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile= articleService.getViewFile(uploadFile);
        String storeFilename=uploadFile.getStoreFilename();

        regdate=regdate.replace(":","");

        return new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/viewfiles/"+storeFilename);
    }

    @GetMapping(value = "/article/likers") //Likes/article/free/likes
    public ResponseEntity<List<String>> getArticleLikers(@RequestParam String writer, @RequestParam String regdate, Article article){
        article.setWriter(writer);
        article.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getArticleLikers(article));
    }

    @GetMapping(value = "/article/reply/likers") //Likes/comment/free/likes
    public ResponseEntity<List<String>> getReplyLikers(@RequestParam String replyer, @RequestParam String regdate, Comment comment){
        comment.setReplyer(replyer);
        comment.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReplyLikers(comment));
    }

    @GetMapping(value = "/article/reply/reComment/likers") //Likes/reComment/free/likes
    public ResponseEntity<List<String>> getReCommentLikers(@RequestParam String reCommenter, @RequestParam String regdate, ReComment reComment){
        reComment.setReCommenter(reCommenter);
        reComment.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReCommentLikers(reComment));
    }

    @GetMapping(value = "/article/totalcomment/count")
    public ResponseEntity<Integer> getReCommentCount(@RequestParam String writer, @RequestParam String regdate, Article article){
        article.setWriter(writer);
        article.setRegdate(regdate);
        return ResponseEntity.ok().body(articleService.getReCommentCount(article));
    }


    @PostMapping(value = "/article")
    public ResponseEntity<Article> insertArticle(HttpServletRequest request, String token, @RequestBody Article article){

        if(tokenManager.checkAccessToken(request, token, article.getWriter())){
            return ResponseEntity.ok().body(articleService.registerArticle(article));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PostMapping(value = "/article/shareFiles") //Files/attach/article/free/files
    public void insertArticleShareFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
                                        @RequestParam(value="regdate") String regdate,
                                        @RequestParam(value="files") List<MultipartFile> files,
                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {


        if(tokenManager.checkAccessToken(request, token, writer)) {
            articleService.registerArticleShareFiles(writer, regdate, files, dir);
        }
    }

    @PostMapping(value="/article/viewFiles") //Files/view/article/free/files
    public ResponseEntity<List<UploadFile>> insertArticleViewFiles(HttpServletRequest request, @RequestParam(value="writer") String writer,
                                                         @RequestParam(value = "regdate") String regdate,
                                                         @RequestParam(value ="files") List<MultipartFile> files,
                                                         String token, @Value("${file.dir}") String dir) throws IOException {
        if(tokenManager.checkAccessToken(request, token, writer)){
            return ResponseEntity.ok().body(articleService.registerArticleViewFiles(writer, regdate, files, dir));
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/article/reply/viewFiles") //Files/view/comment/free/files
    public ResponseEntity<List<UploadFile>> insertReplyViewFiles(HttpServletRequest request, @RequestParam(value="replyer") String replyer,
                                                                 @RequestParam(value = "regdate") String regdate,
                                                                 @RequestParam(value = "files") List<MultipartFile> files,
                                                                 String token, @Value("${file.dir}") String dir) throws IOException{
        if(tokenManager.checkAccessToken(request,token, replyer)){
            return ResponseEntity.ok().body(articleService.registerReplyViewFiles(replyer,regdate,files,dir));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/article/reComment/viewFiles") //Files/view/reComment/free/files
    public ResponseEntity<List<UploadFile>> insertReCommentViewFiles(HttpServletRequest request, @RequestParam(value="reCommenter") String reCommenter,
                                                                 @RequestParam(value = "regdate") String regdate,
                                                                 @RequestParam(value = "files") List<MultipartFile> files,
                                                                 String token, @Value("${file.dir}") String dir) throws IOException{
        if(tokenManager.checkAccessToken(request,token, reCommenter)){
            return ResponseEntity.ok().body(articleService.registerReCommentViewFiles(reCommenter,regdate,files,dir));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value="/article/reply") //Comments/free/comment
    public ResponseEntity<Comment> insertReply(HttpServletRequest request, String token, @RequestBody Comment comment){
        if(tokenManager.checkAccessToken(request, token, comment.getReplyer())){
            return ResponseEntity.ok().body(articleService.registerReply(comment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value="/article/reply/reComment") //ReComments/free/reComment
    public ResponseEntity<ReComment> insertReComment(HttpServletRequest request, String token, @RequestBody ReComment reComment){
        if(tokenManager.checkAccessToken(request, token, reComment.getReCommenter())){
            return ResponseEntity.ok().body(articleService.registerReComment(reComment));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PostMapping(value = "/article/view") //Views/article/free/view
    public void insertArticleView(HttpServletRequest request, String token, @RequestBody ArticleView articleView){
        if(tokenManager.checkAccessToken(request,token, articleView.getViewer())) {
            articleService.registerArticleView(articleView);
        }
    }

    @PostMapping(value = "/article/like") //Likes/article/free/like
    public ResponseEntity<List<String>> insertArticleLike(HttpServletRequest request, String token, @RequestBody ArticleLike articleLike){
        if(tokenManager.checkAccessToken(request, token, articleLike.getLiker())){
            return ResponseEntity.ok().body(articleService.registerArticleLike(articleLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value = "/article/reply/like") //Likes/comment/free/like
    public ResponseEntity<List<String>> insertReplyLike(HttpServletRequest request, String token, @RequestBody CommentLike commentLike){
        if(tokenManager.checkAccessToken(request, token, commentLike.getLiker())){
            return ResponseEntity.ok().body(articleService.registerReplyLike(commentLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value = "article/reply/reComment/like") //Like/reComment/free/like
    public ResponseEntity<List<String>> insertReCommentLike(HttpServletRequest request, String token, @RequestBody ReCommentLike reCommentLike){
        if(tokenManager.checkAccessToken(request, token, reCommentLike.getLiker())){
            return ResponseEntity.ok().body(articleService.registerReCommentLike(reCommentLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/article")
    public void updateArticle(HttpServletRequest request, String token, @RequestParam("writer") String writer, @RequestParam("regdate") String regdate, @RequestBody Article article){
        if(tokenManager.checkAccessToken(request,token,writer)){
            article.setWriter(writer);
            article.setRegdate(regdate);
            articleService.modifyArticle(article);
        }
    }

    @PatchMapping(value="/article/reply") //Comments/free/comment
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

    @PatchMapping(value="/article/reply/reComment") //ReComments/free/reComment
    public ResponseEntity<String> updateReComment(HttpServletRequest request, String token,
                                                  @RequestParam String reCommenter, @RequestParam String regdate, @RequestBody ReComment reComment){
        if(tokenManager.checkAccessToken(request, token,reCommenter)){
            reComment.setReCommenter(reCommenter);
            reComment.setRegdate(regdate);
            articleService.modifyReComment(reComment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }


    @DeleteMapping(value = "/article/{writer}/{regdate}/{role}")
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("writer") String writer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, Article article){
        if(tokenManager.checkAccessToken(request,  token, writer)|| role.equals("ADMIN")){
            article.setWriter(writer);
            article.setRegdate(regdate);
            articleService.removeArticle(article);
        }
    }

//    @DeleteMapping(value="/article/shareFiles/{id}/{writer}/{regdate}")
//    public void deleteArticleShareFile(HttpServletRequest request, String token, @PathVariable int id,
//                                       @PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
//        System.out.println(regdate);
//        if(checkAccessToken(request, token, writer)){
//            uploadFile.setId(id);
//            uploadFile.setUploader(writer);
//            uploadFile.setRegdate(regdate);
//            boardService.removeArticleShareFile(uploadFile);
//        }
//    }

    @DeleteMapping(value="/article/shareFiles") //Files/attach/article/free/files
    public void deleteArticleShareFile(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){

            for(UploadFile deleteFile : deleteFileList){
                articleService.removeArticleShareFile(deleteFile);
            }
        }
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

    @DeleteMapping(value="/article/reply/reComment/{reCommenter}/{regdate}/{role}") //ReComments/article/free/reComment
    public ResponseEntity<String> deleteReComment(HttpServletRequest request, String token,
                                                  @PathVariable("reCommenter") String reCommenter,@PathVariable("regdate") String regdate, @PathVariable("role") String role, ReComment reComment){

        if(tokenManager.checkAccessToken(request, token, reCommenter) || role.equals("ADMIN")){
            reComment.setReCommenter(reCommenter);
            reComment.setRegdate(regdate);
            articleService.removeReComment(reComment);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");

    }

    @DeleteMapping(value = "/article/like/{liker}/{writer}/{regdate}") //Likes/article/free/like
    public ResponseEntity<List<String>> deleteArticleLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String writer, @PathVariable String regdate, ArticleLike articleLike){
        if(tokenManager.checkAccessToken(request, token, liker)){
            articleLike.setLiker(liker);
            articleLike.setWriter(writer);
            articleLike.setRegdate(regdate);
            return ResponseEntity.ok().body(articleService.removeArticleLike(articleLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @DeleteMapping(value = "/article/reply/like/{liker}/{replyer}/{regdate}") //Likes/comment/free/like
    public ResponseEntity<List<String>> deleteReplyLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String replyer, @PathVariable String regdate, CommentLike commentLike){
        if(tokenManager.checkAccessToken(request, token, liker)){
            commentLike.setLiker(liker);
            commentLike.setReplyer(replyer);
            commentLike.setRegdate(regdate);
            return ResponseEntity.ok().body(articleService.removeReplyLike(commentLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @DeleteMapping(value = "/article/reply/reComment/like/{liker}/{reCommenter}/{regdate}") //Likes/reComment/free/like
    public ResponseEntity<List<String>> deleteReCommentLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                        @PathVariable String reCommenter, @PathVariable String regdate, ReCommentLike reCommentLike){
        if(tokenManager.checkAccessToken(request, token, liker)){
            reCommentLike.setLiker(liker);
            reCommentLike.setReCommenter(reCommenter);
            reCommentLike.setRegdate(regdate);
            return ResponseEntity.ok().body(articleService.removeReCommentLike(reCommentLike));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

}
