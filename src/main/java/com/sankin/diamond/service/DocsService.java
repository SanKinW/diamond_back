package com.sankin.diamond.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sankin.diamond.DTO.*;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Team;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.mapper.DocMapper;
import com.sankin.diamond.mapper.TeamMapper;
import com.sankin.diamond.mapper.UsersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;


@Service
public class DocsService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private TeamMapper teamMapper;

    public int insertOne(Docs docs) {
        docs.setCreateTime(new Timestamp(new Date().getTime()));
        docs.setUpdateTime(new Timestamp(new Date().getTime()));
        int result =  docMapper.insert(docs);
        return result;
    }

    public int updateOne(Docs docs) {
        docs.setUpdateTime(new Timestamp(new Date().getTime()));
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

    public Page<Docs> selectByPage(Integer userId, Integer page) {
        QueryWrapper<Docs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", userId).eq("deleted",0);
        Page<Docs> docs = new Page<>(page, 6);
        Page<Docs> docsPage = docMapper.selectPage(docs, queryWrapper);
        return docsPage;
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

    public void setNum(SmallDocDTO smallDocDTO, Integer viewed, Integer commented, Integer shared, Integer modified) {
        smallDocDTO.setViewed(viewed);
        smallDocDTO.setCommented(commented);
        smallDocDTO.setShared(shared);
        smallDocDTO.setModified(modified);
    }

    public void setAuthority(SmallDocDTO smallDocDTO, Docs doc, Integer userId) {
        System.out.println("userId="+userId);
        Team team = teamMapper.selectById(doc.getTeamId());
        String[] members = team.getMembers().split(",");
        for(int i = 0; i < members.length; ++i) {
            int NUM = Integer.parseInt(members[i]);
            System.out.println("teamMemberId=" + NUM);
            if (NUM == userId) {
                setNum(smallDocDTO, 1, 1, 1, 1);
                return;
            }
        }
        int authority = doc.getAuthority();
        int num[] = new int[4];
        int count = 0;
        while (authority != 0) {
            num[count] = authority&1;
            count++;
            authority = authority >> 1;
        }
        setNum(smallDocDTO, num[0], num[1], num[2], num[3]);
    }

    public List<SmallDocDTO> selectByTeamId(Integer id, Integer userId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("team_id", id);
        columnMap.put("deleted", 0);
        List<Docs> docs = docMapper.selectByMap(columnMap);
        List<SmallDocDTO> smallDocDTOS = new ArrayList<>();
        for (Docs doc : docs) {
            SmallDocDTO smallDocDTO = new SmallDocDTO();
            smallDocDTO.setId(doc.getId());
            smallDocDTO.setTitle(doc.getTitle());
            Users user = usersMapper.selectById(doc.getCreator());
            smallDocDTO.setCreatorName(user.getUserName());
            setAuthority(smallDocDTO, doc, userId);
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

    public void resetAuthority(Integer docId, Integer weight) {
        Docs doc = docMapper.selectById(docId);
        doc.setAuthority(weight);
        docMapper.updateById(doc);
    }

    public int updateEdited(Integer docId) {
        Docs doc = docMapper.selectById(docId);
        if (doc.getEdited() == 1) return 0;
        doc.setEdited(1);
        docMapper.updateById(doc);
        return 1;
    }

    public void deleteAll(Integer userId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("creator", userId);
        columnMap.put("deleted", 1);
        docMapper.deleteByMap(columnMap);
    }

    public void deleteComplete(Integer docId) {
        docMapper.deleteById(docId);
    }

    public void updateCreator(int docCreator, Integer teamId, int teamCreator) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("creator", docCreator);
        columnMap.put("team_id", teamId);
        List<Docs> docs = docMapper.selectByMap(columnMap);
        for (Docs doc : docs) {
            doc.setCreator(teamCreator);
            docMapper.updateById(doc);
        }
    }

    public void updateAuthority(Integer docId, Integer authority) {
        Docs doc = docMapper.selectById(docId);
        doc.setAuthority(authority);
        docMapper.updateById(doc);
    }
}
