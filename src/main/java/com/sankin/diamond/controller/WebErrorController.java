package com.sankin.diamond.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class WebErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(HttpServletRequest request, Model model) {
        /*HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            model.addAttribute("message","您的请求出错了，要不换个姿势吧");
        }
        if (status.is5xxServerError()) {
            model.addAttribute("message","服务器冒烟了，请稍后再试！");
        }*/
        return "404 Not Found";
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
