package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

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
        notificationService.TeamInvite(teamId, userName);
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
        notificationService.joinTeam(teamId, userName);
        return ResultDTO.okOf();
    }


}
