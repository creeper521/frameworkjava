package com.bitejiuyeke.bitegateway.filter;

import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonsecurity.util.JwtUtil;
import com.bitejiuyeke.bitegateway.config.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter , Ordered {

    /**
     * 忽略的url
     */
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    /**
     * redis服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 登录过滤器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String url = request.getURI().getPath();

        //忽略的url
        if(StringUtil.matches(url, ignoreWhite.getWhites())){
            return chain.filter(exchange);
        }

        //获取token
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
            return unauthorizedResponse(exchange, ResultCode.TOKEN_EMPTY);
        }
        //解析token
        Claims claims;
        try {
            claims = JwtUtil.parseToken(token);
            if (claims == null){
                return unauthorizedResponse(exchange, ResultCode.TOKEN_INVALID);
            }
        } catch (Exception e) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_OVERTIME);
        }

        //检验登录状态
        String userKey = JwtUtil.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userKey));
        if (!islogin){
            return unauthorizedResponse(exchange, ResultCode.LOGIN_STATUS_OVERTIME);
        }

        //提取用户信息
        String userId = JwtUtil.getUserId(claims);
        String userName = JwtUtil.getUserName(claims);
        String userFrom = JwtUtil.getUserFrom(claims);
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName)){
            return unauthorizedResponse(exchange, ResultCode.TOKEN_CHECK_FAILED);
        }

        //添加用户信息
        addHeader(mutate, SecurityConstants.USER_KEY, userKey);
        addHeader(mutate, SecurityConstants.USER_ID, userId);
        addHeader(mutate, SecurityConstants.USERNAME, userName);
        addHeader(mutate, SecurityConstants.USER_FROM, userFrom);

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    /**
     * 添加用户信息
     * @param mutate
     * @param name
     * @param value
     */
    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value ==  null){
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtil.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    /**
     * 获取token的key
     * @param token
     * @return
     */
    private String getTokenKey(String token) {
        return TokenConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 响应结果
     * @param exchange
     * @param resultCode
     * @return
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ResultCode resultCode) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        int retCode = Integer.parseInt(String.valueOf(resultCode).substring(0, 3));
        return ServletUtil.webFluxResponseWriter(
                exchange.getResponse(),
                HttpStatus.valueOf(retCode),
                resultCode.getMsg(),
                resultCode.getCode());
    }

    /**
     * 获取token
     * @param request
     * @return
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION);
        if(StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)){
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    /**
     * 顺序
     * @return
     */
    @Override
    public int getOrder() {
        return -200;
    }
}
