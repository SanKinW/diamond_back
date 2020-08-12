package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class InformationDTO {
    private String userName;
    private String password;
    private String token;
    private String avatarUrl;
    private String sex;
    private Date birthday;
    private String telephone;
    private String email;
}
