package com.sankin.diamond.service;

import com.sankin.diamond.DTO.TeamCheckDTO;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.TeamMapper;
import com.sankin.diamond.mapper.UsersMapper;
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
    }
}
