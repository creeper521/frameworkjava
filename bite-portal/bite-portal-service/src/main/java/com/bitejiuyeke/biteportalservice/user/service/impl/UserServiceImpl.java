package com.bitejiuyeke.biteportalservice.user.service.impl;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.appuser.feign.AppUserFeignClient;
import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonmessage.service.CaptchaService;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import com.bitejiuyeke.biteportalservice.user.entity.dto.CodeLoginDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.LoginDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.UserDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.WechatLoginDTO;
import com.bitejiuyeke.biteportalservice.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceImpl implements IUserService {

    /**
     * 令牌服务
     */
    @Autowired
    private TokenService tokenService;

    /**
     * C端用户服务
     */
    @Autowired
    private AppUserFeignClient appUserFeignClient;

    /**
     * 验证码服务
     */
    @Autowired
    private CaptchaService captchaService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        if (loginDTO instanceof CodeLoginDTO codeLoginDTO) {
            loginByCode(codeLoginDTO, loginUserDTO);
        } else if (loginDTO instanceof WechatLoginDTO wechatLoginDTO) {
            loginByWechat(wechatLoginDTO, loginUserDTO);
        } else {
            throw new ServiceException("⽆效的登录⽅式！",
                    ResultCode.INVALID_PARA.getCode());
        }
        // 该⽅法会设置⽤⼾token、⽣命周期，并返回 Token
        loginUserDTO.setUserFrom("app");
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 微信登录
     *
     * @param wechatLoginDTO
     * @param loginUserDTO
     */
    private void loginByWechat(WechatLoginDTO wechatLoginDTO, LoginUserDTO loginUserDTO) {
        AppUserVO appUserVO;
        // 根据openId查询用户信息
        R<AppUserVO> result = appUserFeignClient.findByOpenId(wechatLoginDTO.getOpenId());
        if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
            // 用户不存在，则注册
            appUserVO = register(wechatLoginDTO);
        }else {
            appUserVO = result.getData();
        }
        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());
    }


    /**
     * 验证码登录
     * @param codeLoginDTO
     * @param loginUserDTO
     */
    private void loginByCode(CodeLoginDTO codeLoginDTO, LoginUserDTO loginUserDTO) {
        // 1 校验手机号
        if (!VerifyUtil.checkPhone(codeLoginDTO.getPhone())) {
            throw new ServiceException("手机号格式错误", ResultCode.INVALID_PARA.getCode());
        }
        // 2 执行远程调用
        AppUserVO appUserVO;

        R<AppUserVO> result = appUserFeignClient.findByPhone(codeLoginDTO.getPhone());
        // 3 查不到人的处理逻辑
        if (result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null) {
            appUserVO = register(codeLoginDTO);
        } else {
            appUserVO = result.getData();
        }
        // 4 校验验证码
        String cacheCode = captchaService.getCode(codeLoginDTO.getPhone());
        if (cacheCode == null) {
            throw new ServiceException("验证码无效", ResultCode.INVALID_PARA.getCode());
        }
        if (!cacheCode.equals(codeLoginDTO.getCode())) {
            throw new ServiceException("验证码错误", ResultCode.INVALID_PARA.getCode());
        }
        // 5 校验验证码通过
        captchaService.deleteCode(codeLoginDTO.getPhone());
        // 6 设置登录信息
        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());
    }

    /**
     * 注册
     * @param loginDTO
     * @return
     */
    private AppUserVO register(LoginDTO loginDTO) {
        R<AppUserVO> result = null;
        if(loginDTO instanceof WechatLoginDTO wechatLoginDTO){
            result = appUserFeignClient.registerByOpenId(wechatLoginDTO.getOpenId());
            if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
                log.error("用户注册失败! {}", wechatLoginDTO.getOpenId());
            }
        }else if(loginDTO instanceof CodeLoginDTO codeLoginDTO){
            result = appUserFeignClient.registerByPhone(codeLoginDTO.getPhone());
            if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
                log.error("用户注册失败! {}", codeLoginDTO.getPhone());
            }
        }
        return result == null ? null : result.getData();
    }

    @Override
    public String sendCode(String phone) {
        return null;
    }

    /**
     * 修改用户信息
     * @param userEditReqDTO C端用户编辑DTO
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        R<Void> result = appUserFeignClient.edit(userEditReqDTO);
        if (result == null || result.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException("修改用户失败");
        }
    }

    @Override
    public UserDTO getLoginUser() {
        return null;
    }

    @Override
    public void logout() {

    }
}
