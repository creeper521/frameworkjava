package com.bitejiuyeke.bitecommonrabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * RabbitMq 配置类
 */
@Configuration
public class RabbitMqCommonConfig {

    /**
     * json 序列化转换器
     * @return 序列化转换器
     */
    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
