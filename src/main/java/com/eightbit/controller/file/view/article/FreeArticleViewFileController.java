package com.eightbit.controller.file.view.article;


import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.FileService;
import com.eightbit.persistence.file.attach.article.FreeArticleAttachFileRepository;
import com.eightbit.persistence.file.view.article.FreeArticleViewFileRepository;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/Files/view/article/free/**")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class FreeArticleViewFileController {

    private final FileService fileService;

    private final FreeArticleViewFileRepository freeArticleViewFileRepository;

    private final TokenManager tokenManager;

    @GetMapping(value="/views/{writer}/{regdate}")
    public ResponseEntity<List<UploadFile>> getArticleAttachFileList(@PathVariable String writer, @PathVariable String regdate, UploadFile uploadFile){
        uploadFile.setUploader(writer);
        uploadFile.setRegdate(regdate);
        return  ResponseEntity.ok().body(freeArticleViewFileRepository.getViewFileList(uploadFile));
    }

    @GetMapping(value = "/view/{id}/{uploader}/{regdate}")
    public ResponseEntity<Resource> downloadArticleAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                                              @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile= freeArticleViewFileRepository.getViewFile(uploadFile);
        String storeFilename=uploadFile.getStoreFilename();
        String uploadFilename=uploadFile.getUploadFilename();
        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";

        regdate=regdate.replace(":","");

        UrlResource resource=new UrlResource("file:"+filepath+uploader+"/article/free/"+regdate+"/viewfiles/"+storeFilename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @PostMapping(value = "/files") //Files/attach/article/free/files
    @Transactional
    public void insertArticleViewFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
                                        @RequestParam(value="regdate") String regdate,
                                        @RequestParam(value="files") List<MultipartFile> files,
                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {


        if(tokenManager.checkAccessToken(request, token, writer)) {
            List<UploadFile> viewfiles=fileService.registerBoardFiles(writer, regdate, files, dir, "article", "free", "viewfiles");
            for(UploadFile viewfile : viewfiles){
                freeArticleViewFileRepository.registerArticleViewFile(viewfile);
            }
        }
    }

    @DeleteMapping(value="/files")
    @Transactional
    public void deleteArticleViewFiles(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){

            for(UploadFile deleteFile : deleteFileList){
                if (freeArticleViewFileRepository.removeArticleViewFile(deleteFile)) {
                    fileService.removeBoardFile(deleteFile, "article", "free", "viewfiles");
                }
            }
        }
    }

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

}
