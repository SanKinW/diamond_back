package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotificationDTO {
    private Integer notifier;
    private String notifierName;
    private Integer receiver;
    private String receiverName;
    private Integer type;
    private Timestamp createTime;
    private Integer status;
    private Integer outerId;
    private String outerTitle;
    private Integer completed;
}
