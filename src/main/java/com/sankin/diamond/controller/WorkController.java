package com.sankin.diamond.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sankin.diamond.DTO.DeleteDocDTO;
import com.sankin.diamond.DTO.TeamCheckDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.DTO.ViewDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Favourites;
import com.sankin.diamond.entity.Users;
import com.sankin.diamond.entity.Views;
import com.sankin.diamond.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : SanKinW
 * @since : 2020.8.12
 * 工作台
 */
@RestController
public class WorkController {
    @Autowired
    private DocsService docsService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UsersService usersService;

    /**
     * 最近浏览文档
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/views/{userId}", method = RequestMethod.GET)
    public List<Views> browsingRecord(@PathVariable("userId") Integer userId, HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<Views> views = viewService.selectById(userId);
        return views;
    }

    /**
     * 收藏的文档
     * @param page
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/collect/{page}/{userId}", method = RequestMethod.GET)
    public Page<Favourites> favouriteRecord(@PathVariable("page") Integer page,
                                            @PathVariable("userId") Integer userId,
                                            HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Page<Favourites> favourites = favouriteService.selectByPage(userId, page);
        return favourites;
    }

    /**
     * 自己创建的文档
     * @param page
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/mydoc/{page}/{userId}", method = RequestMethod.GET)
    public Page<Docs> myDocRecord(@PathVariable("page")Integer page,
                                  @PathVariable("userId") Integer userId,
                                  HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Page<Docs> docs = docsService.selectByPage(userId, page);
        return docs;
    }

    /**
     * 加入的团队
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/myteam/{userId}", method = RequestMethod.GET)
    public List<TeamCheckDTO> myTeamRecord(@PathVariable("userId") Integer userId,HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Users user = usersService.selectById(userId);
        String regex = ",";
        List<Integer> ids = new ArrayList<>();
        String[] temp = user.getTeamIds().split(regex);
        for (String id:temp) {
            Integer num = Integer.parseInt(id);
            ids.add(num);
        }
        List<TeamCheckDTO> checkDTOS = teamService.selectByIds(ids);
        return checkDTOS;
    }

    /**
     * 回收站
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/recycle/{userId}", method = RequestMethod.GET)
    public List<DeleteDocDTO> recycle(@PathVariable("userId") Integer userId,
                                      HttpServletRequest request) {
        //UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<DeleteDocDTO> deleteDocDTOS = docsService.selectDeletedByCreator(userId);
        return deleteDocDTOS;
    }
}
