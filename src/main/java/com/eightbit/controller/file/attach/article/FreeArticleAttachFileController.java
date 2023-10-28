//package com.eightbit.controller.file.attach.article;
//
//import com.eightbit.entity.uploadfile.UploadFile;
//import com.eightbit.persistence.file.attach.article.FreeArticleAttachFileRepository;
//import com.eightbit.impl.token.TokenManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.util.UriUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/Files/attach/article/free/**")
//@RequiredArgsConstructor
//@Slf4j
//@Primary
//@PropertySource("classpath:upload.properties")
//public class FreeArticleAttachFileController {
//
//    private final AttachFileService attachFileService;
//
//    private final FreeArticleAttachFileRepository freeAttachRepository;
//
//    private final TokenManager tokenManager;
//
//
//    @GetMapping(value="/attaches/{writer}/{regdate}") //Files/attach/article/free/attatches
//    public ResponseEntity<List<UploadFile>> getAttachList(@PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
//        uploadFile.setUploader(writer);
//        uploadFile.setRegdate(regdate);
//        return  ResponseEntity.ok().body(freeAttachRepository.getAttachList(uploadFile));
//    }
//
//    @GetMapping(value = "/article/attach/{id}/{uploader}/{regdate}")  //Files/attach/article/free/attatch
//    public ResponseEntity<Resource> downloadAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
//                                                       @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
//        uploadFile.setUploader(uploader);
//        uploadFile.setRegdate(regdate);
//        uploadFile.setId(id);
//        uploadFile= freeAttachRepository.getAttachFile(uploadFile);
//        String storeFilename=uploadFile.getStoreFilename();
//        String uploadFilename=uploadFile.getUploadFilename();
//        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
//        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";
//
//        regdate=regdate.replace(":","");
//
//        UrlResource resource=new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/sharefiles/"+storeFilename);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//    }
//
//    @PostMapping(value = "/article/shareFiles") //Files/attach/article/free/files
//    @Transactional
//    public void insertArticleShareFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
//                                        @RequestParam(value="regdate") String regdate,
//                                        @RequestParam(value="files") List<MultipartFile> files,
//                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {
//
//
//        if(tokenManager.checkAccessToken(request, token, writer)) {
//            attachFileService.registerArticleShareFiles(writer, regdate, files, dir);
//        }
//    }
//
//    @DeleteMapping(value="/article/shareFiles") //Files/attach/article/free/files
//    @Transactional
//    public void deleteArticleShareFile(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
//        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){
//
//            for(UploadFile deleteFile : deleteFileList){
//                attachFileService.removeArticleShareFile(deleteFile);
//            }
//        }
//    }
//
//    //    @DeleteMapping(value="/article/shareFiles/{id}/{writer}/{regdate}")
////     public void deleteArticleShareFile(HttpServletRequest request, String token, @PathVariable int id,
////                                       @PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
////        System.out.println(regdate);
////        if(tokenManager.checkAccessToken(request, token, writer)){
////            uploadFile.setId(id);
////            uploadFile.setUploader(writer);
////            uploadFile.setRegdate(regdate);
////            articleService.removeArticleShareFile(uploadFile);
////        }
////    }
//}
