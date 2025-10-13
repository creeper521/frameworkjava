package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典类型写入参数DTO
 */
@Data
public class DictionaryTypeWriteReqDTO {

    /**
     * 字典类型名称
     */
    @NotNull(message = "字典类型名称不能为空")
    private String value;

    /**
     * 字典类型标识
     */
    @NotNull(message = "字典类型标识不能为空")
    private String TypeKey;

    /**
     * 备注
     */
    private String remark;
}
