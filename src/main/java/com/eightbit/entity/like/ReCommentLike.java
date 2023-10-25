package com.eightbit.entity.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReCommentLike {
    private String liker;
    private String reCommenter;
    private String regdate;
}
