package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ViewDTO {
    private Integer docId;
    private String docName;
    private Timestamp latestTime;
}
