package com.bitejiuyeke.biteadminapi.appuser.feign;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @GetMapping("/phone_find")
    R<AppUserVO> findByPhone(@RequestParam String phoneNumber);


    /**
     * 根据手机号注册用户
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @GetMapping("/register/phone")
    R<AppUserVO> registerByPhone(@RequestParam String phoneNumber);

    /**
     * 编辑⽤⼾
     *
     * @param userEditReqDTO ⽤⼾编辑DTO
     * @return void类型
     */
    @PostMapping("/edit")
    R<Void> edit(@Validated @RequestBody UserEditReqDTO userEditReqDTO);
}
