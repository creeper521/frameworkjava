package com.bitejiuyeke.biteportalservice.user.service;

import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.LoginDTO;

public interface IUserService {
    /**
     * 登录
     * @param loginDTO
     * @return
     */
    TokenDTO login(LoginDTO loginDTO);
}
