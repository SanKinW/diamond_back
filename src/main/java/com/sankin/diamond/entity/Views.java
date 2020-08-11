package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "views")
public class Views {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer viewerId;
    private Integer docId;
    private Timestamp latestTime;
}
