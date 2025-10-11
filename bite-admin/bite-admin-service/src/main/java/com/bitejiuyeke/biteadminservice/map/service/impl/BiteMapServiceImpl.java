package com.bitejiuyeke.biteadminservice.map.service.impl;

import com.bitejiuyeke.biteadminapi.map.constants.MapConstants;
import com.bitejiuyeke.biteadminservice.map.domain.dto.SysRegionDTO;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import com.bitejiuyeke.biteadminservice.map.mapper.RegionMapper;
import com.bitejiuyeke.biteadminservice.map.service.IBiteMapService;
import com.bitejiuyeke.bitecommoncache.utils.CacheUtil;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class BiteMapServiceImpl implements IBiteMapService {

    /**
     * 本地内存服务对象
     */
    @Autowired
    private Cache<String, Object> caffeineCache;

    /**
     * redis服务
     */
    @Autowired
    public RedisService redisService;

    /**
     * sys_region表的mapper
     */
    @Autowired
    private RegionMapper regionMapper;


    /**
     * 缓存预热方案
     * @return 缓存列表数据
     */
    @Override
    public List<SysRegionDTO> getCityList() {
        // 1 声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
        // 2 查询数据库
        List<SysRegion> list =  regionMapper.selectAllRegion();
        // 3 提取城市数据列表,并且做对象转换
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        return result;
    }

    /**
     * 城市列表查询 V1
     * @return 城市列表信息
     */
    public List<SysRegionDTO> getCityListV1() {
        //声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
        //查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //提取数据列表，并作对象转换
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        return result;
    }

    /**
     * 城市列表查询 V2
     * @return 城市列表信息
     */
    public List<SysRegionDTO> getCityListV2() {
        //声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
        //查询数据库
        List<SysRegionDTO> cache = redisService.getCacheObject(MapConstants.CACHE_MAP_CITY_KEY,
                new TypeReference<List<SysRegionDTO>>() {});
        if (cache != null){
            return cache;
        }
        //查数据库,并作对象转换
        List<SysRegion> list = regionMapper.selectAllRegion();
        //提取数据列表，并作对象转换
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        //设置缓存
        redisService.setCacheObject(MapConstants.CACHE_MAP_CITY_KEY, result);
        return result;
    }

    /**
     * 城市列表查询 V3 二级缓存方案
     * @return 城市列表信息
     */
    public List<SysRegionDTO> getCityListV3() {
        List<SysRegionDTO> result = new ArrayList<>();

        List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY,
                new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if(cache != null){
            return cache;
        }

        List<SysRegion> list = regionMapper.selectAllRegion();
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, result, caffeineCache, 120L, TimeUnit.MINUTES);
        return result;
    }

    @PostConstruct
    public void initCityMap(){
        //先直接查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //服务启动的时候缓存城市列表
        loadCityInfo(list);
    }

    /**
     * 缓存城市信息
     * @param list
     */
    private void loadCityInfo(List<SysRegion> list) {
        List<SysRegionDTO> result = new ArrayList<>();
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
            CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, result, caffeineCache, 120L, TimeUnit.MINUTES);
        }
    }
}
