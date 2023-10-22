package com.eightbit.biz.board.vo;

import lombok.Data;

@Data
public class ReplyVO {
    private int id;
    private int seq;
    private String original_writer;
    private String original_regdate;
    private String replyer;
    private String content;
    private String regdate;
    private int likecount;
    private String updatedate;
    private String[] nomalfiles;
    private String[] sharefiles;

    public ReplyVO(String replyer, String regdate){
        this.replyer=replyer;
        this.regdate=regdate;
    }
}
