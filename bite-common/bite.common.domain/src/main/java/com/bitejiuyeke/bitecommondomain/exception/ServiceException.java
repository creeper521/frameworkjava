package com.bitejiuyeke.bitecommondomain.exception;

import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException{

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    //共三种方式来完成异常的构造

    /**
     * 构造函数
     * @param resultCode
     */
    public ServiceException(ResultCode resultCode){
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    /**
     * 构造函数
     * @param message
     */
    public ServiceException(String message){
        this.code = ResultCode.ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造函数
     * @param code
     * @param message
     */
    public ServiceException(String message, int code){
        this.code = code;
        this.message = message;
    }
}
