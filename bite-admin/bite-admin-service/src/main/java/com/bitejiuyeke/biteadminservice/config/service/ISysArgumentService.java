package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgumentVO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;

/**
 * 参数服务相关接口
 */
public interface ISysArgumentService {

    /**
     * 根据参数键查询参数对象
     * @param configKey 参数键
     * @return 参数对象
     */
    ArgumentDTO getByConfigKey(String configKey);

    /**
     * 新增参数
     * @param argumentAddReqDTO 参数对象
     * @return 新增结果
     */
    Long add(ArgumentAddReqDTO argumentAddReqDTO);

    /**
     * 查询参数列表
     * @param argumentListReqDTO
     * @return
     */
    BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO);
}
