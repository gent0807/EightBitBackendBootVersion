package com.eightbit.entity.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Read {
    private String reader;
    private String master;
    private String regdate;
    private String readDate;
    private String contentType;
}
