package com.bitejiuyeke.biteadminservice.user.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserListReqDTO implements Serializable {
    /**
     * ⽤⼾id
     */
    private Long userId;
    /**
     * ⼿机号
     */
    private String phoneNumber;
    /**
     * 状态
     */
    private String status;
}
