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
        int authority = 1;
        int num[] = new int[4];
        int count = 0;
        while (authority != 0) {
            num[count] = authority&1;
            count++;
            authority = authority >> 1;
        }
        System.out.println(num[0]);
        System.out.println(num[1]);
        System.out.println(num[2]);
        System.out.println(num[3]);
    }

    @Test
    void testMyCode() {
        Users user = new Users();
        user.setSex("女");
        user.setId(1);
        user.setUserName("SanKinW");
        usersService.modifyInformation(user);
    }
}
