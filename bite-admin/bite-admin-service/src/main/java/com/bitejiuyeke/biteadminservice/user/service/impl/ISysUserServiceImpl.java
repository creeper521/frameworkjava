package com.bitejiuyeke.biteadminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.nacos.api.model.v2.ErrorCode;
import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.entity.SysUser;
import com.bitejiuyeke.biteadminservice.user.mapper.SysUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ISysUserServiceImpl implements ISysUserService {
    /**
     * token服务
     */
    @Autowired
    private TokenService tokenService;

    /**
     * 用户Mapper
     */
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public TokenDTO login(PasswordLoginDTO passwordLoginDTO) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        //检验手机号
        if(!VerifyUtil.checkPhone(passwordLoginDTO.getPhone())){
            throw new ServiceException("手机号格式非法",
                    ResultCode.INVALID_PARA.getCode());
        }

        //判断手机号是否存在mysql表中
        SysUser sysUser = sysUserMapper.selectByPhoneNumber(
                AESUtil.encryptHex(passwordLoginDTO.getPhone())
        );
        if(sysUser == null){
            throw new ServiceException("手机号不存在",
                    ResultCode.INVALID_PARA.getCode());
        }

        //校验密码
        //先解密
        String password = AESUtil.decryptStr(passwordLoginDTO.getPassword());
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码加密错误，请确认后重新登录",
                    ResultCode.INVALID_PARA.getCode());
        }
        String passwordEncrypt = DigestUtil.sha256Hex(password);
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("账号密码错误，请确认后重新登录",
                    ResultCode.INVALID_PARA.getCode());
        }
// 3、校验⽤⼾状态
        if ("disable".equalsIgnoreCase(sysUser.getStatus())) {
            throw new ServiceException(ResultCode.USER_DISABLE);
        }
// 4、设置登录信息
        loginUserDTO.setUserId(sysUser.getId());
        loginUserDTO.setUserName(sysUser.getNickName());
        loginUserDTO.setUserFrom("sys");
// 该⽅法会设置⽤⼾token、⽣命周期，并返回 Token
        return tokenService.createToken(loginUserDTO);
    }
}
