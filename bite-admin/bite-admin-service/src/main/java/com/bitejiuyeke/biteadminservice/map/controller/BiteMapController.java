package com.bitejiuyeke.biteadminservice.map.controller;

import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.biteadminservice.map.domain.dto.SysRegionDTO;
import com.bitejiuyeke.biteadminservice.map.service.IBiteMapService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BiteMapController implements MapFeignClient {

    @Autowired
    private IBiteMapService mapService;

    /**
     * 获取城市列表
     * @return
     */
    @Override
    public R<List<RegionVO>> getCityList() {
        List<SysRegionDTO> regionDTOS = mapService.getCityList();
        List<RegionVO> result = BeanCopyUtil.copyListProperties(regionDTOS, RegionVO::new);
        return R.ok(result);
    }

    /**
     * 获取城市列表
     * @return
     */
    @Override
    public R<Map<String, List<RegionVO>>> getCityPylist() {
        Map<String, List<RegionVO>> result = new LinkedHashMap<>();
        Map<String, List<SysRegionDTO>> pinyinList = mapService.getCityPylist();
        for (Map.Entry<String, List<SysRegionDTO>> region : pinyinList.entrySet()) {
            result.put(
                    region.getKey(),
                    BeanCopyUtil.copyListProperties(region.getValue(), RegionVO::new)
            );
        }
        return R.ok(result);
    }

    @Override
    public R<List<RegionVO>> regionChildren(Long parentId) {
        List<SysRegionDTO> regionDTOS = mapService.getRegionChildren(parentId);
        List<RegionVO> result = BeanCopyUtil.copyListProperties(regionDTOS, RegionVO::new);
        return R.ok(result);
    }

    @Override
    public R<List<RegionVO>> getHotCityList() {
        List<SysRegionDTO> hotList = mapService.getHotCityList();
        return R.ok(BeanCopyUtil.copyListProperties(hotList, RegionVO::new));
    }
}
