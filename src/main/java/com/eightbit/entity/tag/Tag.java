package com.eightbit.entity.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    private int id;
    private String tagger;
    private String regdate;
    private String tag;
    private String contentType;
}
