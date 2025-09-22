package com.bitejiuyeke.bitecommondomain.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     *  数据
     */
    private T data;

    public R(){}

    /**
     * 成功返回结果
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(){
        return restResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    /**
     * 成功返回结果
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(T data){
        return restResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功返回结果
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(T data, String msg){
        return restResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败返回结果
     * @param <T>
     * @return
     */

    public static <T> R<T> fail(){
        return restResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg(), null);
    }

    /**
     * 失败返回结果
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(String  msg){
        return restResult(ResultCode.ERROR.getCode(), msg, null);
    }

    /**
     * 失败返回结果
     * @param code
     * @param <T>
     * @return
     */

    public static <T> R<T> fail(Integer code, String  msg){
        return restResult(code, msg, null);
    }

    /**
     * 失败返回结果
     * @param data
     * @param <T>
     * @return
     */

    public static <T> R<T> fail(T data){
        return restResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg(), data);
    }

    /**
     * 失败返回结果
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(T data, String  msg){
        return restResult(ResultCode.ERROR.getCode(), msg, data);
    }

    /**
     * 响应结果
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> restResult(int code, String msg, T data){
        R<T> apiResult = new R<T>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }
}
