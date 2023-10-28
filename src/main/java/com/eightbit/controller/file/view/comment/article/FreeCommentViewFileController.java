package com.eightbit.controller.file.view.comment.article;

import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.inter.file.BoardFileService;
import com.eightbit.inter.file.CommentFileService;
import com.eightbit.persistence.comment.article.FreeCommentRepository;
import com.eightbit.persistence.file.view.comment.article.FreeCommentViewFileRepository;
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
@RequestMapping("/Files/view/article/free/comment/*")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class FreeCommentViewFileController {
    private final CommentFileService commentFileService;

    private final FreeCommentViewFileRepository freeCommentViewFileRepository;

    private final FreeCommentRepository freeCommentRepository;

    private final TokenManager tokenManager;

    @GetMapping(value="/views/{uploader}/{regdate}")
    public ResponseEntity<List<UploadFile>> getFreeCommentViewFileList(@PathVariable String uploader, @PathVariable String regdate, UploadFile uploadFile){
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        return  ResponseEntity.ok().body(freeCommentViewFileRepository.getViewFileList(uploadFile));
    }

    @GetMapping(value = "/view/{id}/{uploader}/{regdate}")
    public ResponseEntity<Resource> downloadFreeCommentViewFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate,
                                                              @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {

        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setId(id);

        Comment comment= freeCommentRepository.getOriginWriterAndRegdate(uploadFile);

        uploadFile=freeCommentViewFileRepository.getViewFile(uploadFile);

        String storeFilename=uploadFile.getStoreFilename();
        String uploadFilename=uploadFile.getUploadFilename();
        String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
        String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";

        regdate=regdate.replace(":","");

        UrlResource resource=new UrlResource("file:"+filepath+comment.getOriginal_author()+"/article/free/"+comment.getOriginal_regdate()+"/comment/"+uploader+"/"+regdate+"/viewfiles/"+storeFilename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @PostMapping(value = "/files")
    @Transactional
    public void insertFreeCommentViewFiles(MultipartHttpServletRequest request, @RequestParam(value ="author") String commenter,
                                           @RequestParam(value="regdate") String regdate,
                                           @RequestParam(value="files") List<MultipartFile> files,
                                           String token, @Value("${file.dir}") String dir, UploadFile uploadFile) throws IOException, ServletException {


        if(tokenManager.checkAccessToken(request, token, commenter)) {
            uploadFile.setUploader(commenter);
            uploadFile.setRegdate(regdate);
            Comment comment=freeCommentRepository.getOriginWriterAndRegdate(uploadFile);
            List<UploadFile> viewfiles=commentFileService.registerCommentFiles(commenter, regdate, comment.getOriginal_author(), comment.getOriginal_regdate(), files, dir, "article", "free", "viewfiles");
            for(UploadFile viewfile : viewfiles){
                freeCommentViewFileRepository.registerCommentViewFile(viewfile);
            }
        }
    }

    @DeleteMapping(value="/files")
    @Transactional
    public void deleteFreeCommentViewFiles(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){
            for(UploadFile deleteFile : deleteFileList){
                Comment comment= freeCommentRepository.getOriginWriterAndRegdate(deleteFile);
                if (freeCommentViewFileRepository.removeCommentViewFile(deleteFile)) {
                    commentFileService.removeCommentFile(deleteFile, comment.getOriginal_author(), comment.getOriginal_regdate(),"article", "free", "viewfiles");
                }
            }
        }
    }

}
