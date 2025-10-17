package com.bitejiuyeke.biteadminservice.user.domain.vo;

import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import lombok.Data;

/**
 * B端用户登录
 */
@Data
public class SysUserLoginVO extends LoginUserDTO {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * ⾝份
     */
    private String identity;
    /**
     * 状态
     */
    private String status;
}
