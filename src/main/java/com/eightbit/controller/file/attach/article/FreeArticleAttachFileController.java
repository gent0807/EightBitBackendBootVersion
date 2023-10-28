package com.eightbit.controller.file.attach.article;


import com.eightbit.entity.file.UploadFile;
import com.eightbit.inter.file.BoardFileService;
import com.eightbit.persistence.file.attach.article.FreeArticleAttachFileRepository;
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
@RequestMapping("/Files/attach/article/free/**")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class FreeArticleAttachFileController {

    private final BoardFileService boardFileService;

    private final FreeArticleAttachFileRepository freeArticleAttachFileRepository;

    private final TokenManager tokenManager;

    @GetMapping(value="/attaches/{uploader}/{regdate}") //Files/attach/article/free/attatches
    public ResponseEntity<List<UploadFile>> getFreeArticleAttachFileList(@PathVariable String uploader, @PathVariable String regdate, UploadFile uploadFile){
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        return  ResponseEntity.ok().body(freeArticleAttachFileRepository.getAttachFileList(uploadFile));
    }

    @GetMapping(value = "/attach/{id}/{uploader}/{regdate}")  //Files/attach/article/free/attach
    public ResponseEntity<Resource> downloadFreeArticleAttachFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                                       @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);
        uploadFile= freeArticleAttachFileRepository.getAttachFile(uploadFile);
        String storeFilename=uploadFile.getStoreFilename();
        String uploadFilename=uploadFile.getUploadFilename();
        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";

        regdate=regdate.replace(":","");

        UrlResource resource=new UrlResource("file:"+filepath+uploader+"/article/free/"+regdate+"/sharefiles/"+storeFilename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @PostMapping(value = "/files") //Files/attach/article/free/files
    @Transactional
    public void insertFreeArticleShareFiles(MultipartHttpServletRequest request, @RequestParam(value ="writer") String writer,
                                        @RequestParam(value="regdate") String regdate,
                                        @RequestParam(value="files") List<MultipartFile> files,
                                        String token, @Value("${file.dir}") String dir) throws IOException, ServletException {


        if(tokenManager.checkAccessToken(request, token, writer)) {
            List<UploadFile> sharefiles= boardFileService.registerBoardFiles(writer, regdate, files, dir, "article", "free", "sharefiles");
            for(UploadFile sharefile : sharefiles){
                freeArticleAttachFileRepository.registerArticleShareFile(sharefile);
            }
        }
    }

    @DeleteMapping(value="/files") //Files/attach/article/free/files
    @Transactional
    public void deleteFreeArticleShareFiles(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){

            for(UploadFile deleteFile : deleteFileList){
                if (freeArticleAttachFileRepository.removeArticleShareFile(deleteFile)) {
                    boardFileService.removeBoardFile(deleteFile, "article", "free", "sharefiles");
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
