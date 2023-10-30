package com.eightbit.entity.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {
    private String liker;
    private String master;
    private String regdate;
    private int depth;
    private String contentType;
}
