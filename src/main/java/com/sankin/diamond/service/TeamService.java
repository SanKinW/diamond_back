package com.sankin.diamond.service;

import com.sankin.diamond.entity.Team;
import com.sankin.diamond.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    private TeamMapper teamMapper;


    public int insertOne(Team team) {
        int result = teamMapper.insert(team);
        return result;
    }
}
