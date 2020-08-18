package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.InformationDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
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
     * 查看个人信息
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public Object getInformation(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        return user;
    }

    /**
     * 修改个人信息
     * @param informationDTO
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/information", method = RequestMethod.POST)
    public Object changeInformation(@RequestBody InformationDTO informationDTO, HttpServletRequest request) {
        Users users = new Users();
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        BeanUtils.copyProperties(informationDTO,users);
        users.setId(user.getId());
        int result = usersService.modifyInformation(users);
        if (result > 0) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(users, userDTO);
            userDTO.setTeams(user.getTeams());
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(ErrorType.NAME_REPEAT);
    }
}
