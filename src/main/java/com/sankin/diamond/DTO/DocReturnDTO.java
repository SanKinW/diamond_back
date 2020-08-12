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
    private Integer collected;
    private List<Comments> comments;
}
