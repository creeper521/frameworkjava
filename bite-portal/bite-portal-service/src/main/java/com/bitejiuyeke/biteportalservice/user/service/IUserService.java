package com.bitejiuyeke.biteportalservice.user.service;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.LoginDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.UserDTO;

public interface IUserService {
    /**
     * 登录
     * @param loginDTO
     * @return
     */
    TokenDTO login(LoginDTO loginDTO);

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    String sendCode(String phone);

    /**
     * 编辑用户
     * @param userEditReqDTO
     */
    void edit(UserEditReqDTO userEditReqDTO);

    /**
     * 获取当前登录用户
     * @return
     */
    UserDTO getLoginUser();

    /**
     * 登出
     */
    void logout();
}
