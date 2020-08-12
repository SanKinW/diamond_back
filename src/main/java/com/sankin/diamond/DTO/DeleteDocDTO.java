package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeleteDocDTO {
    private Integer id;
    private String title;
    private Timestamp updateTime;
}
