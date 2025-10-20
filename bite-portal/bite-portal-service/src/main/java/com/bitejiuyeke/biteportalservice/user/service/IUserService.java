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

    String sendCode(String phone);

    void edit(UserEditReqDTO userEditReqDTO);

    UserDTO getLoginUser();

    void logout();
}
