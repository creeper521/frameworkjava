package com.bitejiuyeke.biteadminapi.map.feign;

import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.PlaceSearchReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionCityVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.SearchPoiVo;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "mapFeignClient", value = "bite-admin")
public interface MapFeignClient {
    /**
     * 获取城市列表
     * @return
     */
    @GetMapping("/map/city_list")
    R<List<RegionVO>> getCityList();

    /**
     * 城市拼音归类查询
     * @return 城市字母与城市列表的哈希
     */
    @GetMapping("/map/city_pinyin_list")
    R<Map<String, List<RegionVO>>> getCityPylist();

    /**
     * 下级区域查询
     */
    @GetMapping("/map/region_children_list")
    R<List<RegionVO>> regionChildren(@RequestParam Long parentId);

    /**
     * 获取热门城市列表
     */
    @GetMapping("/map/city_hot_list")
    R<List<RegionVO>> getHotCityList();

    /**
     * 搜索区域
     */
    @PostMapping("/map/search")
    R<BasePageVO<SearchPoiVo>> searchSuggestOnMap(@RequestBody PlaceSearchReqDTO placeSearchReqDTO);

    /**
     * 根据经纬度获取当前所在城市
     */
    @PostMapping("/map/locate_city_by_location")
    R<RegionCityVO> locateCityByLocation(@RequestBody LocationReqDTO
                                                 location);
}
