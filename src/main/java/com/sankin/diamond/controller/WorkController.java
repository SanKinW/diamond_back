package com.sankin.diamond.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sankin.diamond.DTO.DeleteDocDTO;
import com.sankin.diamond.DTO.TeamCheckDTO;
import com.sankin.diamond.DTO.UserDTO;
import com.sankin.diamond.DTO.ViewDTO;
import com.sankin.diamond.entity.Docs;
import com.sankin.diamond.entity.Favourites;
import com.sankin.diamond.entity.Views;
import com.sankin.diamond.service.DocsService;
import com.sankin.diamond.service.FavouriteService;
import com.sankin.diamond.service.TeamService;
import com.sankin.diamond.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 最近浏览文档
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/views", method = RequestMethod.GET)
    public List<Views> browsingRecord(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<Views> views = viewService.selectById(user.getId());
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
    @RequestMapping(value = "/collect/{page}", method = RequestMethod.GET)
    public Page<Favourites> favouriteRecord(@PathVariable("page") Integer page, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Page<Favourites> favourites = favouriteService.selectByPage(user, page);
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
    @RequestMapping(value = "/mydoc/{page}", method = RequestMethod.GET)
    public Page<Docs> myDocRecord(@PathVariable("page")Integer page, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        Page<Docs> docs = docsService.selectByPage(user, page);
        return docs;
    }

    /**
     * 加入的团队
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/myteam", method = RequestMethod.GET)
    public List<TeamCheckDTO> myTeamRecord(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<TeamCheckDTO> checkDTOS = teamService.selectByIds(user.getTeams());
        return checkDTOS;
    }

    /**
     * 回收站
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/recycle", method = RequestMethod.GET)
    public List<DeleteDocDTO> recycle(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<DeleteDocDTO> deleteDocDTOS = docsService.selectDeletedByCreator(user.getId());
        return deleteDocDTOS;
    }
}
