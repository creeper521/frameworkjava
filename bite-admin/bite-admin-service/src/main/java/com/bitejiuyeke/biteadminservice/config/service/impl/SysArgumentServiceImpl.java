package com.bitejiuyeke.biteadminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgumentVO;
import com.bitejiuyeke.biteadminservice.config.domain.entity.SysArgument;
import com.bitejiuyeke.biteadminservice.config.mapper.SysArgumentMapper;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysArgumentServiceImpl implements ISysArgumentService {

    @Resource
    private SysArgumentMapper sysArgumentMapper;

    /**
     * 根据参数键查询参数对象
     * @param configKey 参数键
     * @return
     */
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

    /**
     * 新增参数
     * @param argumentAddReqDTO 参数对象
     * @return
     */
    @Override
    public Long add(ArgumentAddReqDTO argumentAddReqDTO) {
        //先查询表中有没有重复的
        LambdaQueryWrapper<SysArgument> wrapper = new LambdaQueryWrapper<>();

        wrapper.select(SysArgument::getId).eq(SysArgument::getName, argumentAddReqDTO.getName())
                .or()
                .eq(SysArgument::getConfigKey, argumentAddReqDTO.getConfigKey());

        SysArgument sysArgument = sysArgumentMapper.selectOne(wrapper);
        if(sysArgument != null){
            throw new ServiceException("参数已存在");
        }

        //在进行插入
        sysArgument = new SysArgument();
        sysArgument.setName(argumentAddReqDTO.getName());
        sysArgument.setConfigKey(argumentAddReqDTO.getConfigKey());
        sysArgument.setValue(argumentAddReqDTO.getValue());
        if(StringUtils.isNotBlank(argumentAddReqDTO.getRemark())){
            sysArgument.setRemark(argumentAddReqDTO.getRemark());
        }
        sysArgumentMapper.insert(sysArgument);
        return sysArgument.getId();
    }

    /**
     * 查询参数列表
     * @param argumentListReqDTO
     * @return
     */
    @Override
    public BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO) {
        BasePageVO<ArgumentVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysArgument> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(argumentListReqDTO.getConfigKey())){
            wrapper.eq(SysArgument::getConfigKey, argumentListReqDTO.getConfigKey());
        }
        if(StringUtils.isNotBlank(argumentListReqDTO.getName())){
            wrapper.likeRight(SysArgument::getName, argumentListReqDTO.getName());
        }
        Page<SysArgument> page = sysArgumentMapper.selectPage(new Page<>(
                argumentListReqDTO.getPageNo().longValue(),
                argumentListReqDTO.getPageSize().longValue()),
                wrapper);
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));

        List<ArgumentVO> list = new ArrayList<>();
        for (SysArgument sysArgument : page.getRecords()) {
            ArgumentVO argumentVO = new ArgumentVO();
            BeanUtils.copyProperties(sysArgument, argumentVO);
            list.add(argumentVO);
        }
        result.setList(list);
        return result;
    }
}
