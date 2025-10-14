package com.bitejiuyeke.biteadminapi.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 参数服务相关远程调用
 */
@FeignClient(contextId = "argumentFeignClient", name = "bite-admin", path = "/argument")
public interface ArgumentFeignClient {

}
