package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.SysRegionDTO;

import java.util.List;

/**
 * 地图服务的接口
 */
public interface IBiteMapService {
    /**
     * 获取城市列表
     * @return
     */
    List<SysRegionDTO> getCityList();
}
