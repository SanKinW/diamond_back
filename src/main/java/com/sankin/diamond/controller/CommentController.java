package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.CommentDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.service.CommentService;
import com.sankin.diamond.service.DocsService;
import com.sankin.diamond.service.NotificationService;
import com.sankin.diamond.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DocsService docsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsersService usersService;

    /**
     * 评论
     * @param commentDTO
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/comment/{userId}", method = RequestMethod.POST)
    public Object Comment(@RequestBody CommentDTO commentDTO,
                          @PathVariable("userId") Integer userId,
                          HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Users user = usersService.selectById(userId);
        commentService.insertOne(commentDTO, user);
        docsService.incComment(commentDTO.getDocId());
        notificationService.newComment(commentDTO, userId);
        return ResultDTO.okOf();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/commentLike/{commentId}", method = RequestMethod.GET)
    public Object incLike(@PathVariable("commentId") Integer commentId) {
        commentService.incLike(commentId);
        return ResultDTO.okOf();
    }
}
