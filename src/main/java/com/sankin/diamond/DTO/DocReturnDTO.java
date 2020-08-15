package com.sankin.diamond.DTO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.sankin.diamond.entity.Comments;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DocReturnDTO {
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
    private Integer updatedId;
    private Integer collected;
    private List<Comments> comments;
    private Integer commented;//可评论
    private Integer shared;//可分享
    private Integer modified;//可修改
    private Integer edited;//此时可编辑，避免编辑冲突
    private ResultDTO resultDTO;//返回状态
}
