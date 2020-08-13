package com.sankin.diamond;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sankin.diamond.DTO.CommentDTO;
import com.sankin.diamond.DTO.DocReturnDTO;
import com.sankin.diamond.DTO.LogDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Favourites;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.entity.Views;
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

    @Test
    void contextLoads() {
        Users user = usersService.selectById(1);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("niu");
        commentDTO.setDocId(1);
        commentService.insertOne(commentDTO, userDTO);
    }

    @Test
    void testMyCode() {
        Docs doc = new Docs();
        doc.setTitle("dog");
        doc.setCreator(1);
        doc.setContent("so faith!");
        doc.setTeamId(0);
        doc.setAuthority(1);
        docsService.insertOne(doc);
    }
}
