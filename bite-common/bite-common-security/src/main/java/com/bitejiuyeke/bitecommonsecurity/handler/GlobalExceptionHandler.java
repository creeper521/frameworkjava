package com.bitejiuyeke.bitecommonsecurity.handler;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 设置http响应码
     *
     * @param response 响应信息
     * @param errcode 响应码
     */
    private void setResponseCode(HttpServletResponse response,Integer errcode)
    {
        int httpCode =
                Integer.parseInt(String.valueOf(errcode).substring(0,3));
        response.setStatus(httpCode);
    }
    /**
     * 请求⽅式不⽀持
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 异常结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?>
    handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                        HttpServletRequest
                                                request,
                                        HttpServletResponse
                                                response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不⽀持'{}'请求", requestURI, e.getMethod());
        setResponseCode(response,
                ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode());
        return R.fail(ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode(),
                ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getMsg());
    }
    /**
     * 类型不匹配异常
     *
     * @param e 异常信息
     * @param response 响应
     * @return 不匹配结果
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<?>
    handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException
                                                      e,
                                              HttpServletResponse
                                                      response) {
        log.error("类型不匹配异常",e);
        setResponseCode(response, ResultCode.PARA_TYPE_MISMATCH.getCode());
        return R.fail(ResultCode.PARA_TYPE_MISMATCH.getCode(),
                ResultCode.PARA_TYPE_MISMATCH.getMsg());
    }

    /**
     * 业务异常(微服务侧)
     *
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 响应结果
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> handleServiceException(ServiceException e, HttpServletResponse response, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生异常",requestURI, e);
        setResponseCode(response, e.getCode());
        return R.fail(e.getCode(), e.getMessage());
    }




    /**
     * url未找到异常
     *
     * @param e 异常信息
     * @param response 响应
     * @return 异常结果
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public R<?> handleMethodNoResourceFoundException(NoResourceFoundException
                                                             e,
                                                     HttpServletResponse
                                                             response) {
        log.error("url未找到异常",e);
        setResponseCode(response, ResultCode.URL_NOT_FOUND.getCode());
        return R.fail(ResultCode.URL_NOT_FOUND.getCode(),
                ResultCode.URL_NOT_FOUND.getMsg());
    }
    /**
     * 拦截运⾏时异常
     *
     * @param e 异常信息
     * @param request 请求信息
     * @param response 响应信息
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e, HttpServletRequest
            request,
                                       HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发⽣运⾏时异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }
    /**
     * 系统异常
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发⽣异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }
}
