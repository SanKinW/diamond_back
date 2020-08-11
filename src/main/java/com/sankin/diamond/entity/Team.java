package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "team")
public class Team {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String teamName;
    private String basicInformation;
    private Integer creator;
    private String members;
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;
}
