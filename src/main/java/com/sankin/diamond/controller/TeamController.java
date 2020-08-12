package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.*;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.DocsService;
import com.sankin.diamond.service.TeamService;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private DocsService docsService;

    /**
     * 创建团队
     * @param teamCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public Object createTeam(@RequestBody TeamCreateDTO teamCreateDTO, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Team team = new Team();
        BeanUtils.copyProperties(teamCreateDTO, team);
        team.setCreator(user.getId());
        String member = "" + user.getId();
        team.setMembers(member);
        int result = teamService.insertOne(team);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.TEAM_CREATE_FAILED));
    }

    /**
     * 解散团队
     */
    @ResponseBody
    @RequestMapping(value = "/deleteTeam/{id}", method = RequestMethod.DELETE)
    public Object deletedTeam(@PathVariable("id") Integer id) {
        docsService.updateTeam(id);
        usersService.dismiss(id);
        teamService.deleteById(id);
        return ResultDTO.okOf();
    }


    /**
     * 查看团队信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/team/{id}", method = RequestMethod.GET)
    public TeamReturnDTO teamInformation(@PathVariable("id")Integer id) {
        Team team = teamService.selectById(id);
        TeamReturnDTO returnDTO = new TeamReturnDTO();
        BeanUtils.copyProperties(team, returnDTO);
        String[] members = team.getMembers().split(",");
        List<String> memberName = usersService.selectByIds(members);
        returnDTO.setMembersName(memberName);
        List<SmallDocDTO> docDTOS = docsService.selectByTeamId(id);
        returnDTO.setDocs(docDTOS);
        return returnDTO;
    }

    /**
     * 加入团队
     * @param teamId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/join/{teamId}/{userId}", method = RequestMethod.GET)
    public Object joinTeam(@PathVariable("teamId") Integer teamId, @PathVariable("userId") Integer userId) {
        int result = teamService.updateMembers(teamId, userId);
        if (result > 0) {
            /**
             * 用户的团队列表中添加
             */
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(new ErrorException(ErrorType.TEAM_JOIN_FAILED));
    }


}
