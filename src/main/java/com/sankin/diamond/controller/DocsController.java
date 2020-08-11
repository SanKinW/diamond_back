package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.DocCreateDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.DocsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:SanKinW
 * @since:2020.8.11
 */
@RestController
public class DocsController {
    @Autowired
    private DocsService docsService;

    /**
     * 创建新文档
     * @param docCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doc", method = RequestMethod.POST)
    public Object createDoc(@RequestBody DocCreateDTO docCreateDTO, HttpServletRequest request) {
        Users user = (Users) request.getSession().getAttribute("user");
        Docs docs = new Docs();
        BeanUtils.copyProperties(docCreateDTO, docs);
        docs.setCreator(user.getId());
        int result = docsService.insertOne(docs);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.PUBLISH_ERROR));
    }

    /**
     * 更新文档
     * @param docCreateDTO
     * @param request
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/doc/{id}",method = RequestMethod.POST)
    private Object updateDoc(@RequestBody DocCreateDTO docCreateDTO,
                             HttpServletRequest request,
                             @PathVariable("id") Integer id) {
        Users user = (Users) request.getSession().getAttribute("user");
        Docs docs = new Docs();
        BeanUtils.copyProperties(docCreateDTO, docs);
        docs.setId(id);
        int result = docsService.updateOne(docs);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.UPDATE_FAILED));
    }
}
