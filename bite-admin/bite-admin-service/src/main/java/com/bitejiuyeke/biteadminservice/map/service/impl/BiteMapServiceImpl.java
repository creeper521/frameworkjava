package com.bitejiuyeke.biteadminservice.map.service.impl;


import com.bitejiuyeke.biteadminapi.map.constants.MapConstants;
import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.PlaceSearchReqDTO;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.biteadminservice.map.domain.dto.*;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import com.bitejiuyeke.biteadminservice.map.mapper.RegionMapper;
import com.bitejiuyeke.biteadminservice.map.service.IBiteMapService;
import com.bitejiuyeke.biteadminservice.map.service.IMapProvider;
import com.bitejiuyeke.bitecommoncache.utils.CacheUtil;
import com.bitejiuyeke.bitecommoncore.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommoncore.utils.PageUtil;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
     * 参数服务处理对象
     */
    @Autowired
    private ISysArgumentService sysArgumentService;

    /**
     * 腾讯位置服务的服务类
     */
    @Autowired
    private IMapProvider mapProvider;



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
        //服务启动的时候，缓存城市归类列表
        loadCityPinyinInfo(list);
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

    /**
     * 城市拼⾳归类查询
     * @return 城市字⺟与城市列表的哈希
     */
    @Override
    public Map<String, List<SysRegionDTO>> getCityPylist() {
        Map<String, List<SysRegionDTO>> map = CacheUtil.getL2Cache(
                redisService,
                MapConstants.CACHE_MAP_CITY_PINYIN_KEY,
                new TypeReference<Map<String, List<SysRegionDTO>>>() {},
                caffeineCache
        );
        return map;
    }

    /**
     * 初始化A-Z城市列表缓存内容
     */
    private void loadCityPinyinInfo(List<SysRegion> list){
        //拿城市，再封装
        Map<String, List<SysRegionDTO>> map = new TreeMap<>();
        for (SysRegion sysRegion : list) {
            if(sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                //拿首字母
                String firstChar = sysRegionDTO.getPinyin()
                                .toUpperCase()
                                .substring(0, 1);
                //若该字母出现过
                if(map.containsKey(firstChar)){
                    map.get(firstChar).add(sysRegionDTO);
                }else {
                    List<SysRegionDTO> regionDTOList = new ArrayList<>();
                    regionDTOList.add(sysRegionDTO);
                    map.put(firstChar, regionDTOList);
                }
            }
        }
        //构建缓存
        CacheUtil.setL2Cache(redisService,
                MapConstants.CACHE_MAP_CITY_PINYIN_KEY,
                map,
                caffeineCache,
                120L,
                TimeUnit.MINUTES);
    }

    /**
     * 获取子级区域信息
     * @param parentId
     * @return
     */
    @Override
    public List<SysRegionDTO> getRegionChildren(Long parentId) {
        //构建参与构建缓存的key
        String key = MapConstants.CACHE_MAP_CITY_CHILDREN_KEY + parentId;
        //查缓存
        List<SysRegionDTO> resultRegions = CacheUtil.getL2Cache(redisService, key,
                new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if(resultRegions != null){
            return resultRegions;
        }
        //查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        List<SysRegionDTO> result = new ArrayList<>();
        for (SysRegion sysRegion : list){
            if (sysRegion.getParentId() != null && sysRegion.getParentId().equals(parentId)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        //设置缓存
        CacheUtil.setL2Cache(redisService, key, result, caffeineCache, 120L, TimeUnit.MINUTES);
        return result;
    }

    /**
     * 获取热门城市列表信息
     * @return
     */
    @Override
    public List<SysRegionDTO> getHotCityList() {
        //先查缓存
        List<SysRegionDTO> hotCityList = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_HOT_CITY,
                new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if(hotCityList != null){
            return hotCityList;
        }
        //设置六个热门城市
        String ids = sysArgumentService.getByConfigKey(MapConstants.CONFIG_KEY).getValue();
        List<Long> idList = new ArrayList<>();
        for (String num : ids.split(",")){
            idList.add(Long.parseLong(num));
        }
        //查询热门城市结果
        List<SysRegionDTO> result = new ArrayList<>();
        for(SysRegion sysRegion : regionMapper.selectBatchIds(idList)){
            SysRegionDTO sysRegionDTO = new SysRegionDTO();
            BeanUtils.copyProperties(sysRegion, sysRegionDTO);
            result.add(sysRegionDTO);
        }
        //设置缓存
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_HOT_CITY, result, caffeineCache, 120L, TimeUnit.MINUTES);
        return result;
    }

    /**
     * 根据地图搜索
     * @param placeSearchReqDTO
     * @return
     */
    @Override
    public BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO) {
        //构建查询腾讯位置服务的入参
        SuggestSearchDTO suggestSearchDTO = new SuggestSearchDTO();
        BeanUtils.copyProperties(placeSearchReqDTO, suggestSearchDTO);
        suggestSearchDTO.setPageIndex(placeSearchReqDTO.getPageNo());
        suggestSearchDTO.setId(String.valueOf(placeSearchReqDTO.getId()));
        //调用地图位置查询接口
        PoiListDTO poiListDTO = mapProvider.searchQQMapPlaceByRegion(suggestSearchDTO);
        //做结果对象转换
        List<PoiDTO> poiDTOList = poiListDTO.getData();
        BasePageDTO<SearchPoiDTO> result = new BasePageDTO<>();
        result.setTotals(poiListDTO.getCount());
        result.setTotalPages(PageUtil.getTotalPages(result.getTotals(),
                placeSearchReqDTO.getPageSize()));

        List<SearchPoiDTO> pageRes = new ArrayList<>();
        for (PoiDTO poiDTO : poiDTOList){
            SearchPoiDTO searchPoiDTO = new SearchPoiDTO();
            BeanUtils.copyProperties(poiDTO, searchPoiDTO);
            searchPoiDTO.setLongitude(poiDTO.getLocation().getLng());
            searchPoiDTO.setLatitude(poiDTO.getLocation().getLat());
            pageRes.add(searchPoiDTO);
        }
        result.setList(pageRes);
        //返回结果
        return result;
    }


    /**
     * 根据经纬度获取城市信息
     * @param locationReqDTO
     * @return
     */
    @Override
    public RegionCityDTO getCityByLocation(LocationReqDTO locationReqDTO) {
// 1 构建查询腾讯位置服务的⼊参
        LocationDTO locationDTO = new LocationDTO();
        BeanUtils.copyProperties(locationReqDTO, locationDTO);
        RegionCityDTO result = new RegionCityDTO();
// 2 调⽤腾讯位置服务接⼝
        GeoResultDTO geoResultDTO =
                mapProvider.getQQMapDistrictByLonLat(locationDTO);
        if (geoResultDTO != null && geoResultDTO.getResult() != null &&
                geoResultDTO.getResult().getAd_info() != null) {
            String cityName = geoResultDTO.getResult().getAd_info().getCity();
// 3 查缓存
            List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService,
                    MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {
                    }, caffeineCache);
            for (SysRegionDTO sysRegionDTO: cache) {
                if (sysRegionDTO.getFullName().equals(cityName)) {
                    BeanUtils.copyProperties(sysRegionDTO, result);
                    return result;
                }
            }
        }
        return result;
    }
}
