package com.eightbit.persistence.report.article;

import com.eightbit.entity.report.Report;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeArticleReportRepository {

    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(Report report){
        return mybatis.selectOne("FreeArticleReportMyBatisDAO.getReportCount", report);
    }


    public void insertReport(Report report){
        mybatis.insert("FreeArticleReportMyBatisDAO.insertReport", report);
    }


    public void deleteReport(Report report){
        mybatis.delete("FreeArticleReportMyBatisDAO.deleteReport", report);
    }
}