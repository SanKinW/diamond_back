package com.sankin.diamond.interceptor;

import com.sankin.diamond.DTO.SmallTeamDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.UsersMapper;
import com.sankin.diamond.service.NotificationService;
import com.sankin.diamond.service.TeamService;
import com.sankin.diamond.service.UsersService;
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
    private UsersService usersService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TeamService teamService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    List<Users> users = usersService.selectByMap(token);
                    if (users.size() != 0) {
                        Users user = users.get(0);
                        UserDTO userDTO = new UserDTO();
                        BeanUtils.copyProperties(user,userDTO);
                        List<SmallTeamDTO> teamDTOS = teamService.setBasicByIds(user.getTeamIds());
                        usersService.setNameByIds(teamDTOS);
                        userDTO.setTeams(teamDTOS);
                        userDTO.setUnRead(notificationService.unRead(user.getId()));
                        request.getSession().setAttribute("user", userDTO);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
