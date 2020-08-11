package com.sankin.diamond.exception;

public class ErrorException extends RuntimeException{
    private Integer type;
    private String message;

    public ErrorException(ErrorCode errorCode) {
        this.type = errorCode.getType();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Integer getType() {
        return type;
    }
}
