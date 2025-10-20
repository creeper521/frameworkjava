package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminapi.appuser.feign.AppUserFeignClient;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/app_user")
public class AppUserController implements AppUserFeignClient {

    /**
     * 用户服务
     */
    @Autowired
    public IAppUserService appUserService;

//    @Value("${appuser.info.default-Avatar}")
//    private String defaultAvatar;

    /**
     * 通过openId查询用户信息
     * @param openId
     * @return
     */
    @Override
    public R<AppUserVO> findByOpenId(@RequestParam String openId) {
        AppUserDTO appUserDTO = appUserService.findByOpenId(openId);
        return null == appUserDTO ? R.ok() : R.ok(appUserDTO.convertToVO());
    }

    /**
     * 通过openId注册用户
     * @param openId
     * @return
     */
    @Override
    public R<AppUserVO> registerByOpenId(String openId) {
        AppUserDTO appUserDTO = appUserService.registerByOpenId(openId);
        if (null == appUserDTO) {
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findByPhone(String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.findByPhone(phoneNumber);
        if (appUserDTO == null) {
            return R.ok();
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据手机号注册用户
     * @param phoneNumber 手机号
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByPhone(String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.registerByPhone(phoneNumber);
        if (appUserDTO == null) {
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 编辑用户
     * @param userEditReqDTO
     * @return
     */
    @Override
    public R<Void> edit(UserEditReqDTO userEditReqDTO) {
        appUserService.edit(userEditReqDTO);
        return R.ok();
    }

    @Override
    public R<AppUserVO> findById(Long userId) {
        AppUserDTO appUserDTO = appUserService.findById(userId);
        return appUserDTO == null ? R.ok() : R.ok(appUserDTO.convertToVO());
    }

    @Override
    public R<List<AppUserVO>> list(@RequestBody List<Long> userIds) {
        List<AppUserDTO> appUserDTOS = appUserService.getUserList(userIds);
        return R.ok(appUserDTOS.stream()
                        .filter(Objects::nonNull)
                        .map(AppUserDTO::convertToVO)
                        .collect(Collectors.toList())
        );
    }

}
