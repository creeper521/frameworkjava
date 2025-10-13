package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典数据写入参数DTO
 */
@Data
public class DictionaryDataAddReqDTO {
    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键不能为空")
    private String typeKey;

    /**
     * 字典数据键
     */
    @NotBlank(message = "字典数据键不能为空")
    private String dataKey;

    /**
     * 字典数据值
     */
    @NotBlank(message = "字典数据值不能为空")
    private String value;

    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
}
