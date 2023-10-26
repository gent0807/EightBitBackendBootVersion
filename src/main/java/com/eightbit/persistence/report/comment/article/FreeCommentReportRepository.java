package com.eightbit.persistence.report.comment.article;

import com.eightbit.controller.file.attach.game.IndieGameAttachController;
import com.eightbit.entity.report.ArticleReport;
import com.eightbit.entity.report.CommentReport;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeCommentReportRepository {
    private final SqlSessionTemplate mybatis;

    public Integer getReportCount(CommentReport report){
        return mybatis.selectOne("FreeCommentReportMyBatisDAO.getReportCount", report);
    }

    public void insertReport(CommentReport report){
        mybatis.insert("FreeCommentReportMyBatisDAO.insertReport", report);
    }

    public void deleteReport(CommentReport report){
        mybatis.delete("FreeCommentReportMyBatisDAO.deleteReport", report);
    }
}
