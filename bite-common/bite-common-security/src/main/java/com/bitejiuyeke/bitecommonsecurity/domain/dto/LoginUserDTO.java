package com.bitejiuyeke.bitecommonsecurity.domain.dto;

import lombok.Data;

@Data
public class LoginUserDTO {
    /**
     * ⽤⼾唯⼀标识
     */
    private String token;
    /**
     * ⽤⼾名id
     */
    private Long userId;
    /**
     * ⽤⼾来源
     */
    private String userFrom;
    /**
     * ⽤⼾名
     */
    private String userName;
    /**
     * 登录时间
     */
    private Long loginTime;
    /**
     * 过期时间
     */
    private Long expireTime;
}
