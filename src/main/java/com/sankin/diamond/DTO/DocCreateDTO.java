package com.sankin.diamond.DTO;

import lombok.Data;


@Data
public class DocCreateDTO {
    private String title;
    private String content;
    private Integer teamId;
    private Integer authority;
}
