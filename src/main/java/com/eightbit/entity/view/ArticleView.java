package com.eightbit.entity.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleView {
    private String viewer;
    private String writer;
    private String regdate;
    private String contentType;
}
