package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;

public interface IAppUserService {
    /**
     *微信注册
     *
     * @param openId ⽤⼾微信ID
     * @return C端⽤⼾DTO
     */
    AppUserDTO registerByOpenId(String openId);

    /**
     * 根据⽤⼾微信id查询
     * @param openId ⽤⼾微信ID
     * @return C端⽤⼾DTO
     */
    AppUserDTO findByOpenId(String openId);
}