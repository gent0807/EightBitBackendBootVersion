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
    private String author;
    private String regdate;
    private String report;
}
