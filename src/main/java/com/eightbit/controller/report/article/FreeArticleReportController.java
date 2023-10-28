package com.eightbit.controller.report.article;

import com.eightbit.entity.report.Report;
import com.eightbit.persistence.report.article.FreeArticleReportRepository;
import com.eightbit.impl.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/Reports/article/free/*")
@RequiredArgsConstructor
public class FreeArticleReportController {

    private final FreeArticleReportRepository reportRepository;

    private final TokenManager tokenManager;

    @PostMapping("/report/count")
    public ResponseEntity<Integer> getReportCount(HttpServletRequest request, String token, @RequestBody Report report){
        if(tokenManager.checkAccessToken(request, token, report.getReporter())){
            System.out.println(report.getReporter());
            System.out.println(report.getAuthor());
            System.out.println(report.getRegdate());
            System.out.println(report.getReport());
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
