package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.config.domain.dto.*;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryDataVO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryTypeVO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryData;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryType;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryDataMapper;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryTypeMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysDictionaryTypeImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Autowired
    private SysDictionaryDataMapper sysDictionaryDataMapper;

    /**
     * 添加字典类型
     * @param dictionaryTypeWriteReqDTO
     * @return
     */
    @Override
    public Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        //先查询是否存在该类型
        LambdaQueryWrapper<SysDictionaryType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //构建查询条件
        lambdaQueryWrapper.select(SysDictionaryType::getId)
                .eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue())
                .or()
                .eq(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey());
        //查询
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(lambdaQueryWrapper);
        //判断
        if (sysDictionaryType != null){
            throw new ServiceException("字典类型键或值已存在");
        }
        //添加
        sysDictionaryType = new SysDictionaryType();
        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        sysDictionaryType.setTypeKey(dictionaryTypeWriteReqDTO.getTypeKey());
        if(StringUtils.isNotBlank(dictionaryTypeWriteReqDTO.getRemark())){
            sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        }
        sysDictionaryTypeMapper.insert(sysDictionaryType);
        return sysDictionaryType.getId();

    }


    /**
     *  查询字典类型
     * @param dictionaryTypeListReqDTO
     * @return
     */
    @Override
    public BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        //初始化返回结果
        BasePageVO<DictionaryTypeVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        //对值和键分别进行模糊查询和精准查询
        if(StringUtils.isNotBlank(dictionaryTypeListReqDTO.getValue())){
            queryWrapper.likeRight(SysDictionaryType::getValue,
                    dictionaryTypeListReqDTO.getValue());
        }

        if (StringUtils.isNotBlank(dictionaryTypeListReqDTO.getTypeKey())){
            queryWrapper.eq(SysDictionaryType::getTypeKey,
                    dictionaryTypeListReqDTO.getTypeKey());
        }

        //执行分页查询
        Page<SysDictionaryType> page = sysDictionaryTypeMapper.selectPage(
                new Page<>(dictionaryTypeListReqDTO.getPageNo().longValue(),
                        dictionaryTypeListReqDTO.getPageSize().longValue()),
                queryWrapper
        );
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));

        //构造返回结果
        List<DictionaryTypeVO> list = new ArrayList<>();
        for (SysDictionaryType sysDictionaryType : page.getRecords()) {
            DictionaryTypeVO dictionaryTypeVO = new DictionaryTypeVO();
            BeanUtils.copyProperties(sysDictionaryType, dictionaryTypeVO);
            list.add(dictionaryTypeVO);
        }
        result.setList(list);
        return result;
    }

    /**
     * 修改字典类型
     * @param dictionaryTypeWriteReqDTO
     * @return
     */
    @Override
    public Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(
                new LambdaQueryWrapper<SysDictionaryType>()
                        .eq(SysDictionaryType::getTypeKey,
                                dictionaryTypeWriteReqDTO.getTypeKey())
        );
        if (sysDictionaryType == null){
            throw new ServiceException("字典类型不存在");
        }
        if(sysDictionaryTypeMapper.selectOne(
                new LambdaQueryWrapper<SysDictionaryType>()
                        .ne(SysDictionaryType::getTypeKey, sysDictionaryType.getTypeKey())
                        .eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue())
        ) != null){
            throw new ServiceException("字典类型键已存在");
        }

        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        if(StringUtils.isNotBlank(dictionaryTypeWriteReqDTO.getRemark())){
            sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        }
        sysDictionaryTypeMapper.updateById(sysDictionaryType);

        return sysDictionaryType.getId();
    }

    /**
     * 根据字典类型查询字典数据
     * @param dicTypeCode
     * @return
     */
    @Override
    public List<DictionaryDataDTO> selectDicDataByType(String dicTypeCode) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(
                new LambdaQueryWrapper<SysDictionaryData>()
                        .eq(SysDictionaryData::getTypeKey, dicTypeCode)
        );
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list){
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }

    /**
     * 根据字典类型查询字典数据
     * @param dicTypeCodes
     * @return
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selectDicDataByTypes(List<String> dicTypeCodes) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(
                new LambdaQueryWrapper<SysDictionaryData>()
                        .in(SysDictionaryData::getTypeKey, dicTypeCodes)
        );
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list){
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        Map<String, List<DictionaryDataDTO>> map = new HashMap<>();
        for (DictionaryDataDTO dictionaryDataDTO : result){
            List<DictionaryDataDTO> value;
            if(map.get(dictionaryDataDTO.getTypeKey()) == null){
                value = new ArrayList<>();
                value.add(dictionaryDataDTO);
                map.put(dictionaryDataDTO.getTypeKey(), value);
            }else{
                value = map.get(dictionaryDataDTO.getTypeKey());
                value.add(dictionaryDataDTO);
            }
        }
        return map;
    }

    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
        // 1 根据字典数据业务主键查询字典数据表实体类对象
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().eq(SysDictionaryData::getDataKey, dataKey));
        // 2 做对象转换
        DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
        BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
        return dictionaryDataDTO;
    }

    @Override
    public List<DictionaryDataDTO> getDicDataByDataKeys(List<String> dataKeys) {
        if(dataKeys.isEmpty())return Collections.emptyList();
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>()
                .in(SysDictionaryData::getDataKey, dataKeys));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list){
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }

    @Override
    public Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>().eq(SysDictionaryType::getTypeKey, dictionaryDataAddReqDTO.getTypeKey())) == null) {
            throw new ServiceException("字典类型不存在");
        }
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>()
                .eq(SysDictionaryData::getValue, dictionaryDataAddReqDTO.getValue())
                .or()
                .eq(SysDictionaryData::getDataKey, dictionaryDataAddReqDTO.getDataKey())
        );
        if (sysDictionaryData != null) {
            throw new ServiceException("字典数据键或值已存在");
        }
        sysDictionaryData = new SysDictionaryData();
        sysDictionaryData.setDataKey(dictionaryDataAddReqDTO.getDataKey());
        sysDictionaryData.setTypeKey(dictionaryDataAddReqDTO.getTypeKey());
        if (dictionaryDataAddReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataAddReqDTO.getSort());
        }
        if (StringUtils.isNotBlank(dictionaryDataAddReqDTO.getRemark())) {
            sysDictionaryData.setRemark(dictionaryDataAddReqDTO.getRemark());
        }
        sysDictionaryData.setValue(dictionaryDataAddReqDTO.getValue());
        sysDictionaryDataMapper.insert(sysDictionaryData);
        return sysDictionaryData.getId();
    }

    @Override
    public BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dictionaryDataListReqDTO) {
        BasePageVO<DictionaryDataVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionaryData::getTypeKey, dictionaryDataListReqDTO.getTypeKey());
        if (StringUtils.isNotBlank(dictionaryDataListReqDTO.getValue())) {
            queryWrapper.likeRight(SysDictionaryData::getValue, dictionaryDataListReqDTO.getValue());
        }
        queryWrapper.orderByAsc(SysDictionaryData::getSort);
        queryWrapper.orderByAsc(SysDictionaryData::getId);
        Page<SysDictionaryData> page = sysDictionaryDataMapper.selectPage(
                new Page<>(dictionaryDataListReqDTO.getPageNo().longValue(), dictionaryDataListReqDTO.getPageSize().longValue()),
                queryWrapper
        );
        result.setTotals(((Long)page.getTotal()).intValue());
        result.setTotalPages(((Long)page.getPages()).intValue());
        List<DictionaryDataVO> list = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : page.getRecords()) {
            DictionaryDataVO dictionaryDataVo = new DictionaryDataVO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataVo);
            list.add(dictionaryDataVo);
        }
        result.setList(list);
        return result;
    }

    @Override
    public Long editData(DictionaryDataEditReqDTO dictionaryDataEditReqDTO) {
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().eq(SysDictionaryData::getDataKey, dictionaryDataEditReqDTO.getDataKey()));
        if (sysDictionaryData == null) {
            throw new ServiceException("字典数据不存在");
        }
        if (sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().ne(SysDictionaryData::getDataKey, dictionaryDataEditReqDTO.getDataKey()).eq(SysDictionaryData::getValue, dictionaryDataEditReqDTO.getValue())) != null) {
            throw new ServiceException("字典数据名称已存在");
        }
        sysDictionaryData.setValue(dictionaryDataEditReqDTO.getValue());
        if (dictionaryDataEditReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataEditReqDTO.getSort());
        }
        if (StringUtils.isNotBlank(dictionaryDataEditReqDTO.getRemark())) {
            sysDictionaryData.setRemark(dictionaryDataEditReqDTO.getRemark());
        }
        sysDictionaryDataMapper.updateById(sysDictionaryData);
        return sysDictionaryData.getId();
    }
}
