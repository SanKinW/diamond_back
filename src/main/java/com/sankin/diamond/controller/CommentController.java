package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.CommentDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.service.CommentService;
import com.sankin.diamond.service.DocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DocsService docsService;

    /**
     * 评论
     * @param commentDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object Comment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        commentService.insertOne(commentDTO, user);
        docsService.incComment(commentDTO.getDocId());
        return ResultDTO.okOf();
    }


}
