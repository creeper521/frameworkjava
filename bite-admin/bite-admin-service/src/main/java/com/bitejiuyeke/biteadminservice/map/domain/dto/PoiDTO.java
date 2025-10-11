package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class PoiDTO {
    /**
     * poi地点的唯⼀标识
     */
    private String id;
    /**
     * poi地点的名称

     */
    private String title;
    /**
     * 地址
     */
    private String address;
    /**
     * POI类型，值说明：0:普通POI / 1:公交⻋站 / 2:地铁站 / 3:公交线路 / 4:⾏政区划
     */
    private String type;
    /**
     * poi地点所处坐标
     */
    private LocationDTO location;
}
