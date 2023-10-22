package com.eightbit.biz.board.vo;

import lombok.Data;

@Data
public class ReCommentVO {
    private int id;
    private int seq;
    private String original_replyer;
    private String original_regdate;
    private String reCommenter;
    private String content;
    private String regdate;
    private String updatedate;
    private String[] nomalfiles;
    private String[] sharefiles;

    public ReCommentVO(String reCommenter, String regdate){
        this.reCommenter=reCommenter;
        this.regdate=regdate;
    }
}
