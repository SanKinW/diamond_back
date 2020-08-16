package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.*;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Notification;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.mapper.TeamMapper;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private TeamMapper teamMapper;

    public Users checkLog(LogDTO logDTO) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name",logDTO.getUserName());
        columnMap.put("password", logDTO.getPassword());
        List<Users> usersList = usersMapper.selectByMap(columnMap);
        if (usersList.size() == 1) return usersList.get(0);
        else return null;
    }

    public void updateByOne(Users result) {
        usersMapper.updateById(result);
    }

    public int insertOne(LogDTO logDTO) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", logDTO.getUserName());
        List<Users> usersList = usersMapper.selectByMap(columnMap);
        if (usersList.size() != 0) return 0;
        Users users = new Users();
        users.setUserName(logDTO.getUserName());
        users.setPassword(logDTO.getPassword());
        int result = usersMapper.insert(users);
        return result;
    }

    public int modifyInformation(Users users) {
        int result = usersMapper.updateById(users);
        return result;
    }

    public List<String> selectByIds(String[] members) {
        List<Integer> ids = new ArrayList<>();
        for (String member : members) {
            Integer id = Integer.parseInt(member);
            ids.add(id);
        }
        List<Users> users = usersMapper.selectBatchIds(ids);
        List<String> userNames = new ArrayList<>();
        for (Users user: users) {
            userNames.add(user.getUserName());
        }
        return userNames;
    }

    public void dismiss(Integer id) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        String teamIds = "" + id;
        queryWrapper.like("team_ids",teamIds);
        List<Users> users = usersMapper.selectList(queryWrapper);
        for (Users user : users) {
            String[] ids = user.getTeamIds().split(",");
            String newIds = "";
            for(int i = 0; i < ids.length; ++i) {
                if (!ids[i].equals(teamIds)) {
                    if (i == 0) newIds = newIds + ids[i];
                    else newIds = newIds + "," + ids[i];
                }
            }
            user.setTeamIds(newIds);
            usersMapper.updateById(user);
        }
    }

    public Users selectById(int i) {
        return usersMapper.selectById(i);
    }

    public void updateTeams(Integer userId, Integer teamId) {
        Users user = usersMapper.selectById(userId);
        String teamIds = user.getTeamIds();
        if (teamId == null || teamId.equals("")) {
            teamIds = "" + teamId;
        }
        else {
            teamIds = teamIds + "," + teamId;
        }
        user.setTeamIds(teamIds);
        usersMapper.updateById(user);
    }

    public void quitTeam(String userName, Integer teamId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        Users user = usersMapper.selectByMap(columnMap).get(0);
        String[] teamIds = user.getTeamIds().split(",");
        String newIds = "";
        for(int i = 0; i < teamIds.length; ++i) {
            if (!teamIds[i].equals(teamId)) {
                if (i == 0) newIds = newIds + teamIds[i];
                else newIds = newIds + "," + teamIds[i];
            }
        }
        user.setTeamIds(newIds);
        usersMapper.updateById(user);
    }

    public List<NotificationDTO> addName(List<Notification> notifications) {
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification:notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            String notifierName = usersMapper.selectById(notification.getNotifier()).getUserName();
            String receiverName = usersMapper.selectById(notification.getReceiver()).getUserName();
            notificationDTO.setNotifierName(notifierName);
            notificationDTO.setReceiverName(receiverName);
            notificationDTOS.add(notificationDTO);
        }
        return notificationDTOS;
    }

    public void setDocAuthority(DocReturnDTO returnDTO, Integer commented, Integer shared, Integer modified) {
        returnDTO.setCommented(commented);
        returnDTO.setShared(shared);
        returnDTO.setModified(modified);
    }

    public void setAuthority(DocReturnDTO returnDTO, Docs doc, Integer userId) {
        if (doc.getTeamId() != null && doc.getTeamId() > 0) {
            Team team = teamMapper.selectById(doc.getTeamId());
            String[] members = team.getMembers().split(",");
            for(int i = 0; i < members.length; ++i) {
                int num = Integer.parseInt(members[i]);
                if (num == userId) setDocAuthority(returnDTO, 1, 1, 1);
                returnDTO.setResultDTO(ResultDTO.okOf());
                return;
            }
        }
        int authority = doc.getAuthority();
        int num[] = new int[4];
        int count = 0;
        while (authority != 0) {
            num[count] = authority&1;
            count++;
            authority = authority >> 1;
        }
        setDocAuthority(returnDTO, num[1], num[2], num[3]);
        if (num[0] == 0) returnDTO.setResultDTO(ResultDTO.errorOf(ErrorType.READ_NOTIFICATION_FAILED));
        else returnDTO.setResultDTO(ResultDTO.okOf());
    }

    public String getNameById(Integer creator) {
        Users user = usersMapper.selectById(creator);
        return user.getUserName();
    }

    public List<Users> selectByMap(String token) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("token", token);
        return usersMapper.selectByMap(columnMap);
    }

    public void setNameByIds(List<SmallTeamDTO> teamDTOS) {
        for (SmallTeamDTO teamDTO : teamDTOS) {
            Users user = usersMapper.selectById(teamDTO.getCreator());
            teamDTO.setCreatorName(user.getUserName());
        }
    }
}
