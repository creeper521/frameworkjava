package com.bitejiuyeke.bitecommonsecurity.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import lombok.Data;

@Data
public class TokenDTO {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 过期时间
     */
    private Long expires;

    public TokenVO convertToVO() {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(this.accessToken);
        tokenVO.setExpires(this.expires);
        return tokenVO;
    }
}