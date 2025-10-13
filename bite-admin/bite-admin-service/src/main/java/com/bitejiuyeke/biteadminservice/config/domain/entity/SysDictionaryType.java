package com.bitejiuyeke.biteadminservice.config.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_dictionary_type")
public class SysDictionaryType {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型编码
     */
    private String typeKey;

    /**
     * 字典类型名称
     */
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer Status;
}
