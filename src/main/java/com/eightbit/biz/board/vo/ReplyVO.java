package com.eightbit.biz.board.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVO {
    private int id;
    private int seq;
    private String original_writer;
    private String original_regdate;
    private String replyer;
    private String content;
    private String regdate;
    private int likecount;
    private int recomment_count;
    private String updatedate;
    private String[] nomalfiles;
    private String[] sharefiles;

    public ReplyVO(String replyer, String regdate){
        this.replyer=replyer;
        this.regdate=regdate;
    }
}
