package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserListReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.vo.SysUserLoginVO;
import com.bitejiuyeke.biteadminservice.user.domain.vo.SysUserVo;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.TokenVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys_user")
public class SysUserController{
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login/password")
    public R<TokenVO> login (@Validated @RequestBody PasswordLoginDTO loginDTO){
        TokenDTO tokenDTO = sysUserService.login(loginDTO);
        return R.ok(tokenDTO.convertToVO());
    }

    /**
     * 新增与编辑接口
     * @param sysUserDTO
     * @return
     */
    @PostMapping("/add_edit")
    public R<Long> addOrEditUser (@RequestBody SysUserDTO sysUserDTO){
        Long userId = sysUserService.addOrEditUser(sysUserDTO);
        return R.ok(userId);
    }

    /**
     * 获取用户列表
     * @param  sysUserListReqDTO
     * @return
     */
    @PostMapping("/list")
    public R<List<SysUserVo>> getUserList(@RequestBody SysUserListReqDTO
                                                  sysUserListReqDTO) {
        List<SysUserDTO> sysUserDTOS =
                sysUserService.getUserList(sysUserListReqDTO);
        return R.ok(sysUserDTOS.stream()
                .map(SysUserDTO::convertToVO)
                .collect(Collectors.toList()));
    }

    /**
     * 获取登录用户信息
     * @return
     */
    @GetMapping("/login_info/get")
    public R<SysUserLoginVO> getLoginUser(){
        return R.ok(sysUserService.getLoginUser().convertToVO());
    }

}
