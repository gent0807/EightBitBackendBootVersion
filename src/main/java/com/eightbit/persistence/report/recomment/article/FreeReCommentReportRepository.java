package com.eightbit.persistence.report.recomment.article;


import com.eightbit.entity.report.ArticleReport;
import com.eightbit.entity.report.ReCommentReport;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeReCommentReportRepository {
    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(ReCommentReport report){
        return mybatis.selectOne("FreeReCommentReportMyBatisDAO.getReportCount", report);
    }

    public void insertReport(ReCommentReport report){
        mybatis.insert("FreeReCommentReportMyBatisDAO.insertReport", report);
    }

    public void deleteReport(ReCommentReport report){
        mybatis.delete("FreeReCommentReportMyBatisDAO.deleteReport", report);
    }
}
