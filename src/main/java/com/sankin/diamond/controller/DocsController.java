package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.DocCreateDTO;
import com.sankin.diamond.DTO.DocReturnDTO;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.entity.Comments;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import com.sankin.diamond.service.CommentService;
import com.sankin.diamond.service.DocsService;
import com.sankin.diamond.service.FavouriteService;
import com.sankin.diamond.service.ViewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author:SanKinW
 * @since:2020.8.11
 */
@RestController
public class DocsController {
    @Autowired
    private DocsService docsService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private CommentService commentService;

    /**
     * 查看文档
     * @param id
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/doc/{id}/{title}", method = RequestMethod.GET)
    public Object viewDoc(@PathVariable("id") Integer id,
                          @PathVariable("title")String title,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        DocReturnDTO returnDTO = new DocReturnDTO();
        Docs doc = docsService.selectOne(id);
        BeanUtils.copyProperties(doc, returnDTO);
        if(user != null) {
            viewService.createOrUpdate(user.getId(),id, title);
            returnDTO.setCollected(favouriteService.findByOne(user, id));
        }
        else {
            viewService.createOrUpdate(0,id, title);
            returnDTO.setCollected(0);
        }
        List<Comments> comments = commentService.selectByDocId(id);
        returnDTO.setComments(comments);
        /**
         * 增加一下权限
         */
        System.out.println(doc.getTitle());
        return returnDTO;
    }

    /**
     * 创建新文档
     * @param docCreateDTO
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/doc/{userId}", method = RequestMethod.POST)
    public Object createDoc(@RequestBody DocCreateDTO docCreateDTO,
                            @PathVariable("userId") Integer userId,
                            HttpServletRequest request) {
        //Users user = (Users) request.getSession().getAttribute("user");
        Docs docs = new Docs();
        BeanUtils.copyProperties(docCreateDTO, docs);
        docs.setCreator(userId);
        int result = docsService.insertOne(docs);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.PUBLISH_ERROR));
    }

    /**
     * 更新文档
     * @param docCreateDTO
     * @param request
     * @param docId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/doc/{docId}/{userId}",method = RequestMethod.POST)
    public Object updateDoc(@RequestBody DocCreateDTO docCreateDTO,
                             HttpServletRequest request,
                             @PathVariable("docId") Integer docId,
                             @PathVariable("userId") Integer userId) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Docs docs = new Docs();
        BeanUtils.copyProperties(docCreateDTO, docs);
        docs.setId(docId);
        docs.setUpdatedId(docId);
        int result = docsService.updateOne(docs);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(new ErrorException(ErrorType.UPDATE_FAILED));
    }

    /**
     * 收藏文档
     * @param docId
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/collect/{docId}/{title}/{userId}",method = RequestMethod.GET)
    public Object collectDoc(@PathVariable("docId") Integer docId,
                             @PathVariable("title")String title,
                             @PathVariable("userId") Integer userId,
                             HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        favouriteService.createOne(userId, docId, title);
        docsService.incLike(docId);
        return ResultDTO.okOf();
    }

    /**
     * 取消收藏
     * @param docId
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/cancel/{docId}/{userId}",method = RequestMethod.GET)
    public Object cancelCollectDoc(@PathVariable("docId") Integer docId,
                                   @PathVariable("userId") Integer userId,
                                   HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        favouriteService.cancelOne(docId, userId);
        docsService.decLike(docId);
        return ResultDTO.okOf();
    }
    /**
     * 删除文档
     * @param id
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public Object deleteDoc(@PathVariable("id") Integer id) {
        docsService.deleteOne(id);
        return ResultDTO.okOf();
    }

    /**
     * 恢复文档
     * @param id
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/recovery/{id}",method = RequestMethod.PUT)
    public Object recoveryDoc(@PathVariable("id") Integer id) {
        docsService.recoveryOne(id);
        return ResultDTO.okOf();
    }

}
