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
import com.sankin.diamond.service.*;
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

    @Autowired
    private UsersService usersService;

    /**
     * 查看文档
     * @param id
     * @param title
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/doc/{id}/{title}/{userId}", method = RequestMethod.GET)
    public Object viewDoc(@PathVariable("id") Integer id,
                          @PathVariable("title")String title,
                          @PathVariable("userId") Integer userId,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Users user = usersService.selectById(userId);
        DocReturnDTO returnDTO = new DocReturnDTO();
        Docs doc = docsService.selectOne(id);
        if (doc == null) {
            returnDTO.setResultDTO(ResultDTO.errorOf(ErrorType.DOC_DELETED));
            return returnDTO;
        }
        if(user != null) {
            if (doc.getCreator() == userId) {
                returnDTO.setShared(0);
                returnDTO.setCommented(0);
                returnDTO.setModified(1);
                returnDTO.setEdited(0);
                returnDTO.setResultDTO(ResultDTO.okOf());
            }
            else if (doc.getAuthority() == 0 && (doc.getTeamId() == null || doc.getTeamId() == 0)) {
                returnDTO.setResultDTO(ResultDTO.errorOf(ErrorType.READ_NOTIFICATION_FAILED));
                return returnDTO;
            }
            else {
                usersService.setAuthority(returnDTO, doc, user.getId());
            }
            viewService.createOrUpdate(user.getId(),id, title);
            returnDTO.setCollected(favouriteService.findByOne(user, id));
        }
        else {
            viewService.createOrUpdate(0,id, title);
            int viewed = doc.getAuthority() & 1;
            if (viewed == 1) {
                returnDTO.setCollected(0);
                returnDTO.setCommented(0);
                returnDTO.setShared(0);
                returnDTO.setModified(0);
                returnDTO.setEdited(0);
                returnDTO.setResultDTO(ResultDTO.okOf());
            }
            else {
                returnDTO.setResultDTO(ResultDTO.errorOf(ErrorType.READ_NOTIFICATION_FAILED));
                return returnDTO;
            }
        }
        BeanUtils.copyProperties(doc, returnDTO);
        String creatorName = usersService.getNameById(doc.getCreator());
        returnDTO.setCreatorName(creatorName);
        List<Comments> comments = commentService.selectByDocId(id);
        returnDTO.setComments(comments);
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
        else return ResultDTO.errorOf(ErrorType.PUBLISH_ERROR);
    }

    /**
     * 编辑文档前的状态查看
     * @param docId
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/docStatus/{docId}",method = RequestMethod.GET)
    public Object changeDocEdited(@PathVariable("docId") Integer docId) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        int result = docsService.updateEdited(docId);
        if (result == 1) return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.SOMEONE_EDITING);
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
        docs.setEdited(0);
        int result = docsService.updateOne(docs);
        if (result > 0) return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.UPDATE_FAILED);
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
