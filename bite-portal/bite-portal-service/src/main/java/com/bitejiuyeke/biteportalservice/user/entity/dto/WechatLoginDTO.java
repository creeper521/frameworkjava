package com.bitejiuyeke.biteportalservice.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WechatLoginDTO extends LoginDTO{

    /**
     * 微信openId
     */
    @NotBlank(message = "openId不能为空")
    private String openId;
}
