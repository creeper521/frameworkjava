package com.bitejiuyeke.biteadminapi.appuser.feign;

import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "appUserFeignClient", value = "bite-admin", path = "/app_user")
public interface AppUserFeignClient {
    /**
     * 根据openId查询⽤⼾信息
     *
     * @param openId ⽤⼾微信ID
     * @return C端⽤⼾VO
     */
    @GetMapping("/open_id_find")
    R<AppUserVO> findByOpenId(@RequestParam String openId);

    /**
     * 微信注册
     * @param openId ⽤⼾微信ID
     * @return C端⽤⼾VO
     */
    @GetMapping("/register/openid")
    R<AppUserVO> registerByOpenId(@RequestParam String openId);
}
