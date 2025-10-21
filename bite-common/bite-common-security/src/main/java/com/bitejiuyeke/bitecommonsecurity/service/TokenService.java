package com.bitejiuyeke.bitecommonsecurity.service;

import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommondomain.constants.CacheConstants;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.util.JwtUtil;
import com.bitejiuyeke.bitecommonsecurity.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {

    /**
     * 毫秒
     */
    protected static final long MILLIS_SECOND = 1000;
    /**
     * 60s
     */
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    /**
     * 刷新周期
     */
    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME
            * MILLIS_MINUTE;
    /**
     * 过期时间
     */
    private final static Long EXPIRE_TIME = CacheConstants.EXPIRATION;
    /**
     * token的key
     */
    private final static String ACCESS_TOKEN = TokenConstants.LOGIN_TOKEN_KEY;

    /**
     * redis操作服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 创建token
     * @param loginUserDTO
     * @return
     */
    public TokenDTO createToken(LoginUserDTO loginUserDTO) {
        String token = UUID.randomUUID().toString();
        loginUserDTO.setToken(token);
        refreshToken(loginUserDTO);

        //jwt存储信息
        Map<String, Object> claimMap = new HashMap<String, Object>();
        claimMap.put(SecurityConstants.USER_ID, token);
        claimMap.put(SecurityConstants.USER_KEY, loginUserDTO.getUserId());
        claimMap.put(SecurityConstants.USERNAME, loginUserDTO.getUserName());
        claimMap.put(SecurityConstants.USER_FROM, loginUserDTO.getUserFrom());

        //接口返回信息
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(JwtUtil.createToken(claimMap));
        tokenDTO.setExpires(EXPIRE_TIME);
        return tokenDTO;
    }

    /**
     * 获取登录用户信息
     * @param token
     * @return
     */
    public LoginUserDTO getLoginUser(String token) {
        LoginUserDTO user = null;

        try {
            if(StringUtils.isNotEmpty(token)){
                String userKey = JwtUtil.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userKey), LoginUserDTO.class);
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    public LoginUserDTO getLoginUser(HttpServletRequest request) {
        String token = SecurityUtil.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取登录用户信息
     * @return
     */
    public LoginUserDTO getLoginUser() {
        return getLoginUser(ServletUtil.getRequest());
    }

    /**
     * 删除用户信息
     * @param token
     */
    public void delLoginUser(String token) {
        if(StringUtils.isNotEmpty(token)){
            String userKey = JwtUtil.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }

    /**
     * 删除用户信息
     * @param userId
     * @param userFrom
     */
    public void delLoginUser(Long userId, String userFrom) {
        if(userId == null)return;
        //遍历全部key然后删除
        Collection<String> tokenKeys = redisService.keys(ACCESS_TOKEN + "*");
        for(String tokenKey : tokenKeys){
            LoginUserDTO user = redisService.getCacheObject(tokenKey, LoginUserDTO.class);
            if(user != null && user.getUserId().equals(userId) && user.getUserFrom().equals(userFrom)){
                redisService.deleteObject(tokenKey);
            }
        }
    }

    /**
     * 验证token有效期
     * @param loginUserDTO
     */
    public void verifyToken(LoginUserDTO loginUserDTO) {
        long expireTime = loginUserDTO.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUserDTO);
        }

    }

    /**
     * 设置用户信息
     * @param loginUserDTO
     */
    public void setLoginUser(LoginUserDTO loginUserDTO) {
        if(loginUserDTO != null && StringUtils.isNotEmpty(loginUserDTO.getToken())){
            refreshToken(loginUserDTO);
        }
    }

    /**
     * 刷新token
     *
     * @param loginUserDTO
     */
    private void refreshToken(LoginUserDTO loginUserDTO) {
        loginUserDTO.setLoginTime(System.currentTimeMillis());
        loginUserDTO.setExpireTime(loginUserDTO.getLoginTime() + EXPIRE_TIME * MILLIS_MINUTE);

        String userKey = getTokenKey(loginUserDTO.getToken());
        redisService.setCacheObject(userKey, loginUserDTO, EXPIRE_TIME, TimeUnit.MINUTES);
    }

    /**
     * token key信息
     * @param token
     * @return
     */
    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}
