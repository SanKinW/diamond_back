package com.sankin.diamond.service;

import com.sankin.diamond.DTO.TeamCheckDTO;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamMapper teamMapper;


    public int insertOne(Team team) {
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
}
