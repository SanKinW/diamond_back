package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.NotificationService;
import com.sankin.diamond.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TeamService teamService;
    /**
     * 邀请别人加入团队
     * @param teamId
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/invite/{teamId}/{userName}", method = RequestMethod.GET)
    public Object inviteUser(@PathVariable("teamId") Integer teamId,
                             @PathVariable("userName") String userName) {
        boolean check = teamService.checkIn(teamId, userName);
        if (check) {
            notificationService.TeamInvite(teamId, userName);
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(ErrorType.USER_HAVE_ENTER);
    }

    /**
     * 拒绝邀请
     * @param teamId
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/refuseInvitation/{teamId}/{userName}", method = RequestMethod.GET)
    public Object inviteUserReturn(@PathVariable("teamId") Integer teamId,
                             @PathVariable("userName") String userName) {
        notificationService.refuse(teamId, userName, 9);
        return ResultDTO.okOf();
    }

    /**
     * 申请加入团队
     * @param teamId
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/apply/{teamId}/{userName}", method = RequestMethod.GET)
    public Object joinTeamByOwn(@PathVariable("teamId") Integer teamId,
                                @PathVariable("userName") String userName) {
        boolean check = teamService.checkIn(teamId, userName);
        if (check) {
            notificationService.joinTeam(teamId, userName);
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(ErrorType.USER_HAVE_ENTER);
    }

    /**
     * 拒绝申请
     * @param teamId
     * @param userName
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/refuseApply/{teamId}/{userName}", method = RequestMethod.GET)
    public Object joinTeamByOwnReturn(@PathVariable("teamId") Integer teamId,
                                @PathVariable("userName") String userName) {
        notificationService.refuse(teamId, userName, 10);
        return ResultDTO.okOf();
    }
}
