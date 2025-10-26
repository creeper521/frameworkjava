package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 参数服务相关远程调用
 */
@FeignClient(contextId = "argumentFeignClient", name = "bite-admin", path = "/argument")
public interface ArgumentFeignClient {
    /**
     * 根据参数键查询参数对象
     * @param configKey 配置键
     * @return 参数对象
     */
    @PostMapping("/key")
    ArgumentDTO getByConfigKey(@RequestParam String configKey);

    /**
     * 根据参数键查询参数对象
     * @param configKeys
     * @return
     */
    @PostMapping("/keys")
    List<ArgumentDTO> getByConfigKeys(@RequestBody List<String> configKeys);
}
