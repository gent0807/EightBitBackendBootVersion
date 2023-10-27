package com.eightbit.controller.report.comment.article;

import com.eightbit.entity.report.Report;
import com.eightbit.persistence.report.comment.article.FreeCommentReportRepository;
import com.eightbit.util.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/Reports/comment/free/*")
@RequiredArgsConstructor
public class FreeCommentReportController {

    private final FreeCommentReportRepository reportRepository;

    private final TokenManager tokenManager;

    @PostMapping("/report/count")
    public ResponseEntity<Integer> getReportCount(HttpServletRequest request, String token, @RequestBody Report report){
        if(tokenManager.checkAccessToken(request, token, report.getReporter())){
            return ResponseEntity.ok().body(reportRepository.getReportCount(report));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/report")
    @Transactional
    public void insertReport(HttpServletRequest request, String token, @RequestBody Report report){
        if(tokenManager.checkAccessToken(request, token, report.getReporter())){
            reportRepository.insertReport(report);
        }
    }

    @DeleteMapping(value = "/report/{nickname}")
    @Transactional
    public void deleteReport(HttpServletRequest request, String token, String nickname, @RequestBody Report report){
        if(tokenManager.checkAccessToken(request,token,nickname)){
            reportRepository.deleteReport(report);
        }
    }
}
