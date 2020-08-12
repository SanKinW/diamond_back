package com.sankin.diamond.interceptor;

import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    Map<String, Object> columnMap = new HashMap<>();
                    columnMap.put("token", token);
                    List<Users> users = usersMapper.selectByMap(columnMap);
                    if (users.size() != 0) {
                        Users user = users.get(0);
                        UserDTO userDTO = new UserDTO();
                        /*userDTO.setId(user.getId());
                        userDTO.setUserName(user.getUserName());
                        userDTO.setPassword(user.getPassword());
                        userDTO.setAvatarUrl(user.getAvatarUrl());
                        userDTO.setBirthday(user.getBirthday());
                        userDTO.setSex(user.getSex());
                        userDTO.setToken(user.getToken());
                        userDTO.setEmail(user.getEmail());
                        userDTO.setTelephone(user.getTelephone());*/
                        BeanUtils.copyProperties(user,userDTO);
                        String regex = ",";
                        List<Integer> ids = new ArrayList<>();
                        String[] temp = user.getTeamIds().split(regex);
                        for (String id:temp) {
                            Integer num = Integer.parseInt(id);
                            ids.add(num);
                        }
                        userDTO.setTeams(ids);
                        request.getSession().setAttribute("user", userDTO);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
