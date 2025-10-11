package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class AdInfoDTO {
    /**
     * 国家代码
     */
    private String nation_code;
    /**
     * ⾏政区划代码
     */
    private String adcode;
    /**
     * 城市代码
     */
    private String city_code;
    /**
     * ⾏政区划名称
     */
    private String name;
    /**
     * 国家
     */
    private String nation;
    /**
     * 省/直辖市
     */
    private String province;
    /**
     * 地级市
     */
    private String city;
    /**
     * 县区⼀级
     */
    private String district;
}