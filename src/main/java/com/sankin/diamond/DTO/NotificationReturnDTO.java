package com.sankin.diamond.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NotificationReturnDTO {
    private List<NotificationDTO> notificationDTOS;
    private Long totalPages;//总页数
    private Long totalNotifications;//总记录数
    private Long currentPage;//当前页码
    private boolean hasNext;//是否有上一页
    private boolean hasPrevious;//是否有下一页
}
