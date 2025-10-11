package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.SysRegionDTO;

import java.util.List;
import java.util.Map;

/**
 * 地图服务的接口
 */
public interface IBiteMapService {
    /**
     * 获取城市列表
     * @return
     */
    List<SysRegionDTO> getCityList();

    /**
     * 按照A-Z分开的城市列表信息
     * @return
     */
    Map<String, List<SysRegionDTO>> getCityPylist();

    /**
     * 获取下级区域信息
     * @param parentId
     * @return
     */
    List<SysRegionDTO> getRegionChildren(Long parentId);

    /**
     * 获取热门城市
     */
    List<SysRegionDTO> getHotCityList();
}
