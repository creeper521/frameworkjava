package com.bitejiuyeke.biteadminapi.config.domain.vo;


import lombok.Data;

/**
 * 参数VO
 */
@Data
public class ArgumentVO {
    /**
     * 参数ID
     */
    private Long id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键
     */
    private String configKey;

    /**
     * 参数值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;
}
