package com.eightbit.persistence.report.article;

import com.eightbit.entity.report.ArticleReport;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeArticleReportRepository {

    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(ArticleReport report){
        return mybatis.selectOne("FreeArticleReportMyBatisDAO.getReportCount", report);
    }

    public void insertReport(ArticleReport report){
        mybatis.insert("FreeArticleReportMyBatisDAO.insertReport", report);
    }

    public void deleteReport(ArticleReport report){
        mybatis.delete("FreeArticleReportMyBatisDAO.deleteReport", report);
    }
}