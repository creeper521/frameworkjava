package com.bitejiuyeke.biteadminservice.user.mapper;

import com.bitejiuyeke.biteadminservice.user.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper {
    SysUser selectByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
