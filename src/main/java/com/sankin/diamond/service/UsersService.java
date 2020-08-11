package com.sankin.diamond.service;

import com.sankin.diamond.DTO.LogDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;

    public Users checkLog(LogDTO logDTO) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name",logDTO.getUserName());
        columnMap.put("password", logDTO.getPassword());
        List<Users> usersList = usersMapper.selectByMap(columnMap);
        if (usersList.size() == 1) return usersList.get(0);
        else return null;
    }

    public void updateByOne(Users result) {
        usersMapper.updateById(result);
    }

    public int insertOne(LogDTO logDTO) {
        Users users = new Users();
        users.setUserName(logDTO.getUserName());
        users.setPassword(logDTO.getPassword());
        int result = usersMapper.insert(users);
        return result;
    }

    public void modifyInformation(Users users) {
        usersMapper.updateById(users);
    }
}
