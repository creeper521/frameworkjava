package com.bitejiuyeke.bitecommonsecurity.util;

import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

public class JwtUtil {
    /**
     * 密钥
     */
    public static String secret = TokenConstants.SECRET;

    /**
     * 从令牌中生成令牌
     * @param claims
     * @return
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }

    /**
     * 解析令牌
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取用户key
     * @param token
     * @return
     */
    public static String getUserKey(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
        
    }

    /**
     * 获取用户
     * @param claims
     * @return
     */
    public static String getUserKey(Claims  claims){
        return getValue(claims, SecurityConstants.USER_KEY);

    }

    /**
     * 根据身份信息获取键值
     * @param claims
     * @param key
     * @return
     */
    public static String getValue(Claims claims, String key) {
        Object value = claims.get(key);
        if (value == null){
            return "";
        }
        return value.toString();
    }

    /**
     * 获取用户id
     * @param token
     * @return
     */
    public static String getUserId(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_ID);
    }

    /**
     * 获取用户id
     * @param claims
     * @return
     */
    public static String getUserId(Claims  claims){
        return getValue(claims, SecurityConstants.USER_ID);
    }

    /**
     * 获取用户名称
     * @param token
     * @return
     */
    public static String getUserName(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USERNAME);
    }
    /**
     * 获取用户名称
     * @param claims
     * @return
     */
    public static String getUserName(Claims  claims){
        return getValue(claims, SecurityConstants.USERNAME);
    }

    /**
     *  获取用户来源
     * @param token
     * @return
     */
    public static String getUserFrom(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_FROM);
    }

    /**
     * 获取用户来源
     * @param claims
     * @return
     */
    public static String getUserFrom(Claims  claims){
        return getValue(claims, SecurityConstants.USER_FROM);
    }
}

