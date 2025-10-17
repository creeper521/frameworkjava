package com.bitejiuyeke.biteadminservice.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import org.apache.ibatis.annotations.Param;

public interface AppUserMapper extends BaseMapper<AppUser> {

    /**
     * 根据微信ID查询
     * @param openId 微信ID
     * @return AppUser
     */
    AppUser selectByOpenId(@Param("openId") String openId);
}
