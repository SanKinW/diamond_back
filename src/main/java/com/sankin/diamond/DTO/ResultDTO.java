package com.sankin.diamond.DTO;

import com.sankin.diamond.exception.ErrorCode;
import com.sankin.diamond.exception.ErrorException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer type;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer type, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setType(type);
        resultDTO.setMessage(message);
        return resultDTO;
    }


    public static ResultDTO errorOf(ErrorCode errorCode) {
        return errorOf(errorCode.getType(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(ErrorException e) {
        return errorOf(e.getType(), e.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setType(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setType(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
