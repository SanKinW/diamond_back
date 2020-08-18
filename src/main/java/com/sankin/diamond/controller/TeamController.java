package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.*;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.DocsService;
import com.sankin.diamond.service.NotificationService;
import com.sankin.diamond.service.TeamService;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private DocsService docsService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 创建团队
     * @param teamCreateDTO
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/team/{userId}", method = RequestMethod.POST)
    public Object createTeam(@RequestBody TeamCreateDTO teamCreateDTO,
                             @PathVariable("userId") Integer userId,
                             HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Team team = new Team();
        BeanUtils.copyProperties(teamCreateDTO, team);
        team.setCreator(userId);
        String member = "" + userId;
        team.setMembers(member);
        int result = teamService.insertOne(team);
        if (result > 0) {
            int teamId = teamService.selectByTime(userId);
            usersService.updateTeams(userId,teamId);
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(ErrorType.TEAM_CREATE_FAILED);
    }

    /**
     * 解散团队
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/deleteTeam/{id}", method = RequestMethod.DELETE)
    public Object deletedTeam(@PathVariable("id") Integer id) {
        docsService.updateTeam(id);
        usersService.dismiss(id);
        Team team = teamService.selectById(id);
        notificationService.teamDismiss(team);
        teamService.deleteById(id);
        return ResultDTO.okOf();
    }


    /**
     * 查看团队信息
     * @param teamId
     * @param userId
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/team/{teamId}/{userId}", method = RequestMethod.GET)
    public TeamReturnDTO teamInformation(@PathVariable("teamId")Integer teamId,
                                         @PathVariable("userId") Integer userId,
                                         HttpServletRequest request) {
        Team team = teamService.selectById(teamId);
        TeamReturnDTO returnDTO = new TeamReturnDTO();
        BeanUtils.copyProperties(team, returnDTO);
        String[] members = team.getMembers().split(",");
        List<String> memberName = usersService.selectByIds(members);
        returnDTO.setMembersName(memberName);
        returnDTO.setCreatorName(usersService.getNameById(team.getCreator()));
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<SmallDocDTO> docDTOs = new ArrayList<>();
        //if (user != null) docDTOs = docsService.selectByTeamId(docId, user.getId());
        docDTOs = docsService.selectByTeamId(teamId, userId);
        returnDTO.setDocs(docDTOs);
        return returnDTO;
    }

    /**
     * 修改文档权限
     * @param authority
     * @param docId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/modifyAuthority/{docId}/{authority}", method = RequestMethod.GET)
    public Object modifyAuthority(@PathVariable("authority") Integer authority,
                                  @PathVariable("docId") Integer docId) {
        docsService.updateAuthority(docId, authority);
        return ResultDTO.okOf();
    }


    /**
     * 加入团队
     * @param teamId
     * @param userId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/join/{teamId}/{userId}", method = RequestMethod.GET)
    public Object joinTeam(@PathVariable("teamId") Integer teamId,
                           @PathVariable("userId") Integer userId,
                           HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        if (user != null) {
            if (user.getId() == userId) notificationService.joinReturn(userId, teamId, 7);
            else notificationService.joinReturn(userId, teamId, 8);
        }
        int result = teamService.updateMembers(teamId, userId);
        if (result > 0) {
            usersService.updateTeams(userId, teamId);
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(ErrorType.TEAM_JOIN_FAILED);
    }

    /**
     * 退出团队
     * @param teamId
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/quit/{teamId}/{userName}", method = RequestMethod.GET)
    public Object quitTeam(@PathVariable("teamId") Integer teamId,
                           @PathVariable("userName") String userName,
                           HttpServletRequest request) {
        usersService.quitTeam(userName, teamId);
        teamService.clearUser(userName, teamId);
        int teamCreator = teamService.getCreatorById(teamId);
        int docCreator = usersService.getIdByName(userName);
        docsService.updateCreator(docCreator, teamId, teamCreator);
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        if (user.getUserName().equals(userName)) notificationService.quit(teamId, userName, 3);
        else notificationService.quit(teamId, userName, 4);
        return ResultDTO.okOf();
    }

    /**
     * 设置文档权限
     * @param docId
     * @param weight
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/authority/{docId}/{weight}", method = RequestMethod.GET)
    public Object setAuthority(@PathVariable("docId") Integer docId,
                               @PathVariable("weight") Integer weight) {
        docsService.resetAuthority(docId, weight);
        return ResultDTO.okOf();
    }

    /**
     * 搜索团队
     * @param teamName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/search/{teamName}", method = RequestMethod.GET)
    public List<TeamReturnDTO> searchTeam(@PathVariable("teamName") String teamName) {
        List<TeamReturnDTO> teamReturnDTOS = teamService.selectByTeamName(teamName);
        return teamReturnDTOS;
    }
}
