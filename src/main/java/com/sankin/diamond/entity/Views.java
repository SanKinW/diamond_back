package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "views")
public class Views {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer viewerId;
    private Integer docId;
    private String docTitle;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp latestTime;
}
