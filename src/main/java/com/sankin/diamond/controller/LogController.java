package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.LogDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author:SanKinW
 * @since:2020.8.11
 */
@RestController
public class LogController {
    @Autowired
    private UsersService usersService;

    /**
     * 注册功能
     * @param logDTO
     * @param response
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(@RequestBody LogDTO logDTO, HttpServletResponse response) {
        int result = usersService.insertOne(logDTO);
        if (result > 0) {
            Users users = new Users();
            users.setId(result);
            String token = UUID.randomUUID().toString();
            users.setToken(token);
            usersService.updateByOne(users);
            response.addCookie(new Cookie("token",token));
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(new ErrorException(ErrorType.NAME_REPEAT));
    }

    /**
     * 登录功能
     * @param logDTO
     * @param response
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody LogDTO logDTO, HttpServletResponse response){
        System.out.println("begin");
        Users result = usersService.checkLog(logDTO);
        if (result != null) {
            String token = UUID.randomUUID().toString();
            result.setToken(token);
            usersService.updateByOne(result);
            response.addCookie(new Cookie("token",token));
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(new ErrorException(ErrorType.INFORMATION_ERROR));
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "您已退出登录";
    }
}
