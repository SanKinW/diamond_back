package com.sankin.diamond.service;

import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.mapper.DocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocsService {

    @Autowired
    private DocMapper docMapper;

    public int insertOne(Docs docs) {
        int result =  docMapper.insert(docs);
        return result;
    }

    public int updateOne(Docs docs) {
        int result =  docMapper.updateById(docs);
        return result;
    }
}
