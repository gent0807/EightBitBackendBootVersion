//package com.eightbit.controller.file.view.article;
//
//import com.eightbit.entity.uploadfile.UploadFile;
//import com.eightbit.persistence.file.view.article.FreeArticleViewFileRepository;
//import com.eightbit.impl.token.TokenManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.List;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/Files/view/article/free/**")
//@RequiredArgsConstructor
//@Slf4j
//@Primary
//@PropertySource("classpath:upload.properties")
//public class FreeArticleViewFileController {
//
//    private final ViewFileService viewFileService;
//
//    private final FreeArticleViewFileRepository freeArticleViewFileRepository;
//
//    private TokenManager tokenManager;
//
//    @GetMapping("/file/{id}/{uploader}/{regdate}") //Files/view/article/free/file
//    public Resource downloadViewFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
//                                     @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
//        uploadFile.setUploader(uploader);
//        uploadFile.setRegdate(regdate);
//        uploadFile.setId(id);
//        uploadFile= freeArticleViewFileRepository.getViewFile(uploadFile);
//        String storeFilename=uploadFile.getStoreFilename();
//
//        regdate=regdate.replace(":","");
//
//        return new UrlResource("file:"+filepath+uploader+"/board/article/"+regdate+"/viewfiles/"+storeFilename);
//    }
//
//    @PostMapping(value="/files") //Files/view/article/free/files
//    @Transactional
//    public ResponseEntity<List<UploadFile>> insertArticleViewFiles(HttpServletRequest request, @RequestParam(value="writer") String writer,
//                                                                   @RequestParam(value = "regdate") String regdate,
//                                                                   @RequestParam(value ="files") List<MultipartFile> files,
//                                                                   String token, @Value("${file.dir}") String dir) throws IOException {
//        if(tokenManager.checkAccessToken(request, token, writer)){
//            return ResponseEntity.ok().body(articleServi.registerArticleViewFiles(writer, regdate, files, dir));
//        }
//
//        return ResponseEntity.ok().body(null);
//    }
//}
