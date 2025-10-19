package com.bitejiuyeke.biteportalservice.user.controller;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.WechatLoginDTO;
import com.bitejiuyeke.biteportalservice.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
