package com.bitejiuyeke.biteadminapi.config.domain.dto;

import lombok.Data;

@Data
public class DictionaryDataDTO {

    /**
     * 字典数据id主键
     */
    private Long id;

    /**
     * 字典类型键
     */
    private String typeKey;

    /**
     * 字典数据键
     */
    private String dataKey;

    /**
     * 字典数据值
     */
    private String value;

    /**
     * 字典数据备注
     */
    private String remark;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;
}
