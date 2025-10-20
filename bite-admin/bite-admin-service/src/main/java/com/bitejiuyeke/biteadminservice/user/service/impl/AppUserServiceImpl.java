package com.bitejiuyeke.biteadminservice.user.service.impl;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserListReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteadminservice.user.config.RabbitConfig;
import com.bitejiuyeke.biteadminservice.user.domain.entity.AppUser;
import com.bitejiuyeke.biteadminservice.user.mapper.AppUserMapper;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     * 消息队列
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    /**
     * 修改用户信息
     * @param userEditReqDTO
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        //初始化
        AppUser appUser = new AppUser();
        appUser.setId(userEditReqDTO.getUserId());
        appUser.setNickName(userEditReqDTO.getNickName());
        appUser.setAvatar(userEditReqDTO.getAvatar());
        appUserMapper.updateById(appUser);

        //发送消息通知消费方
        AppUser appUser1 = appUserMapper.selectById(userEditReqDTO.getUserId());
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser1, appUserDTO);
        appUserDTO.setUserId(appUser1.getId());
        //通过 RabbitMQ 发送消息通知其他服务，告知用户信息已修改。
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"",appUserDTO);
        } catch (Exception e) {
            log.error("生产者发送修改用户消息异常", e);
        }
    }

    /**
     * 获取用户列表
     * @param appUserListReqDTO
     * @return
     */
    @Override
    public BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO) {
        AppUserListReqDTO reqDTO = new AppUserListReqDTO();
        BeanUtils.copyProperties(appUserListReqDTO, reqDTO);

        reqDTO.setPhoneNumber(AESUtil.encryptHex(appUserListReqDTO.getPhoneNumber()));

        BasePageDTO<AppUserDTO> result = new BasePageDTO<>();
        Long total = appUserMapper.selectCount(reqDTO);
        if(0 == total){
            //无数据
            result.setTotals(0);
            result.setTotalPages(0);
            log.info("查询⼈员列表为空，totals:{}, totalPages:{}, pageNo:{}, pageSize:{}",
            result.getTotals(), result.getTotalPages(),
                    reqDTO.getPageNo(), reqDTO.getPageSize());
            result.setList(Arrays.asList());
            return result;
        }
        List<AppUser> appUserList = appUserMapper.selectPage(reqDTO);
        result.setTotals(Integer.parseInt(String.valueOf(total)));
        result.setTotalPages(BasePageDTO.calculateTotalPages(total, reqDTO.getPageSize()));
        if(CollectionUtils.isEmpty(appUserList)){
            log.info("超出查询⼈员列表范围，totals:{}, totalPages:{}, pageNo:{}, pageSize:{}",
            result.getTotals(), result.getTotalPages(),
                    reqDTO.getPageNo(), reqDTO.getPageSize());
            result.setList(Arrays.asList());
            return result;
        }
        result.setList(
                appUserList.stream().map(appUser -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    appUserDTO.setUserId(appUser.getId());
                    appUserDTO.setNickName(appUser.getNickName());
                    appUserDTO.setPhoneNumber(AESUtil.decryptStr(appUser.getPhoneNumber()));
                    appUserDTO.setAvatar(appUser.getAvatar());
                    appUserDTO.setOpenId(appUser.getOpenId());
                    return appUserDTO;
                }).collect(Collectors.toList())
        );
        return result;
    }

    @Override
    public AppUserDTO findById(Long userId) {
        if(null == userId){
            return null;
        }

        AppUser appUser = appUserMapper.selectById(userId);
        if(null == appUser){
            log.error("查询用户信息为空，userId:{}", userId);
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(AESUtil.decryptStr(appUser.getPhoneNumber()));
        appUserDTO.setAvatar(appUser.getAvatar());
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }

    /**
     * 批量获取用户列表
     * @param userIds
     * @return
     */
    @Override
    public List<AppUserDTO> getUserList(List<Long> userIds) {
        if(CollectionUtils.isEmpty(userIds)){
            return Arrays.asList();
        }
        List<AppUser> appUserList = appUserMapper.selectBatchIds(userIds);

        return appUserList.stream()
                .map(appUser -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    BeanUtils.copyProperties(appUser, appUserDTO);
                    appUserDTO.setPhoneNumber(AESUtil.decryptStr(appUser.getPhoneNumber()));
                    appUserDTO.setOpenId(appUser.getOpenId());
                    return appUserDTO;
                }).collect(Collectors.toList());
    }



}
