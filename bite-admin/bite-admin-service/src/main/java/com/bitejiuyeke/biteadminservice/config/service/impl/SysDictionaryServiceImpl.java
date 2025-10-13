package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysDictionaryType;
import com.bitejiuyeke.biteadminservice.config.mapper.SysDictionaryTypeMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SysDictionaryServiceImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

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


}
