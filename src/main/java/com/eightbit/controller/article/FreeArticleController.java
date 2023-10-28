package com.eightbit.controller.article;

import com.eightbit.entity.article.Article;
import com.eightbit.entity.view.ArticleView;
import com.eightbit.inter.article.ArticleService;
import com.eightbit.persistence.article.FreeArticleRepository;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private final FreeArticleRepository freeArticleRepository;

    private final TokenManager tokenManager;

    @GetMapping(value = "/articles")
    public ResponseEntity<List<Article>> getList(){
        return ResponseEntity.ok().body(articleService.getList());
    }


    @GetMapping(value="/article")
    public ResponseEntity<Article> getArticle(@RequestParam String viewer, @RequestParam String writer, @RequestParam String regdate,
                                              @RequestParam String boardType, ArticleView articleView){
        articleView.setViewer(viewer);
        articleView.setWriter(writer);
        articleView.setRegdate(regdate);
        articleView.setBoardType(boardType);
        return ResponseEntity.ok().body(freeArticleRepository.getArticle(articleView));
    }

    @GetMapping(value="/user/articles")
    public ResponseEntity<List<Article>> getUserArticles(@RequestParam String writer){
        return ResponseEntity.ok().body(articleService.getUserArticles(writer));
    }

    @PostMapping(value = "/article")
    @Transactional
    public ResponseEntity<Article> insertArticle(HttpServletRequest request, String token, @RequestBody Article article){

        if(tokenManager.checkAccessToken(request, token, article.getWriter())){
            return ResponseEntity.ok().body(articleService.registerArticle(article));
        }
        return ResponseEntity.status(HttpStatus.resolve(403)).body(null);
    }

    @PatchMapping(value="/article")
    @Transactional
    public void updateArticle(HttpServletRequest request, String token, @RequestParam("writer") String writer, @RequestParam("regdate") String regdate, @RequestBody Article article){
        if(tokenManager.checkAccessToken(request,token,writer)){
            article.setWriter(writer);
            article.setRegdate(regdate);
            articleService.modifyArticle(article);
        }
    }

    @DeleteMapping(value = "/article/{writer}/{regdate}/{role}")
    @Transactional
    public void deleteArticle(HttpServletRequest request, String token,
                              @PathVariable("writer") String writer, @PathVariable("regdate") String regdate,
                              @PathVariable("role") String role, Article article){
        if(tokenManager.checkAccessToken(request,  token, writer)|| role.equals("ADMIN")){
            article.setWriter(writer);
            article.setRegdate(regdate);
            articleService.removeArticle(article);
        }
    }



//    @GetMapping(value="/article/attaches/{writer}/{regdate}") //Files/attach/article/free/attatches
//    public ResponseEntity<List<UploadFile>> getAttachList(@PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
//        uploadFile.setUploader(writer);
//        uploadFile.setRegdate(regdate);
//        return  ResponseEntity.ok().body(articleService.getAttachList(uploadFile));
//    }

//    @GetMapping(value = "/article/attach/{id}/{uploader}/{regdate}")  //Files/attach/article/free/attatch
//    public ResponseEntity<Resource> downloadAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
//                                                       @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
//        uploadFile.setUploader(uploader);
//        uploadFile.setRegdate(regdate);
//        uploadFile.setId(id);
//        uploadFile= articleService.getAttachFile(uploadFile);
//        String storeFilename=uploadFile.getStoreFilename();
//        String uploadFilename=uploadFile.getUploadFilename();
//        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
//        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";

//        regdate=regdate.replace(":","");

//        UrlResource resource=new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/sharefiles/"+storeFilename);

//       return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//    }

//    @GetMapping("/article/view/{id}/{uploader}/{regdate}") //Files/view/article/free/view
//    public Resource downloadViewFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
//                                     @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
//        uploadFile.setUploader(uploader);
//        uploadFile.setRegdate(regdate);
//        uploadFile.setId(id);
//        uploadFile= articleService.getViewFile(uploadFile);
//        String storeFilename=uploadFile.getStoreFilename();

//        regdate=regdate.replace(":","");

//        return new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/viewfiles/"+storeFilename);
//    }



//    @PostMapping(value = "/article/shareFiles") //Files/attach/article/free/files
//    @Transactional
//    public void insertArticleShareFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
//                                        @RequestParam(value="regdate") String regdate,
//                                        @RequestParam(value="files") List<MultipartFile> files,
//                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {
//
//
//        if(tokenManager.checkAccessToken(request, token, writer)) {
//            articleService.registerArticleShareFiles(writer, regdate, files, dir);
//        }
//    }

//    @PostMapping(value="/article/viewFiles") //Files/view/article/free/files
//    @Transactional
//    public ResponseEntity<List<UploadFile>> insertArticleViewFiles(HttpServletRequest request, @RequestParam(value="writer") String writer,
//                                                         @RequestParam(value = "regdate") String regdate,
//                                                         @RequestParam(value ="files") List<MultipartFile> files,
//                                                         String token, @Value("${file.dir}") String dir) throws IOException {
//        if(tokenManager.checkAccessToken(request, token, writer)){
//            return ResponseEntity.ok().body(articleService.registerArticleViewFiles(writer, regdate, files, dir));
//        }
//
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping(value = "/article/reply/viewFiles") //Files/view/comment/free/files
//    @Transactional
//    public ResponseEntity<List<UploadFile>> insertReplyViewFiles(HttpServletRequest request, @RequestParam(value="replyer") String replyer,
//                                                                 @RequestParam(value = "regdate") String regdate,
//                                                                 @RequestParam(value = "files") List<MultipartFile> files,
//                                                                 String token, @Value("${file.dir}") String dir) throws IOException{
//        if(tokenManager.checkAccessToken(request,token, replyer)){
//            return ResponseEntity.ok().body(articleService.registerReplyViewFiles(replyer,regdate,files,dir));
//        }
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping(value = "/article/reComment/viewFiles") //Files/view/reComment/free/files
//    @Transactional
//    public ResponseEntity<List<UploadFile>> insertReCommentViewFiles(HttpServletRequest request, @RequestParam(value="reCommenter") String reCommenter,
//                                                                 @RequestParam(value = "regdate") String regdate,
//                                                                 @RequestParam(value = "files") List<MultipartFile> files,
//                                                                 String token, @Value("${file.dir}") String dir) throws IOException {
//        if (tokenManager.checkAccessToken(request, token, reCommenter)) {
//            return ResponseEntity.ok().body(articleService.registerReCommentViewFiles(reCommenter, regdate, files, dir));
//        }
//        return ResponseEntity.ok().body(null);
//    }
//

//    @DeleteMapping(value="/article/shareFiles/{id}/{writer}/{regdate}")
//     public void deleteArticleShareFile(HttpServletRequest request, String token, @PathVariable int id,
//                                       @PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
//        System.out.println(regdate);
//        if(tokenManager.checkAccessToken(request, token, writer)){
//            uploadFile.setId(id);
//            uploadFile.setUploader(writer);
//            uploadFile.setRegdate(regdate);
//            articleService.removeArticleShareFile(uploadFile);
//        }
//    }
//
//    @DeleteMapping(value="/article/shareFiles") //Files/attach/article/free/files
//    @Transactional
//    public void deleteArticleShareFile(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
//        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){
//
//            for(UploadFile deleteFile : deleteFileList){
//                articleService.removeArticleShareFile(deleteFile);
//            }
//        }
//    }



}
