package com.eightbit.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    private String reporter;
    private String master;
    private String regdate;
    private String report;
    private int depth;
    private String contentType;
}
