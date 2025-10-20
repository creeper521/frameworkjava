package com.bitejiuyeke.biteportalservice.user.controller;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.CodeLoginDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.WechatLoginDTO;
import com.bitejiuyeke.biteportalservice.user.entity.vo.UserVO;
import com.bitejiuyeke.biteportalservice.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login/wechat")
    public R<TokenVO> login(@Validated @RequestBody WechatLoginDTO wechatLoginDTO){
        TokenDTO tokenDTO = userService.login(wechatLoginDTO);
        return R.ok(tokenDTO.convertToVO());
    }

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 验证码
     */
    @GetMapping("/send_code")
    public R<String> sendCode(String phone) {
        return R.ok(userService.sendCode(phone));
    }

    /**
     * 验证码登录
     * @param codeLoginDTO 验证码登录信息
     * @return token信息VO
     */
    @PostMapping("/login/code")
    public R<TokenVO> login(@RequestBody @Validated CodeLoginDTO codeLoginDTO) {
        return R.ok(userService.login(codeLoginDTO).convertToVO());
    }

    /**
     * 修改用户信息
     * @param userEditReqDTO C端用户编辑DTO
     * @return void
     */
    @PostMapping("/edit")
    public R<Void> edit(@RequestBody @Validated UserEditReqDTO userEditReqDTO) {
        userService.edit(userEditReqDTO);
        return R.ok();
    }

    /**
     * 获取登录用户信息
     * @return C端用户信息VO
     */
    @GetMapping("/login_info/get")
    public R<UserVO> getLoginInfo() {
        return R.ok(userService.getLoginUser().convertToVO());
    }

    /**
     * 登出
     * @return void
     */
    @DeleteMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.ok();
    }

}
