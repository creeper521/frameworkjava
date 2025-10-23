package com.bitejiuyeke.biteadminservice.user.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserListReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserLoginDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RefreshScope
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

    /**
     * 字典服务
     */
    @Autowired
    private ISysDictionaryService sysDictionaryService;

    /**
     * 登录
     * @param passwordLoginDTO
     * @return
     */
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
        //
        String passwordEncrypt = DigestUtil.sha256Hex(password);
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("账号密码错误，请确认后重新登录",
                    ResultCode.INVALID_PARA.getCode());
        }
        // 3、校验用户状态
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

    /**
     * 新增与编辑接口的实现方法
     * @param sysUserDTO B端用户信息DTO
     * @return 用户ID
     */
    @Override
    public Long addOrEditUser(SysUserDTO sysUserDTO) {
        // 1 创建一个空的SysUser对象
        SysUser sysUser = new SysUser();

        // 2 先处理新增的逻辑
        if (sysUserDTO.getUserId() == null) {
            // 3 先校验手机号
            if (!VerifyUtil.checkPhone(sysUserDTO.getPhoneNumber())) {
                throw new ServiceException("手机格式错误", ResultCode.INVALID_PARA.getCode());
            }
            // 4 校验密码
            if (StringUtils.isEmpty(sysUserDTO.getPassword()) || !sysUserDTO.checkPassword()) {
                throw new ServiceException("密码校验失败", ResultCode.INVALID_PARA.getCode());
            }
            // 5 手机号唯一性判断
            SysUser existSysUser = sysUserMapper.selectByPhoneNumber(AESUtil.encryptHex(sysUserDTO.getPhoneNumber()));
            if (existSysUser != null) {
                throw new ServiceException("手机号已经被占用", ResultCode.INVALID_PARA.getCode());
            }
            // 6 判断身份信息
            if (StringUtils.isEmpty(sysUserDTO.getIdentity()) || sysDictionaryService.getDicDataByKey(sysUserDTO.getIdentity()) == null) {
                throw new ServiceException("用户身份错误", ResultCode.INVALID_PARA.getCode());
            }
            // 7 判断完成后，执行新增用户逻辑
            sysUser.setPhoneNumber(
                    AESUtil.encryptHex(sysUserDTO.getPhoneNumber())
            );
            sysUser.setPassword(
                    DigestUtil.sha256Hex(sysUserDTO.getPassword())
            );
            sysUser.setIdentity(sysUserDTO.getIdentity());
        }
        sysUser.setId(sysUserDTO.getUserId());
        sysUser.setNickName(sysUserDTO.getNickName());
        // 8 判断用户状态
        if (sysDictionaryService.getDicDataByKey(sysUserDTO.getStatus()) == null) {
            throw new ServiceException("用户状态错误", ResultCode.INVALID_PARA.getCode());
        }
        sysUser.setStatus(sysUserDTO.getStatus());
        sysUser.setRemark(sysUserDTO.getRemark());
        sysUserMapper.insertOrUpdate(sysUser);

        // 9 踢人
        if (sysUserDTO.getUserId() != null && sysUserDTO.getStatus().equals("disable")) {
            tokenService.delLoginUser(sysUserDTO.getUserId(), "sys");
        }
        return sysUser.getId();
    }

    /**
     * 获取用户列表
     * @param sysUserListReqDTO
     * @return
     */
    @Override
    public List<SysUserDTO> getUserList(SysUserListReqDTO sysUserListReqDTO) {
        SysUser searchSysUser = new SysUser();
        searchSysUser.setId(sysUserListReqDTO.getUserId());
        searchSysUser.setStatus(sysUserListReqDTO.getStatus());
        if(StringUtils.isNotEmpty(sysUserListReqDTO.getPhoneNumber())){
            searchSysUser.setPhoneNumber(
                    AESUtil.encryptHex(sysUserListReqDTO.getPhoneNumber())
            );
        }
        List<SysUser> sysUserList = sysUserMapper.selectList(searchSysUser);
        return sysUserList.stream()
                .map(sysUser -> {
                    SysUserDTO sysUserDTO = new SysUserDTO();
                    sysUserDTO.setUserId(sysUser.getId());
                    sysUserDTO.setPhoneNumber(AESUtil.decryptStr(sysUser.getPhoneNumber()));
                    sysUserDTO.setNickName(sysUser.getNickName());
                    sysUserDTO.setRemark(sysUser.getRemark());
                    sysUserDTO.setStatus(sysUser.getStatus());
                    sysUserDTO.setIdentity(sysUser.getIdentity());
                    return sysUserDTO;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public SysUserLoginDTO getLoginUser() {
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();
        if(null == loginUserDTO || null == loginUserDTO.getUserId()){
            throw new ServiceException("用户token有误", ResultCode.INVALID_PARA.getCode());
        }

        //获取用户信息
        SysUser sysUser = sysUserMapper.selectById(loginUserDTO.getUserId());
        if(null == sysUser){
            throw new ServiceException("获取用户信息失败");
        }
        SysUserLoginDTO sysUserLoginDTO = new SysUserLoginDTO();
        sysUserLoginDTO.setNickName(sysUser.getNickName());
        sysUserLoginDTO.setIdentity(sysUser.getIdentity());
        sysUserLoginDTO.setStatus(sysUser.getStatus());
        sysUserLoginDTO.setToken(loginUserDTO.getToken());
        sysUserLoginDTO.setUserId(loginUserDTO.getUserId());
        sysUserLoginDTO.setUserName(loginUserDTO.getUserName());
        sysUserLoginDTO.setLoginTime(loginUserDTO.getLoginTime());
        sysUserLoginDTO.setExpireTime(loginUserDTO.getExpireTime());
        return sysUserLoginDTO;
    }
}
