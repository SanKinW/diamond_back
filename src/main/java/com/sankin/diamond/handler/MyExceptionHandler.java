package com.sankin.diamond.handler;

import com.alibaba.fastjson.JSON;
import com.sankin.diamond.DTO.ResultDTO;
import com.sankin.diamond.exception.ErrorException;
import com.sankin.diamond.exception.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    void handle(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        ResultDTO resultDTO = null;
        if ("application/json".equals(contentType)) {
            if (ex instanceof ErrorException) {
                resultDTO =  ResultDTO.errorOf((ErrorException) ex);
            } else {
                resultDTO = ResultDTO.errorOf(ErrorType.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (ex instanceof ErrorException) {
                resultDTO = ResultDTO.errorOf((ErrorException) ex);
            } else {
               resultDTO = ResultDTO.errorOf(ErrorType.SYSTEM_ERROR.getType(), ErrorType.SYSTEM_ERROR.getMessage());
            }
            try {
                response.setStatus(resultDTO.getType());
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
