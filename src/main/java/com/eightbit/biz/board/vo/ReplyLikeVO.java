package com.eightbit.biz.board.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyLikeVO {
    private String liker;
    private String replyer;
    private String regdate;
}
