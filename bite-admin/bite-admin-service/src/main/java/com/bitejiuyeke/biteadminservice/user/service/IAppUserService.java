package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserListReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.bitecommoncore.domain.dto.BasePageDTO;

import java.util.List;

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

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    AppUserDTO findByPhone(String phoneNumber);

    /**
     * 根据手机号注册用户
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    AppUserDTO registerByPhone(String phoneNumber);

    /**
     * 编辑用户
     *
     * @param userEditReqDTO ⽤⼾编辑DTO
     * @return void类型
     */
    void edit(UserEditReqDTO userEditReqDTO);

    /**
     * 获取用户列表
     *
     * @param appUserListReqDTO ⽤⼾列表查询参数
     * @return BasePageDTO<AppUserDTO>
     */
    BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO);

    /**
     * 批量获取用户列表
     * @param userIds
     * @return
     */

    List<AppUserDTO> getUserList(List<Long> userIds);

    /**
     * 根据ID查询用户信息
     * @param userId
     * @return
     */
    AppUserDTO findById(Long userId);

}