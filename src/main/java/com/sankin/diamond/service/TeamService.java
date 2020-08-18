package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.SmallTeamDTO;
import com.sankin.diamond.DTO.TeamCheckDTO;
import com.sankin.diamond.DTO.TeamReturnDTO;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.TeamMapper;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TeamService {
    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UsersMapper usersMapper;


    public int insertOne(Team team) {
        team.setCreateTime(new Timestamp(new Date().getTime()));
        int result = teamMapper.insert(team);
        return result;
    }

    public List<TeamCheckDTO> selectByIds(List<Integer> teams) {
        List<Team> teamList = teamMapper.selectBatchIds(teams);
        List<TeamCheckDTO> checkDTOS = new ArrayList<>();
        for (Team team: teamList) {
            TeamCheckDTO teamCheckDTO = new TeamCheckDTO();
            teamCheckDTO.setId(team.getId());
            teamCheckDTO.setTeamName(team.getTeamName());
            checkDTOS.add(teamCheckDTO);
        }
        return checkDTOS;
    }

    public Team selectById(Integer id) {
        return teamMapper.selectById(id);
    }


    public int updateMembers(Integer teamId, Integer userId) {
        Team team = teamMapper.selectById(teamId);
        String members = team.getMembers() + "," +userId;
        team.setMembers(members);
        return teamMapper.updateById(team);
    }

    public void deleteById(Integer id) {
        teamMapper.deleteById(id);
    }

    public void clearUser(String userName, Integer teamId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        Users user = usersMapper.selectByMap(columnMap).get(0);
        Team team = teamMapper.selectById(teamId);
        String memberId = "" + user.getId();
        String[] memberIds = team.getMembers().split(",");
        String newIds = "";
        int count = 0;
        for(int i = 0; i < memberIds.length; ++i) {
            if (!memberIds[i].equals(memberId)) {
                if (count == 0) newIds = newIds + memberIds[i];
                else newIds = newIds + "," + memberIds[i];
                count++;
            }
        }
        team.setMembers(newIds);
        teamMapper.updateById(team);
    }

    public List<TeamReturnDTO> selectByTeamName(String teamName) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("team_name", teamName);
        List<Team> teams = teamMapper.selectList(queryWrapper);
        List<TeamReturnDTO> teamReturnDTOS = new ArrayList<>();
        for (Team team:teams) {
            TeamReturnDTO teamReturnDTO = new TeamReturnDTO();
            BeanUtils.copyProperties(team, teamReturnDTO);
            teamReturnDTOS.add(teamReturnDTO);
        }
        return teamReturnDTOS;
    }

    public List<SmallTeamDTO> setBasicByIds(String teamIds) {
        List<SmallTeamDTO> teamDTOS = new ArrayList<>();
        String regex = ",";
        if (teamIds == null || teamIds.equals("")) return null;
        String[] temp = teamIds.split(regex);
        for (String id:temp) {
            Integer teamId = Integer.parseInt(id);
            Team team = teamMapper.selectById(teamId);
            SmallTeamDTO smallTeamDTO = new SmallTeamDTO();
            BeanUtils.copyProperties(team, smallTeamDTO);
            teamDTOS.add(smallTeamDTO);
        }
        return teamDTOS;
    }

    public int selectByTime(Integer userId) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", userId).orderByDesc("create_time");
        List<Team> teams = teamMapper.selectList(queryWrapper);
        return teams.get(0).getId();
    }

    public int getCreatorById(Integer teamId) {
        return teamMapper.selectById(teamId).getCreator();
    }

    public boolean checkIn(Integer teamId, String userName) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_name", userName);
        Users user = usersMapper.selectByMap(columnMap).get(0);
        String userId = "" + user.getId();
        Team team = selectById(teamId);
        String[] members = team.getMembers().split(",");
        for (String member : members) {
            if (member.equals(userId)) return true;
        }
        return false;
    }
}
