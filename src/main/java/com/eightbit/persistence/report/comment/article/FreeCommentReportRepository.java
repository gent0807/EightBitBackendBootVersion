package com.eightbit.persistence.report.comment.article;

import com.eightbit.entity.report.Report;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeCommentReportRepository {
    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(Report report){
        return mybatis.selectOne("FreeCommentReportMyBatisDAO.getReportCount", report);
    }


    public void insertReport(Report report){
        mybatis.insert("FreeCommentReportMyBatisDAO.insertReport", report);
    }


    public void deleteReport(Report report){
        mybatis.delete("FreeCommentReportMyBatisDAO.deleteReport", report);
    }
}
