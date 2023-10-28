//package com.eightbit.controller.file.view.comment.article;
//
//import com.eightbit.entity.uploadfile.UploadFile;
//import com.eightbit.persistence.file.view.comment.article.FreeCommentViewFileRepository;
//import com.eightbit.impl.token.TokenManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.List;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/Files/view/comment/free/**") //Articles/free/*
//@RequiredArgsConstructor
//@Slf4j
//@Primary
//@PropertySource("classpath:upload.properties")
//public class FreeCommentViewFileController {
//
//    private final ViewFileService viewFileService;
//
//    private final FreeCommentViewFileRepository freeArticleViewFileRepository;
//
//    private TokenManager tokenManager;
//
//
//    @PostMapping(value = "/files") //Files/view/comment/free/files
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
//
//}
