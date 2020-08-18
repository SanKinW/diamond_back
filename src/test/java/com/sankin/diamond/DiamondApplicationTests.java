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

    @Autowired
    private NotificationService notificationService;

    @Test
    void contextLoads() {
        notificationService.refuse(1, "Moon", 10);
    }

    @Test
    void testMyCode() {

    }
}
