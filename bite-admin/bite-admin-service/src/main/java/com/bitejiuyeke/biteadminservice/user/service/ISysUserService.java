package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;

public interface ISysUserService {
    /**
     * 登录
     * @param passwordLoginDTO
     * @return
     */
    TokenDTO login(PasswordLoginDTO passwordLoginDTO);

    /**
     * 添加或修改用户
     * 修改只能修改昵称，备注，状态，逻辑为直接覆盖原有数据
     * @param sysUserDTO
     * @return
     */
    Long addOrEditUser(SysUserDTO sysUserDTO);

}
