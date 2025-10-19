package com.bitejiuyeke.biteadminservice.user.service.impl;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        appUser.setNickName("租房⽤⼾" + (int)((Math.random() * 9000) +
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

    /**
     * 根据微信ID查询⽤⼾信息
     * @param openId ⽤⼾微信ID
     * @return
     */
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

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        //先判空
        if(StringUtils.isEmpty(phoneNumber)){
            return null;
        }
        AppUser appUser = appUserMapper.selectByPhoneNumber(AESUtil.encryptHex(phoneNumber));
        if(null == appUser){
            return null;
        }

        //对查出来的结果进行类型转换
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setPhoneNumber(AESUtil.encryptHex(appUser.getPhoneNumber()));
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }

    /**
     * 根据手机号注册用户
     * @param phoneNumber 手机号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO registerByPhone(String phoneNumber) {
        // 1 对手机号进行判断
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new ServiceException("要注册手机号是空的", ResultCode.INVALID_PARA.getCode());
        }
        // 2 生成用户对象
        AppUser appUser = new AppUser();
        appUser.setPhoneNumber(AESUtil.encryptHex(phoneNumber));
        appUser.setNickName("比特用户"+ (int) (Math.random() * 9000) + 1000);
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        return appUserDTO;
    }

}
