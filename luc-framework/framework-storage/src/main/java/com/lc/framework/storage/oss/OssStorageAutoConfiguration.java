package com.lc.framework.storage.oss;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.StorageProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import software.amazon.awssdk.utils.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * <pre>
 *     七牛云对象存储
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 13:45
 * @version : 1.0
 */
@AllArgsConstructor
@ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "enabled", havingValue = "true")
public class OssStorageAutoConfiguration {

    private StorageProperties storageProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "use-async", havingValue = "true", matchIfMissing = true)
    public ExecutorService ossAsyncExecutor() {
        // 核心线程数
        int corePoolSize = 10;
        // 最大线程数
        int maximumPoolSize = 16;
        // 非核心线程的最大空闲时间
        long keepAliveTime = 60;
        // 非核心线程的最大空闲时间单位
        TimeUnit timeUnit = TimeUnit.SECONDS;
        // 队列, 长度为最大线程数
        BlockingQueue<Runnable> fixedBlockingQueue = new LinkedBlockingQueue<>(maximumPoolSize);
        // 线程工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder().threadNamePrefix("s3-async-client_upload").build();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
                fixedBlockingQueue, threadFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OssStorageClientFactory ossStorageClientFactory(@Nullable ExecutorService executorService) {
        return new OssStorageClientFactory(executorService);
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(StorageClientTemplate.class)
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "use-async", havingValue = "false", matchIfMissing = true)
    public StorageClientTemplate storageClientTemplate(OssStorageClientFactory factory) {
        return factory.newInstance(storageProperties.getOss());
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(StorageClientTemplate.class)
    @ConditionalOnProperty(prefix = StorageProperties.PREFIX + ".oss", name = "use-async", havingValue = "true")
    public StorageClientTemplate asyncStorageClientTemplate(OssStorageClientFactory factory) {

        return factory.newAsyncInstance(storageProperties.getOss());
    }
}
