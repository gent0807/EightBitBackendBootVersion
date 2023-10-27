package com.eightbit.persistence.report.recomment.article;


import com.eightbit.entity.report.Report;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeReCommentReportRepository {
    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(Report report){
        return mybatis.selectOne("FreeReCommentReportMyBatisDAO.getReportCount", report);
    }


    public void insertReport(Report report){
        mybatis.insert("FreeReCommentReportMyBatisDAO.insertReport", report);
    }


    public void deleteReport(Report report){
        mybatis.delete("FreeReCommentReportMyBatisDAO.deleteReport", report);
    }
}
