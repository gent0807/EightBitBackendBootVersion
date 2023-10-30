package com.eightbit.persistence.report;

import com.eightbit.entity.report.Report;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(Report report){
        return mybatis.selectOne("ReportMyBatisDAO.getReportCount", report);
    }


    public void insertReport(Report report){
        mybatis.insert("ReportMyBatisDAO.insertReport", report);
    }


    public void deleteReport(Report report){
        mybatis.delete("ReportMyBatisDAO.deleteReport", report);
    }
}
