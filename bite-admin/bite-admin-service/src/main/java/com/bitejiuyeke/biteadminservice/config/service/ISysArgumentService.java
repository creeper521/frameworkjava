package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;

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
}
