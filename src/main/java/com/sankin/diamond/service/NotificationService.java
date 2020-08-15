package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.CommentDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Notification;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.DocMapper;
import com.sankin.diamond.mapper.NotificationMapper;
import com.sankin.diamond.mapper.TeamMapper;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private DocMapper docMapper;

    public void insertOne(Integer notifier, Integer receiver, Integer type, Integer outerId, String outerTitle) {
        Notification notification = new Notification();
        notification.setNotifier(notifier);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setStatus(0);
        notification.setCreateTime(new Timestamp(new Date().getTime()));
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public void TeamInvite(Integer teamId, String userName) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        List<Users> users = usersMapper.selectByMap(columnMap);
        Team team = teamMapper.selectById(teamId);
        insertOne(team.getCreator(), users.get(0).getId(), 1, teamId, team.getTeamName());
    }

    public void joinTeam(Integer teamId, String userName) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        List<Users> users = usersMapper.selectByMap(columnMap);
        Team team = teamMapper.selectById(teamId);
        insertOne(users.get(0).getId(), team.getCreator(), 2, teamId, team.getTeamName());
    }


    public void quit(Integer teamId, String userName, Integer type) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        Users user = usersMapper.selectByMap(columnMap).get(0);
        Team team = teamMapper.selectById(teamId);
        insertOne(0, user.getId(), type, teamId, team.getTeamName());
    }

    public void newComment(CommentDTO commentDTO, Integer userId) {
        Docs doc = docMapper.selectById(commentDTO.getDocId());
        Users receiver = usersMapper.selectById(doc.getCreator());
        insertOne(userId, receiver.getId(), 5, doc.getId(), doc.getTitle());
    }

    public Integer unRead(Integer id) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver", id).eq("status",0);
        return notificationMapper.selectCount(queryWrapper);
    }

    public List<Notification> selectByReceiver(Integer userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver", userId).eq("status",0).orderByDesc("create_time");
        List<Notification> notifications = notificationMapper.selectList(queryWrapper);
        for (Notification notification:notifications) {
            notification.setStatus(1);
            notificationMapper.updateById(notification);
        }
        return notifications;
    }
}
