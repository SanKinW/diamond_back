package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.TeamCreateDTO;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.TeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    /**
     * 创建团队
     * @param teamCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public Object createTeam(@RequestBody TeamCreateDTO teamCreateDTO, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        Team team = new Team();
        BeanUtils.copyProperties(teamCreateDTO, team);
        team.setCreator(user.getId());
        String member = "" + user.getId();
        team.setMembers(member);
        int result = teamService.insertOne(team);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.TEAM_CREATE_FAILED));
    }
}
