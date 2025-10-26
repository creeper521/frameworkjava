package com.bitejiuyeke.bitecommonsecurity.util;

import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 安全工具类
 */
public class SecurityUtil {
    /**
     * 获取令牌
     * @return
     */
    public static String getToken() {
        return getToken(ServletUtil.getRequest());
    }

    /**
     * 获取令牌
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest  request){
        String token = request.getHeader(SecurityConstants.AUTHORIZATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 替换令牌前缀
     * @param token
     * @return
     */
    private static String replaceTokenPrefix(String token) {
        if(StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)){
            return token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

}
