package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 参数修改请求参数
 */
@Data
public class ArgumentEditReqDTO {
    /**
     * 参数键
     */
    @NotBlank(message = "参数键不能为空")
    private String configKey;

    /**
     * 参数名字
     */
    @NotBlank(message = "参数名字不能为空")
    private String name;

    /**
     * 参数值
     */
    @NotBlank(message = "参数值不能为空")
    private String value;

    /**
     * 备注
     */
    private String remark;
}
