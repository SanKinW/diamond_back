package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "notification")
public class Notification {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer type;
    private Timestamp createTime;
    private Integer status;
    private Integer outerId;
    private String outerTitle;
}
