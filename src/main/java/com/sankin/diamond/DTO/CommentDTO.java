package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDTO {
    private String content;
    private Integer docId;
}
