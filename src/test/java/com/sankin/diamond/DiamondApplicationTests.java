package com.sankin.diamond;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sankin.diamond.DTO.*;
import com.sankin.diamond.entity.*;
import com.sankin.diamond.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DiamondApplicationTests {
    @Autowired
    private UsersService usersService;

    @Autowired
    private DocsService docsService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TeamService teamService;

    @Test
    void contextLoads() {
        List<SmallDocDTO> smallDocDTOS = docsService.selectByTeamId(1);
        System.out.println(smallDocDTOS);
    }

    @Test
    void testMyCode() {
        Team team = new Team();
        team.setTeamName("test3");
        team.setBasicInformation("create for test3");
        team.setCreator(2);
        team.setMembers("2");
        teamService.insertOne(team);
        int teamId = teamService.selectByTime(2);
        usersService.updateTeams(2,teamId);
    }
}
