package com.sankin.diamond.DTO;

import lombok.Data;

@Data
public class SmallDocDTO {
    private Integer id;
    private String title;
    private String creatorName;
    private Integer viewed;//可查看
    private Integer commented;//可评论
    private Integer shared;//可分享
    private Integer modified;//可修改
}
