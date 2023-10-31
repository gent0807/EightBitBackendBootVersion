package com.eightbit.controller.file;

import com.eightbit.entity.comment.Comment;
import com.eightbit.entity.file.UploadFile;
import com.eightbit.impl.file.FileServiceImpl;
import com.eightbit.impl.token.TokenManager;
import com.eightbit.persistence.comment.CommentRepository;
import com.eightbit.persistence.file.FileRepository;

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
@RequestMapping("/Files/**")
@RequiredArgsConstructor
@Slf4j
@Primary
@PropertySource("classpath:upload.properties")
public class FileController {
    private final FileServiceImpl fileService;

    private final FileRepository fileRepository;

    private final CommentRepository commentRepository;

    private final TokenManager tokenManager;


    @GetMapping(value="/files/{uploader}/{regdate}/{contentType}/{storeType}/{depth}")
    public ResponseEntity<List<UploadFile>> getFileList(@PathVariable String uploader, @PathVariable String regdate,
                                                        @PathVariable String contentType, @PathVariable String storeType,
                                                        @PathVariable int depth, UploadFile uploadFile){
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setContentType(contentType);
        uploadFile.setStoreType(storeType);
        uploadFile.setDepth(depth);
        return  ResponseEntity.ok().body(fileRepository.getFileList(uploadFile));
    }

    @GetMapping(value = "/file/{id}/{uploader}/{regdate}/{contentType}/{storeType}/{depth}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int id, @PathVariable String uploader, @PathVariable String regdate, @PathVariable String contentType,
                                                                  @PathVariable String storeType, @PathVariable int depth,
                                                                  @Value("${file.dir}") String filepath, UploadFile uploadFile) throws MalformedURLException {
        uploadFile.setId(id);
        uploadFile.setUploader(uploader);
        uploadFile.setRegdate(regdate);
        uploadFile.setContentType(contentType);
        uploadFile.setStoreType(storeType);
        uploadFile.setDepth(depth);

        uploadFile= fileRepository.getFile(uploadFile);

        String storeFilename=uploadFile.getStoreFilename();
        String uploadFilename=uploadFile.getUploadFilename();
        regdate=regdate.replace(":","");

        if(storeType.equals("attach")||storeType.equals("pcGame")||storeType.equals("mobileGame")){
            String encodedUploadFileName= UriUtils.encode(uploadFilename, StandardCharsets.UTF_8);
            String contentDisposition="attachment; filename=\""+ encodedUploadFileName +"\"";
            UrlResource resource=new UrlResource("file:"+filepath+uploader+"/"+contentType+"/"+regdate+"/"+storeType+"/"+storeFilename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        }
        else if(storeType.equals("gameBanner")||storeType.equals("gameImage")){
            return ResponseEntity.ok().body(new UrlResource("file:"+filepath+uploader+"/"+contentType+"/"+regdate+"/"+storeType+"/"+storeFilename));
        }
        else if(storeType.equals("view")){

            if(depth==1){
                return ResponseEntity.ok().body(new UrlResource("file:"+filepath+uploader+"/"+contentType+"/"+regdate+"/"+storeType+"/"+storeFilename));
            }

            else if(depth==2){
                Comment comment=commentRepository.getOriginAuthorAndRegdateFromUploadFile(uploadFile);
                return ResponseEntity.ok().body(new UrlResource("file:"+filepath+comment.getOriginal_author()+"/"+contentType+"/"+comment.getOriginal_regdate()+"/comment/"+uploader+"/"+regdate+"/"+storeType+"/"+storeFilename));
            }

            else if(depth==3){
                Comment comment=commentRepository.getOriginAuthorAndRegdateFromUploadFile(uploadFile);
                String original_commenter=comment.getOriginal_author();
                String original_comment_regdate=comment.getOriginal_regdate();
                comment=commentRepository.getOriginAuthorAndRegdateFromComment(comment);
                return ResponseEntity.ok().body(new UrlResource("file:"+filepath+comment.getOriginal_author()+
                        "/"+contentType+"/"+comment.getOriginal_regdate()+"/comment/"+original_commenter+"/"+original_comment_regdate+"/reComment/"
                        +uploader+"/"+regdate+"/"+storeType+"/"+storeFilename));
            }

        }

        return ResponseEntity.status(HttpStatus.resolve(404)).body(null);


    }

    @PostMapping(value = "/files/{contentType}/{depth}")
    @Transactional
    public void insertFiles(MultipartHttpServletRequest request, @RequestParam(value ="uploader") String uploader,
                                            @RequestParam(value="regdate") String regdate,
                                            @RequestParam(value="files") List<MultipartFile> files,
                                            String token,
                                            @PathVariable(value = "contentType") String contentType,
                                            @RequestParam(value = "storeType") String storeType,
                                            @PathVariable(value = "depth") int depth) throws IOException, ServletException {


        if(tokenManager.checkAccessToken(request, token, uploader)) {
            List<UploadFile> uploadFiles= fileService.registerFiles(uploader, regdate, files, contentType, storeType, depth);
            for(UploadFile uploadFile : uploadFiles){
                fileRepository.registerFile(uploadFile);
            }
        }
    }

    @DeleteMapping(value="/files/{contentType}/{depth}")
    @Transactional
    public void deleteFiles(HttpServletRequest request, String token, @RequestBody List<UploadFile> deleteFileList){
        if(tokenManager.checkAccessToken(request, token, deleteFileList.get(0).getUploader())){

            for(UploadFile deleteFile : deleteFileList){
                if (fileService.removeFile(deleteFile)) {
                    fileRepository.removeFile(deleteFile);
                }
            }
        }
    }




}
