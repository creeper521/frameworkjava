package com.bitejiuyeke.bitecommonmessage.service;

import com.bitejiuyeke.bitecommondomain.constants.MessageConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 */
@Service
@RefreshScope
public class CaptchaService {
    @Autowired
    private RedisService redisService;

    /**
     * 单个手机号，每日发送短信次数的限制
     */
    @Value("${sms.send-limit:}")
    private Integer sendLimit;

    /**
     * 验证码的有效期，单位是分钟
     */
    @Value("${sms.code-expiration:}")
    private Long phoneCodeExpiration;

    public String sendCode(String phone){
        //先判断是否超过每日发送限制
        String limitCacheKey = MessageConstants.SMS_CODE_TIMES_KEY + phone;
        Integer times = redisService.getCacheObject(limitCacheKey, Integer.class);

        times = times == null ? 0 : times;

        if(times >= sendLimit){
            throw new ServiceException("今天发送验证码的次数已经超过限制！", ResultCode.SEND_MSG_FAILED.getCode());
        }

        //判断是否在1分钟内频繁发送
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        String cacheValue = redisService.getCacheObject(cacheKey, String.class);
        long expire = redisService.getExpire(cacheKey);
        if(!StringUtils.isEmpty(cacheKey) && expire > phoneCodeExpiration * 60 - 60){
            long time = expire - phoneCodeExpiration * 60 + 60;
            throw new ServiceException("操作频繁， 请在"+ time+ "秒之后再试", ResultCode.INVALID_PARA.getCode());
        }

        //控制台打印验证码
        System.out.println("验证码：" + MessageConstants.DEFAULT_SMS_CODE);

        // 缓存验证码
        redisService.setCacheObject(cacheKey, MessageConstants.DEFAULT_SMS_CODE, phoneCodeExpiration, TimeUnit.MINUTES);

        // 设置发送时间和次数限制的缓存 （无法预先设置缓存，只能先读后写）
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        redisService.setCacheObject(limitCacheKey, times + 1, seconds, TimeUnit.SECONDS);
        return MessageConstants.DEFAULT_SMS_CODE;
    }


    /**
     * 从缓存中获取手机号的验证码
     * @param phone 手机号
     * @return 验证码
     */
    public String getCode(String phone) {
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        return redisService.getCacheObject(cacheKey, String.class);
    }

    /**
     * 从缓存中删除手机号的验证码
     * @param phone 手机号
     * @return 验证码
     */
    public boolean deleteCode(String phone) {
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        return redisService.deleteObject(cacheKey);
    }

    /**
     * 校验手机号与验证码是否匹配
     * @param phone 手机号
     * @param code 验证码
     * @return 布尔类型
     */
    public boolean checkCode(String phone, String code) {
        if (getCode(phone) == null || StringUtils.isEmpty(getCode(phone))) {
            throw new ServiceException(ResultCode.INVALID_CODE);
        }
        return getCode(phone).equals(code);
    }

}
