package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysArgument;
import com.bitejiuyeke.biteadminservice.config.mapper.SysArgumentMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SysArgumentServiceImpl implements ISysArgumentService {

    @Resource
    private SysArgumentMapper sysArgumentMapper;

    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        //根据参数业务主键查询参数对象
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>()
                .eq(SysArgument::getConfigKey, configKey));
        // 2 做对象转换
        if (sysArgument != null) {
            ArgumentDTO argumentDTO = new ArgumentDTO();
            BeanUtils.copyProperties(sysArgument, argumentDTO);
            return argumentDTO;
        }
        return null;
    }
}
