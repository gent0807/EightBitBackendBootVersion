package com.eightbit.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReport {
    private String reporter;
    private String replyer;
    private String regdate;
    private String report;
}
