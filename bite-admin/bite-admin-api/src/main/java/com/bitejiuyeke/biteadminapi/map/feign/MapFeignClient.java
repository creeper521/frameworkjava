package com.bitejiuyeke.biteadminapi.map.feign;

import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "mapFeignClient", value = "bite-admin")
public interface MapFeignClient {
    @GetMapping("/map/city_list")
    R<List<RegionVO>> getCityList();
}
