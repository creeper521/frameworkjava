package com.bitejiuyeke.biteadminservice.user.service.impl;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
@RefreshScope
public class AppUserServiceImpl implements IAppUserService {

    /**
     * C端用户表对应的Mapper
     */
    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 默认头像
     */
    @Value("${appuser.info.defaultAvatar}")
    private String defaultAvatar;

    /**
     *  微信注册
     * @param openId ⽤⼾微信ID
     * @return
     */
    @Override
    public AppUserDTO registerByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException("要注册的openId为空！");
        }
        AppUser appUser = new AppUser();
        appUser.setOpenId(openId);
        // ⽣成1000到9999之间的随机数
        appUser.setNickName("⽐特租房⽤⼾" + (int)((Math.random() * 9000) +
                1000));
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setOpenId(openId);
        appUserDTO.setAvatar(appUser.getAvatar());
        return appUserDTO;
    }

    @Override
    public AppUserDTO findByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        AppUser appUser = appUserMapper.selectByOpenId(openId);
        if (null == appUser) {
            log.error("查询⼈员信息失败！openId:{}", openId);
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(
                AESUtil.decryptStr(appUser.getPhoneNumber()));
        appUserDTO.setAvatar(appUser.getAvatar());
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }
}
