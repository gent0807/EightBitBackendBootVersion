package com.eightbit.view_controller.board_controller;

import com.eightbit.biz.board.inter.BoardService;
import com.eightbit.biz.board.vo.*;
import com.eightbit.biz.user.inter.UserService;
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

import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Board/*")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class BoardController {

    private final BoardService boardService;

    private final UserService userService;

    @GetMapping(value = "/articles")
    public ResponseEntity<List<BoardVO>> getList(){
        return ResponseEntity.ok().body(boardService.getList());
    }

    @GetMapping(value="/user/articles")
    public ResponseEntity<List<BoardVO>> getUserArticles(@RequestParam String writer){
        return ResponseEntity.ok().body(boardService.getUserArticles(writer));
    }

    @GetMapping(value="/article/replies")
    public ResponseEntity<List<ReplyVO>> getReplies(@RequestParam String original_writer, @RequestParam String original_regdate, ReplyVO replyVO) {
        replyVO.setOriginal_writer(original_writer);
        replyVO.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(boardService.getReplies(replyVO));
    }

    @GetMapping(value="/article/reply/reComments")
    public ResponseEntity<List<ReCommentVO>> getReComments(@RequestParam String original_replyer, @RequestParam String original_regdate, ReCommentVO reCommentVO){
        reCommentVO.setOriginal_replyer(original_replyer);
        reCommentVO.setOriginal_regdate(original_regdate);
        return ResponseEntity.ok().body(boardService.getReComments(reCommentVO));
    }

    @GetMapping(value="/article")
    public ResponseEntity<BoardVO> getArticle(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getArticle(boardVO));
    }

    @GetMapping(value="/article/attaches/{writer}/{regdate}")
    public ResponseEntity<List<UploadFile>> getAttachList(@PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
        uploadFile.setUploader(writer);
        uploadFile.setRegdate(regdate);
        return  ResponseEntity.ok().body(boardService.getAttachList(uploadFile));
    }

    @GetMapping(value = "/article/attach/{id}/{uploader}/{regdate}")
    public ResponseEntity<Resource> downloadAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                                       @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile=boardService.getAttachFile(uploadFile);
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

    @GetMapping("/article/view/{id}/{uploader}/{regdate}")
    public Resource downloadViewFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                     @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile=boardService.getViewFile(uploadFile);
        String storeFilename=uploadFile.getStoreFilename();

        regdate=regdate.replace(":","");

        return new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/viewfiles/"+storeFilename);
    }

    @GetMapping(value = "article/reply")
    public ResponseEntity<ReplyVO> getReply(@RequestParam String replyer, @RequestParam String regdate, ReplyVO replyVO){
        replyVO.setReplyer(replyer);
        replyVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getReply(replyVO));
    }

    @GetMapping(value = "article/reply/reComment")
    public ResponseEntity<ReCommentVO> getReComment(@RequestParam String reCommenter, @RequestParam String regdate, ReCommentVO reCommentVO){
        reCommentVO.setReCommenter(reCommenter);
        reCommentVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getReComment(reCommentVO));
    }

    @GetMapping(value = "/article/likers")
    public ResponseEntity<List<String>> getArticleLikers(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getArticleLikers(boardVO));
    }

    @GetMapping(value = "/article/reply/likers")
    public ResponseEntity<List<String>> getReplyLikers(@RequestParam String replyer, @RequestParam String regdate, ReplyVO replyVO){
        replyVO.setReplyer(replyer);
        replyVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getReplyLikers(replyVO));
    }

    @GetMapping(value = "/article/reply/reComment/likers")
    public ResponseEntity<List<String>> getReCommentLikers(@RequestParam String reCommenter, @RequestParam String regdate, ReCommentVO reCommentVO){
        reCommentVO.setReCommenter(reCommenter);
        reCommentVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getReCommentLikers(reCommentVO));
    }

    @GetMapping(value = "/article/totalcomment/count")
    public ResponseEntity<Integer> getReCommentCount(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        return ResponseEntity.ok().body(boardService.getReCommentCount(boardVO));
    }


    @PostMapping(value = "/article")
    public ResponseEntity<BoardVO> insertArticle(HttpServletRequest request, String token, @RequestBody BoardVO boardVO){

        if(checkAccessToken(request, token, boardVO.getWriter())){
            return ResponseEntity.ok().body(boardService.registerArticle(boardVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PostMapping(value = "/article/shareFiles")
    public void insertArticleShareFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
                                        @RequestParam(value="regdate") String regdate,
                                        @RequestParam(value="files") List<MultipartFile> files,
                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {

        Collection<Part> parts=request.getParts();
        log.info("parts={}", parts);

        for(Part part: parts){
            log.info("=== PART ===");
            log.info("name={}", part.getName());

            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName,
                        part.getHeader(headerName));
            }

            log.info("submittedFileName={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize()); //part body size
        }

        if(checkAccessToken(request, token, writer)) {
            boardService.registerArticleShareFiles(writer, regdate, files, dir);
        }
    }

    @PostMapping(value="/article/viewFiles")
    public ResponseEntity<String> insertArticleViewFile(HttpServletRequest request, @RequestParam(value="writer") String writer,
                                                         @RequestParam(value = "regdate") String regdate,
                                                         @RequestParam(value ="files") List<MultipartFile> files,
                                                         String token, @Value("${file.dir}") String dir){
        if(checkAccessToken(request, token, writer)){
            return ResponseEntity.ok().body(boardService.registerArticleViewFiles(writer, regdate, files,dir));
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value="/article/reply")
    public ResponseEntity<ReplyVO> insertReply(HttpServletRequest request, String token, @RequestBody ReplyVO replyVO){
        if(checkAccessToken(request, token, replyVO.getReplyer())){
            return ResponseEntity.ok().body(boardService.registerReply(replyVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value="/article/reply/reComment")
    public ResponseEntity<ReCommentVO> insertReComment(HttpServletRequest request,  String token, @RequestBody ReCommentVO reCommentVO){
        if(checkAccessToken(request, token, reCommentVO.getReCommenter())){
            return ResponseEntity.ok().body(boardService.registerReComment(reCommentVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }


    @PostMapping(value = "/article/view")
    public void insertArticleView(HttpServletRequest request, String token, @RequestBody ArticleViewVO articleViewVO){
        if(checkAccessToken(request,token, articleViewVO.getViewer())) {
            boardService.registerArticleView(articleViewVO);
        }
    }

    @PostMapping(value = "/article/like")
    public ResponseEntity<List<String>> insertArticleLike(HttpServletRequest request, String token, @RequestBody ArticleLikeVO articleLikeVO){
        if(checkAccessToken(request, token, articleLikeVO.getLiker())){
            return ResponseEntity.ok().body(boardService.registerArticleLike(articleLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value = "/article/reply/like")
    public ResponseEntity<List<String>> insertReplyLike(HttpServletRequest request, String token, @RequestBody ReplyLikeVO replyLikeVO){
        if(checkAccessToken(request, token, replyLikeVO.getLiker())){
            return ResponseEntity.ok().body(boardService.registerReplyLike(replyLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PostMapping(value = "article/reply/reComment/like")
    public ResponseEntity<List<String>> insertReCommentLike(HttpServletRequest request, String token, @RequestBody ReCommentLikeVO reCommentLikeVO){
        if(checkAccessToken(request, token, reCommentLikeVO.getLiker())){
            return ResponseEntity.ok().body(boardService.registerReCommentLike(reCommentLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/article")
    public void updateArticle(HttpServletRequest request, String token, @RequestParam("writer") String writer, @RequestParam("regdate") String regdate, @RequestBody BoardVO boardVO){
        if(checkAccessToken(request,token,writer)){
            boardVO.setWriter(writer);
            boardVO.setRegdate(regdate);
            boardService.modifyArticle(boardVO);
        }
    }

    @PatchMapping(value="/article/reply")
    public ResponseEntity<String> updateReply(HttpServletRequest request, String token,
                                              @RequestParam String replyer, @RequestParam String regdate, @RequestBody ReplyVO replyVO){

        if(checkAccessToken(request,token,replyer)){
            replyVO.setReplyer(replyer);
            replyVO.setRegdate(regdate);
            boardService.modifyReply(replyVO);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

    @PatchMapping(value="/article/reply/reComment")
    public ResponseEntity<String> updateReComment(HttpServletRequest request, String token,
                                                  @RequestParam String reCommenter, @RequestParam String regdate, @RequestBody ReCommentVO reCommentVO){
        if(checkAccessToken(request, token,reCommenter)){
            reCommentVO.setReCommenter(reCommenter);
            reCommentVO.setRegdate(regdate);
            boardService.modifyReComment(reCommentVO);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }


    @PatchMapping(value="/report/article/abuse")
    public void reportAbuseArticle(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        boardService.modifyAbuseArticle(boardVO);
    }

    @PatchMapping(value = "/report/article/19")
    public void report19Article(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        boardService.modify19Article(boardVO);
    }

    @PatchMapping(value="/report/article/incoporate")
    public void reportIncoporateArticle(@RequestParam String writer, @RequestParam String regdate, BoardVO boardVO){
        boardVO.setWriter(writer);
        boardVO.setRegdate(regdate);
        boardService.modifyIncoporateArticle(boardVO);
    }

    @PatchMapping(value="/report/reply/abuse")
    public void reportAbuseReply(@RequestParam String replyer, @RequestParam String regdate, ReplyVO replyVO){
        replyVO.setReplyer(replyer);
        replyVO.setRegdate(regdate);
        boardService.modifyAbuseReply(replyVO);
    }
    @PatchMapping(value="/report/reply/19")
    public void report19Reply(@RequestParam String replyer, @RequestParam String regdate, ReplyVO replyVO){
        replyVO.setReplyer(replyer);
        replyVO.setRegdate(regdate);
        boardService.modify19Reply(replyVO);
    }
    @PatchMapping(value="/report/reply/incoporate")
    public void reportIncopoateReply(@RequestParam String replyer, @RequestParam String regdate, ReplyVO replyVO){
        replyVO.setReplyer(replyer);
        replyVO.setRegdate(regdate);
        boardService.modifyIncoporateReply(replyVO);
    }

    @PatchMapping(value="/report/reComment/abuse")
    public void reportAbuseReComment(@RequestParam String reCommenter, @RequestParam String regdate, ReCommentVO reCommentVO){
        reCommentVO.setReCommenter(reCommenter);
        reCommentVO.setRegdate(regdate);
        boardService.modifyAbuseReComment(reCommentVO);
    }

    @PatchMapping(value="/report/reComment/19")
    public void report19ReComment(@RequestParam String reCommenter, @RequestParam String regdate, ReCommentVO reCommentVO){
        reCommentVO.setReCommenter(reCommenter);
        reCommentVO.setRegdate(regdate);
        boardService.modify19ReComment(reCommentVO);
    }


    @PatchMapping(value="/report/reComment/incoporate")
    public void reportIncoporateReComment(@RequestParam String reCommenter, @RequestParam String regdate, ReCommentVO reCommentVO){
        reCommentVO.setReCommenter(reCommenter);
        reCommentVO.setRegdate(regdate);
        boardService.modifyIncoporateReComment(reCommentVO);
    }

    @DeleteMapping(value = "/article/{writer}/{regdate}/{role}")
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("writer") String writer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, BoardVO boardVO){
        if(checkAccessToken(request,  token, writer)|| role.equals("ADMIN")){
            boardVO.setWriter(writer);
            boardVO.setRegdate(regdate);
            boardService.removeArticle(boardVO);
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

    @DeleteMapping(value="/article/shareFiles")
    public void deleteArticleShareFile(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(checkAccessToken(request, token, deleteFileList.get(0).getUploader())){

            for(UploadFile deleteFile : deleteFileList){
                boardService.removeArticleShareFile(deleteFile);
            }
        }
    }

    @DeleteMapping(value="/article/reply/{replyer}/{regdate}/{role}")
    public ResponseEntity<String> deleteReply(HttpServletRequest request, String token,
                                              @PathVariable("replyer") String replyer, @PathVariable("regdate") String regdate,
                                              @PathVariable("role") String role, ReplyVO replyVO) {
        if(checkAccessToken(request, token, replyer) || role.equals("ADMIN")){
            replyVO.setReplyer(replyer);
            replyVO.setRegdate(regdate);
            boardService.removeReply(replyVO);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");
    }

    @DeleteMapping(value="/article/reply/reComment/{reCommenter}/{regdate}/{role}")
    public ResponseEntity<String> deleteReComment(HttpServletRequest request, String token,
                                                  @PathVariable("reCommenter") String reCommenter,@PathVariable("regdate") String regdate, @PathVariable("role") String role, ReCommentVO reCommentVO){

        if(checkAccessToken(request, token, reCommenter) || role.equals("ADMIN")){
            reCommentVO.setReCommenter(reCommenter);
            reCommentVO.setRegdate(regdate);
            boardService.removeReComment(reCommentVO);
            return ResponseEntity.ok().body("");
        }

        return ResponseEntity.status(HttpStatus.resolve(403)).body("불허된 접근입니다!");

    }

    @DeleteMapping(value = "/article/like/{liker}/{writer}/{regdate}")
    public ResponseEntity<List<String>> deleteArticleLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String writer, @PathVariable String regdate,ArticleLikeVO articleLikeVO){
        if(checkAccessToken(request, token, liker)){
            articleLikeVO.setLiker(liker);
            articleLikeVO.setWriter(writer);
            articleLikeVO.setRegdate(regdate);
            return ResponseEntity.ok().body(boardService.removeArticleLike(articleLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @DeleteMapping(value = "/article/reply/like/{liker}/{replyer}/{regdate}")
    public ResponseEntity<List<String>> deleteReplyLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                          @PathVariable String replyer, @PathVariable String regdate, ReplyLikeVO replyLikeVO){
        if(checkAccessToken(request, token, liker)){
            replyLikeVO.setLiker(liker);
            replyLikeVO.setReplyer(replyer);
            replyLikeVO.setRegdate(regdate);
            return ResponseEntity.ok().body(boardService.removeReplyLike(replyLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @DeleteMapping(value = "/article/reply/reComment/like/{liker}/{reCommenter}/{regdate}")
    public ResponseEntity<List<String>> deleteReCommentLike(HttpServletRequest request, String token, @PathVariable String liker,
                                                        @PathVariable String reCommenter, @PathVariable String regdate, ReCommentLikeVO reCommentLikeVO){
        if(checkAccessToken(request, token, liker)){
            reCommentLikeVO.setLiker(liker);
            reCommentLikeVO.setReCommenter(reCommenter);
            reCommentLikeVO.setRegdate(regdate);
            return ResponseEntity.ok().body(boardService.removeReCommentLike(reCommentLikeVO));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    public boolean checkAccessToken(HttpServletRequest request, String token, String writer){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            token=bearerToken.substring(7);
        }

        return token.equals(userService.getAccessToken(writer));
    }

}
