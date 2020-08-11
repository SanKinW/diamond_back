package com.sankin.diamond.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;


@Data
@TableName(value = "users")
public class Users {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String token;
    private String avatarUrl;
    private String sex;
    private Date birthday;
    private String telephone;
    private String email;
    private String teamIds;
}
