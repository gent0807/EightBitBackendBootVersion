package com.eightbit.entity.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private int seq;
    private String original_author;
    private String original_regdate;
    private String author;
    private String content;
    private String regdate;
    private int likecount;
    private int recomment_count;
    private String updatedate;
    private int depth;
    private String contentType;

    public Comment(String author, String regdate){
        this.author=author;
        this.regdate=regdate;
    }
}
