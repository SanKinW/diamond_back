package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.InformationDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
/**
 * @author:SanKinW
 * @since:2020.8.11
 */
@RestController
public class UserController {
    @Autowired
    private UsersService usersService;

    /**
     * 修改个人信息
     * @param informationDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/information", method = RequestMethod.POST)
    public Object changeInformation(@RequestBody InformationDTO informationDTO, HttpServletRequest request) {
        Users users = new Users();
        Users user = (Users) request.getSession().getAttribute("user");
        BeanUtils.copyProperties(informationDTO,users);
        users.setId(user.getId());
        usersService.modifyInformation(users);
        return ResultDTO.okOf();
    }
}
