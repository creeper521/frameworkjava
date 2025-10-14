package com.bitejiuyeke.bitecommonsecurity.util;

import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

public class JwtUtil {
    /**
     * 密钥
     */
    public static String secret = TokenConstants.SECRET;

    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }

}
