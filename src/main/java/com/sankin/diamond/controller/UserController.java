package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.InformationDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.SmallTeamDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.NotificationService;
import com.sankin.diamond.service.TeamService;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:SanKinW
 * @since:2020.8.11
 */
@RestController
public class UserController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 查看个人信息
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/information/{userName}", method = RequestMethod.GET)
    public Object getInformation(@PathVariable("userName") String userName) {
        Users user = usersService.selectByName(userName);
        return user;
    }

    /**
     * 修改个人信息
     * @param informationDTO
     * @param userId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/information/{userId}", method = RequestMethod.POST)
    public Object changeInformation(@RequestBody InformationDTO informationDTO,
                                    @PathVariable("userId") Integer userId) {
        Users user = usersService.selectById(userId);
        BeanUtils.copyProperties(informationDTO,user);
        int result = usersService.modifyInformation(user);
        if (result > 0) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            List<SmallTeamDTO> teamDTOS = teamService.setBasicByIds(user.getTeamIds());
            usersService.setNameByIds(teamDTOS);
            userDTO.setTeams(teamDTOS);
            userDTO.setUnRead(notificationService.unRead(user.getId()));
            ResultDTO resultDTO =  ResultDTO.okOf();
            resultDTO.setData(userDTO);
            return resultDTO;
        }
        else return ResultDTO.errorOf(ErrorType.NAME_REPEAT);
    }
}
