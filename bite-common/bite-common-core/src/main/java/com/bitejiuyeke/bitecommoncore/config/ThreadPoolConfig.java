package com.bitejiuyeke.bitecommoncore.config;

import com.bitejiuyeke.bitecommoncore.enums.RejectType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程池
     */
    @Value("${thread.pool-executor.corePoolSize:5}")
    private Integer corePoolSize;

    /**
     * 最大线程池
     */
    @Value("${thread.pool-executor.maxPoolSize:100}")
    private Integer maxPoolSize;

    /**
     * 队列大小
     */
    @Value("${thread.pool-executor.queueCapacity:100}")
    private Integer queueCapacity;

    /**
     * 空闲存活时间
     */
    @Value("${thread.pool-executor.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;

    /**
     * 线程前缀
     */
    @Value("${thread.pool-executor.prefixName:thread-service-}")
    private String prefixName;

    /**
     * 拒绝策略
     */
    @Value("${thread.pool-executor.rejectedHandler:2}")
    private String rejectedHandler;

    /**
     * 注册和配置线程执行器
     *
     * @return 线程池执行器
     */
    @Bean("threadPoolTaskExecutor")
    public Executor getThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(prefixName);

        //策略
        executor.setRejectedExecutionHandler(getRejectedHandler());
        return executor;
    }

    /**
     * 拒绝策略
     *
     * @return 拒绝策略
     */
    public RejectedExecutionHandler getRejectedHandler() {
        if(RejectType.AbortPolicy.getValue().equals(rejectedHandler)){
            return new ThreadPoolExecutor.AbortPolicy();
        }else if (
                RejectType.CallerRunsPolicy.getValue().equals(rejectedHandler)
        ){
            return new ThreadPoolExecutor.CallerRunsPolicy();
        }else if(
                RejectType.DiscardOldestPolicy.getValue().equals(rejectedHandler)
        ){
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        }else {
            return new ThreadPoolExecutor.DiscardPolicy();
        }
    }
}
