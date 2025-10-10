package com.bitejiuyeke.biteadminservice.map.controller;

import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.bitecommondomain.domain.R;

import java.util.List;

public class BiteMapController implements MapFeignClient {
    @Override
    public R<List<RegionVO>> getCityList() {
        return null;
    }
}
