package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sankin.diamond.DTO.ViewDTO;
import com.sankin.diamond.entity.Views;
import com.sankin.diamond.mapper.DocMapper;
import com.sankin.diamond.mapper.ViewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ViewService {
    @Autowired
    private ViewsMapper viewsMapper;

    @Autowired
    private DocMapper docMapper;

    public void createOrUpdate(Integer user_id, Integer doc_id, String title) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("viewer_id", user_id);
        columnMap.put("doc_id", doc_id);
        List<Views> views = viewsMapper.selectByMap(columnMap);
        Views view = new Views();
        view.setViewerId(user_id);
        view.setDocId(doc_id);
        view.setDocTitle(title);
        view.setLatestTime(new Timestamp(new Date().getTime()));
        if (views == null || views.size() == 0) {
            viewsMapper.insert(view);
        }
        else {
            view.setId(views.get(0).getId());
            viewsMapper.updateById(view);
        }
    }

    public List<Views> selectById(Integer id) {
        QueryWrapper<Views> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("viewer_id", id).orderByDesc("latest_time").last("limit 20");
        List<Views> viewsList = viewsMapper.selectList(queryWrapper);
        return viewsList;
    }
}
