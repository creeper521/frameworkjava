package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;

public interface ISysUserService {
    /**
     * 登录
     * @param passwordLoginDTO
     * @return
     */
    TokenDTO login(PasswordLoginDTO passwordLoginDTO);
}
