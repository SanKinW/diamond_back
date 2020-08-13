package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.LogDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", logDTO.getUserName());
        List<Users> usersList = usersMapper.selectByMap(columnMap);
        if (usersList.size() != 0) return 0;
        Users users = new Users();
        users.setUserName(logDTO.getUserName());
        users.setPassword(logDTO.getPassword());
        int result = usersMapper.insert(users);
        return result;
    }

    public int modifyInformation(Users users) {
        int result = usersMapper.updateById(users);
        return result;
    }

    public List<String> selectByIds(String[] members) {
        List<Integer> ids = new ArrayList<>();
        for (String member : members) {
            Integer id = Integer.parseInt(member);
            ids.add(id);
        }
        List<Users> users = usersMapper.selectBatchIds(ids);
        List<String> userNames = new ArrayList<>();
        for (Users user: users) {
            userNames.add(user.getUserName());
        }
        return userNames;
    }

    public void dismiss(Integer id) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        String teamIds = "" + id;
        queryWrapper.like("team_ids",teamIds);
        List<Users> users = usersMapper.selectList(queryWrapper);
        for (Users user : users) {
            String[] ids = user.getTeamIds().split(",");
            String newIds = "";
            for(int i = 0; i < ids.length; ++i) {
                if (!ids[i].equals(teamIds)) {
                    if (i == 0) newIds = newIds + ids[i];
                    else newIds = newIds + "," + ids[i];
                }
            }
            user.setTeamIds(newIds);
            usersMapper.updateById(user);
        }
    }

    public Users selectById(int i) {
        return usersMapper.selectById(i);
    }
}
