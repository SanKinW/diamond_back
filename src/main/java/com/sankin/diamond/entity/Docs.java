package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "docs")
public class Docs {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer creator;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer teamId;
    private Integer commentCount;
    private Integer collectCount;
    private Integer authority;
    private Integer deleted;
    private Integer updatedId;
}
