package com.eightbit.entity.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    private int id;
    private int seq;
    private String title;
    private String content;
    private String developer;
    private String role;
    private String regdate;
    private String updatedate;
    private String genre;
    private String url;
    private int visitcnt;
    private int likecount;
    private int reply_count;
    private int pcGameCount;
    private int mobilGameCount;
    private int imgCount;
    private int bannerCount;
    private int depth;
    private String contentType;
}
