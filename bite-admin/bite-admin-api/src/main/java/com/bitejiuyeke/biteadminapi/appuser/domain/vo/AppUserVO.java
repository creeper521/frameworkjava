package com.bitejiuyeke.biteadminapi.appuser.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户VO
 */
@Data
public class AppUserVO implements Serializable {

    /**
     * C端用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 微信ID
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatar;
}

