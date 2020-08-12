package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "comments")
public class Comments {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer docId;
    private Integer commentator;
    private String commentatorName;
    private Timestamp commentTime;
    private Integer likeCount;
}
