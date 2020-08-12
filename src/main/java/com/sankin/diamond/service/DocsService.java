package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sankin.diamond.DTO.DeleteDocDTO;
import com.sankin.diamond.DTO.SmallDocDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.DocMapper;
import com.sankin.diamond.mapper.UsersMapper;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DocsService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private UsersMapper usersMapper;


    public int insertOne(Docs docs) {
        int result =  docMapper.insert(docs);
        return result;
    }

    public int updateOne(Docs docs) {
        int result =  docMapper.updateById(docs);
        return result;
    }

    public Docs selectOne(Integer id) {
        QueryWrapper<Docs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("deleted",0);
        return docMapper.selectOne(queryWrapper);
    }

    public void deleteOne(Integer id) {
        Docs doc = docMapper.selectById(id);
        doc.setDeleted(1);
        docMapper.updateById(doc);
    }

    public IPage<Docs> selectByPage(UserDTO user, Integer page) {
        QueryWrapper<Docs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", user.getId()).select("id","title","collect_count","authority");
        Page<Docs> docs = new Page<>(page, 6);
        IPage<Docs> docsIPage = docMapper.selectPage(docs, queryWrapper);
        return docsIPage;
    }


    public List<DeleteDocDTO> selectDeletedByCreator(Integer id) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("creator", id);
        columnMap.put("deleted", 1);
        List<Docs> deleteDocs = docMapper.selectByMap(columnMap);
        List<DeleteDocDTO> deleteDocDTOS = new ArrayList<>();
        for (Docs doc: deleteDocs) {
            DeleteDocDTO docDTO = new DeleteDocDTO();
            BeanUtils.copyProperties(doc, docDTO);
            deleteDocDTOS.add(docDTO);
        }
        return deleteDocDTOS;
    }

    public void recoveryOne(Integer id) {
        Docs deleteDoc = docMapper.selectById(id);
        deleteDoc.setDeleted(0);
        docMapper.updateById(deleteDoc);
    }

    public List<SmallDocDTO> selectByTeamId(Integer id) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("team_id", id);
        List<Docs> docs = docMapper.selectByMap(columnMap);
        List<SmallDocDTO> smallDocDTOS = new ArrayList<>();
        for (Docs doc : docs) {
            SmallDocDTO smallDocDTO = new SmallDocDTO();
            smallDocDTO.setId(doc.getId());
            smallDocDTO.setTitle(doc.getTitle());
            Users user = usersMapper.selectById(doc.getCreator());
            smallDocDTO.setCreatorName(user.getUserName());
            smallDocDTOS.add(smallDocDTO);
        }
        return smallDocDTOS;
    }

    public void incComment(Integer docId) {
        Docs doc = docMapper.selectById(docId);
        int count = doc.getCommentCount() + 1;
        doc.setCommentCount(count);
        docMapper.updateById(doc);
    }

    public void incLike(Integer id) {
        Docs doc = docMapper.selectById(id);
        int count = doc.getCollectCount() + 1;
        doc.setCollectCount(count);
        docMapper.updateById(doc);
    }

    public void decLike(Integer id) {
        Docs doc = docMapper.selectById(id);
        int count = doc.getCollectCount() - 1;
        doc.setCollectCount(count);
        docMapper.updateById(doc);
    }

    public void updateTeam(Integer id) {
        Docs doc = new Docs();
        doc.setTeamId(0);
        UpdateWrapper<Docs> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("team_id",id);
        docMapper.update(doc, updateWrapper);
    }
}
