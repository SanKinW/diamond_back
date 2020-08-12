package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private Integer id;
    private String userName;
    private String password;
    private String token;
    private String avatarUrl;
    private String sex;
    private Date birthday;
    private String telephone;
    private String email;
    private List<Integer> teams;
}
