package com.bitejiuyeke.bitegateway.handler;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ⽹关统⼀异常处理
 */
@Order(-1)
@Configuration
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    /**
     * 处理器
     *
     * @param exchange ServerWebExchange
     * @param ex       异常信息
     * @return ⽆
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        //响应已经提交到客⼾端，⽆法再对这个响应进⾏常规的异常处理修改了，直接返回⼀个含原始异常ex的Mono.error(ex)
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        int retCode = ResultCode.ERROR.getCode();
        String retMsg = ResultCode.ERROR.getMsg();
        if (ex instanceof NoResourceFoundException) {
            retCode = ResultCode.SERVICE_NOT_FOUND.getCode();
            retMsg = ResultCode.SERVICE_NOT_FOUND.getMsg();
        } else {
            retMsg = ResultCode.ERROR.getMsg();
        }
        //按照统⼀状态码的特点，前三位是http状态码。从中截取http状态码
        int httpCode =
                Integer.parseInt(String.valueOf(retCode).substring(0, 3));
        log.error("[⽹关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().
                getPath(), ex.getMessage());
        return webFluxResponseWriter(response,
                HttpStatus.valueOf(httpCode), retMsg, retCode);
    }

    private static Mono<Void> webFluxResponseWriter(ServerHttpResponse
                                                            response, HttpStatus status, Object value, int code) {
        return webFluxResponseWriter(response,
                MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }

    private static Mono<Void> webFluxResponseWriter(ServerHttpResponse
                                                            response, String contentType, HttpStatus status, Object value, int code) {
        response.setStatusCode(status); //设置http响应
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType); //设置响应体内容类型为json
        R<?> result = R.fail(code, value.toString()); //按照约定响应数据结构，构建响应体内容
        DataBuffer dataBuffer =
                response.bufferFactory().wrap(JsonUtil.obj2String(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer)); //将响应体内容写⼊响应体
    }
}
