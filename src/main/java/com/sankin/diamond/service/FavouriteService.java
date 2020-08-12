package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Favourites;
import com.sankin.diamond.mapper.FavouriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteMapper favouriteMapper;

    public void createOne(UserDTO user, Integer id, String title) {
        Favourites favourites = new Favourites();
        favourites.setCollectorId(user.getId());
        favourites.setDocId(id);
        favourites.setCollectTime(new Timestamp(new Date().getTime()));
        favourites.setDocTitle(title);
        favouriteMapper.insert(favourites);
    }

    public Integer findByOne(UserDTO user, Integer id) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("collector_id", user.getId());
        columnMap.put("doc_id", id);
        List<Favourites> favourites = favouriteMapper.selectByMap(columnMap);
        if (favourites == null || favourites.size() == 0) return 0;
        return 1;

    }

    public IPage<Favourites> selectByPage(UserDTO user, Integer page) {
        QueryWrapper<Favourites> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("collector_id", user.getId());
        Page<Favourites> pages = new Page<>(page, 6);
        IPage<Favourites> favouritesIPage = favouriteMapper.selectPage(pages, queryWrapper);
        return favouritesIPage;
    }

    public void cancelOne(Integer doc_id, Integer user_id) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("doc_id",doc_id);
        columnMap.put("collector_id",user_id);
        favouriteMapper.deleteByMap(columnMap);
    }
}
