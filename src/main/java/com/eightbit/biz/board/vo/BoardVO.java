package com.eightbit.biz.board.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVO {
    private int id;
    private int seq;
    private String title;
    private String content;
    private String writer;
    private String role;
    private String regdate;
    private String updatedate;
    private int visitcnt;
    private int likecount;
    private int reply_count;
    private int attach_count;
}