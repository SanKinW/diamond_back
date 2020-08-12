package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.CommentDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Comments;
import com.sankin.diamond.mapper.CommentsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentsMapper commentsMapper;

    public void insertOne(CommentDTO commentDTO, UserDTO users) {
        Comments comment = new Comments();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setCommentator(users.getId());
        comment.setCommentatorName(users.getUserName());
        comment.setCommentTime(new Timestamp(new Date().getTime()));
        comment.setLikeCount(0);
        commentsMapper.insert(comment);
    }

    public List<Comments> selectByDocId(Integer id) {
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doc_id",id);
        return commentsMapper.selectList(queryWrapper);
    }
}
