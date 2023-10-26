package com.eightbit.controller.report.article;

import com.eightbit.entity.report.ArticleReport;
import com.eightbit.entity.report.CommentReport;
import com.eightbit.entity.user.User;
import com.eightbit.persistence.report.article.FreeArticleReportRepository;
import com.eightbit.util.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Integer> getReportCount(HttpServletRequest request, String token, @RequestBody ArticleReport report){
        if(tokenManager.checkAccessToken(request, token, report.getReporter())){
            System.out.println(report.getReporter());
            System.out.println(report.getWriter());
            System.out.println(report.getRegdate());
            System.out.println(report.getReport());
            return ResponseEntity.ok().body(reportRepository.getReportCount(report));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/report")
    public void insertReport(HttpServletRequest request, String token, @RequestBody ArticleReport report){
        if(tokenManager.checkAccessToken(request, token, report.getReporter())){
            reportRepository.insertReport(report);
        }
    }

    @DeleteMapping(value = "/report/{nickname}")
    public void deleteReport(HttpServletRequest request, String token, String nickname, @RequestBody ArticleReport report){
        if(tokenManager.checkAccessToken(request,token,nickname)){
            reportRepository.deleteReport(report);
        }
    }
}
