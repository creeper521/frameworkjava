package com.bitejiuyeke.biteadminapi.appuser.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppUserListReqDTO extends BasePageReqDTO implements Serializable {
    /**
     * ⽤⼾ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 微信ID
     */
    private String openID;
}
