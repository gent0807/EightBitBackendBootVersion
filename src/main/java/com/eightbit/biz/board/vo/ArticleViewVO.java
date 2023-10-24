package com.eightbit.biz.board.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleViewVO {
    private String viewer;
    private String writer;
    private String regdate;
}
