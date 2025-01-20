package com.lc.framework.storage.oss;

import com.lc.framework.core.utils.ValidatorUtil;
import com.lc.framework.storage.adaptor.StoragePlatformAdaptor;
import com.lc.framework.storage.client.StorageClientFactory;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StorageClientTemplate.AmazonS3Wrapper;
import com.lc.framework.storage.adaptor.QiniuStoragePlatformAdaptor;
import com.lc.framework.storage.oss.async.OssAsyncClientTemplate;
import com.lc.framework.storage.core.BucketInfo;
import com.lc.framework.storage.core.OssStorageProperties;
import com.lc.framework.storage.oss.sync.OssClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.multipart.MultipartConfiguration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * <pre>
 *     StorageClientTemplate创建工厂
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:25
 */
@Slf4j
public class OssStorageClientFactory implements StorageClientFactory<OssStorageProperties> {

    private final ExecutorService executorService;

    public OssStorageClientFactory(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public StorageClientTemplate newInstance(OssStorageProperties properties) {
        // 转换为OssStorageProperties
        Assert.notNull(properties, "StorageProperties must not be bull");
        ValidatorUtil.validate(properties);
        // 构建AmazonS3
        Map<String, AmazonS3Wrapper<S3Client>> s3ClientMap = getS3WrapperMap(properties, this::createS3Client);
        // 设置默认Bucket
        AmazonS3Wrapper<S3Client> defaultS3Client = s3ClientMap.get(properties.getDefaultBucketName());
        log.info("StorageClientTemplate with S3Client created successfully");
        OssClientTemplate clientTemplate = new OssClientTemplate(this.newAdaptor(properties.getPlatform()), defaultS3Client);
        // 放入所有bucket
        clientTemplate.putAllS3Wrappers(s3ClientMap.values());
        return clientTemplate;
    }

    public StorageClientTemplate newAsyncInstance(OssStorageProperties properties) {
        log.info("当前线程池配置: {}", executorService);
        // 转换为OssStorageProperties
        Assert.notNull(properties, "StorageProperties must not be bull");
        ValidatorUtil.validate(properties);
        // 构建AmazonS3
        Map<String, AmazonS3Wrapper<S3AsyncClient>> s3ClientMap = getS3WrapperMap(properties, this::createS3AsyncClient);
        // 设置默认Bucket
        AmazonS3Wrapper<S3AsyncClient> defaultS3Client = s3ClientMap.get(properties.getDefaultBucketName());
        log.info("StorageClientTemplate with S3AsyncClient created successfully");
        OssAsyncClientTemplate ossAsyncClientTemplate = new OssAsyncClientTemplate(this.newAdaptor(properties.getPlatform()), defaultS3Client, executorService);
        ossAsyncClientTemplate.putAllS3Wrappers(s3ClientMap.values());
        return ossAsyncClientTemplate;

    }

    /**
     * 将全局属性赋予BucketInfo中未设置的属性
     *
     * @param global     全局属性
     * @param bucketInfo 当前bucket
     */
    private void mergeGlobalProperty(OssStorageProperties global, BucketInfo bucketInfo) {
        // 存储平台
        if (!StringUtils.hasText(bucketInfo.getPlatform())) {
            bucketInfo.setPlatform(global.getPlatform());
        }
        // 存储平台cdn
        if (!StringUtils.hasText(bucketInfo.getCdn())) {
            bucketInfo.setCdn(global.getCdn());
        }
        // ak
        if (!StringUtils.hasText(bucketInfo.getAccessKey())) {
            bucketInfo.setAccessKey(global.getAccessKey());
        }
        // sk
        if (!StringUtils.hasText(bucketInfo.getSecretKey())) {
            bucketInfo.setSecretKey(global.getSecretKey());
        }
        // endpoint
        if (!StringUtils.hasText(bucketInfo.getEndpoint())) {
            Assert.notNull(global.getDefaultDomainOfBucket(), "defaultDomainOfBucket must not be null when endpoint not set");
            String protocol = "http" +
                    (bucketInfo.isUseHttps() ? "s://" : "://") +
                    "s3." +
                    bucketInfo.getRegion() +
                    "." +
                    global.getDefaultDomainOfBucket();
            bucketInfo.setEndpoint(protocol);
        }
    }

    private <T extends AwsClient> Map<String, StorageClientTemplate.AmazonS3Wrapper<T>> getS3WrapperMap(OssStorageProperties properties, Function<BucketInfo, StorageClientTemplate.AmazonS3Wrapper<T>> wrapperCreator) {
        Map<String, StorageClientTemplate.AmazonS3Wrapper<T>> s3ClientMap = new ConcurrentHashMap<>();
        for (BucketInfo bucketInfo : properties.getBuckets()) {
            // 封装全局属性
            mergeGlobalProperty(properties, bucketInfo);
            // 校验bucket属性
            ValidatorUtil.validate(bucketInfo);
            // 创建AmazonS3客户端包装类
            StorageClientTemplate.AmazonS3Wrapper<T> s3ClientWrapper = wrapperCreator.apply(bucketInfo);
            s3ClientMap.put(bucketInfo.getName(), s3ClientWrapper);
            log.info("AmazonS3 client created, bucketName:{}, endpoint:{}", bucketInfo.getName(), bucketInfo.getEndpoint());
        }
        return s3ClientMap;
    }

    /**
     * TODO 放在工厂方法中，根据属性创建适配器
     * @param platform 存储平台
     * @return 适配器
     */
    private StoragePlatformAdaptor newAdaptor(String platform) {
        if ("qiniu".equals(platform)) {
            return new QiniuStoragePlatformAdaptor();
        }
        return null;
    }

    private AmazonS3Wrapper<S3Client> createS3Client(BucketInfo bucketInfo) {
        // http客户端配置
        SdkHttpClient httpClient = ApacheHttpClient.builder()
                .maxConnections(20)
                .connectionTimeout(Duration.ofMinutes(2))
                .connectionAcquisitionTimeout(Duration.ofMinutes(1)).build();

        // 创建S3客户端
        S3Client s3Client = S3Client.builder()
                // 区域
                .region(getRegion(bucketInfo))
                // 访问端点
                .endpointOverride(getEndpoint(bucketInfo))
                // 客户端
                .httpClient(httpClient)
                // 重写客户端属性
                .overrideConfiguration(getClientOverrideConfiguration())
                // 凭证
                .credentialsProvider(getCredentialsProvider(bucketInfo))
                // S3协议配置
                .serviceConfiguration(getS3Configuration(bucketInfo)).build();

        // 预签名，用于生成access url
        S3Presigner presigner = createPresigner(bucketInfo, s3Client);
        return new AmazonS3Wrapper<>(bucketInfo, s3Client, presigner);
    }

    private AmazonS3Wrapper<S3AsyncClient> createS3AsyncClient(BucketInfo bucketInfo) {

        // http客户端配置
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .maxConcurrency(20)
                .connectionTimeout(Duration.ofMinutes(10))
                .connectionAcquisitionTimeout(Duration.ofMinutes(10)).build();

        // 分片上传配置
        MultipartConfiguration multipartConfiguration = MultipartConfiguration.builder()
                .minimumPartSizeInBytes(1024L * 1024 * 10)
                .apiCallBufferSizeInBytes(1024L * 1024 * 256)
                .thresholdInBytes(1024L * 1024 * 16).build();
        // 创建S3客户端
        S3AsyncClient s3AsyncClient = S3AsyncClient.builder()
                // 区域
                .region(getRegion(bucketInfo))
                // 访问端点
                .endpointOverride(getEndpoint(bucketInfo))
                // 客户端
                .httpClient(httpClient)
                // 重写客户端属性
                .overrideConfiguration(getClientOverrideConfiguration())
                // 凭证
                .credentialsProvider(getCredentialsProvider(bucketInfo))
                // S3协议配置
                .serviceConfiguration(getS3Configuration(bucketInfo))
                .multipartEnabled(bucketInfo.isMultipartEnabled())
                .multipartConfiguration(multipartConfiguration)
                .asyncConfiguration(ac -> ac.advancedOption(SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR, executorService)).build();
        // 预签名
        S3Presigner presigner = createPresigner(bucketInfo, null);
        return new AmazonS3Wrapper<>(bucketInfo,s3AsyncClient, presigner);
    }

    /**
     * 获取区域
     * @param bucketInfo 存储桶信息
     * @return 区域
     */
    private Region getRegion(BucketInfo bucketInfo) {
        return Region.of(bucketInfo.getRegion());
    }

    /**
     * 获取endpoint，由对象存储厂商提供
     * @param bucketInfo 存储桶信息
     * @return endpoint
     */
    private URI getEndpoint(BucketInfo bucketInfo) {
        return URI.create(bucketInfo.getEndpoint());
    }

    /**
     * http客户端配置
     * @return 配置
     */
    private ClientOverrideConfiguration getClientOverrideConfiguration() {
        return ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofSeconds(300))
                .apiCallAttemptTimeout(Duration.ofSeconds(300))
                .retryStrategy(RetryMode.STANDARD).build();
    }

    /**
     * 创建静态凭证
     * @param bucketInfo 存储桶信息
     * @return 静态凭证
     */
    private AwsCredentialsProvider getCredentialsProvider(BucketInfo bucketInfo) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(bucketInfo.getAccessKey(), bucketInfo.getSecretKey()));
    }

    /**
     * S3客户端配置
     * @param bucketInfo 存储桶信息
     * @return S3客户端配置
     */
    private S3Configuration getS3Configuration(BucketInfo bucketInfo) {
        return S3Configuration.builder().pathStyleAccessEnabled(bucketInfo.isPathStyleEnabled()).build();
    }

    /**
     * 创建预签名
     * @param bucketInfo 存储桶信息
     * @param s3Client s3客户端
     * @return 预签名
     */
    private S3Presigner createPresigner(BucketInfo bucketInfo, S3Client s3Client) {

        return S3Presigner.builder()
                // 区域
                .region(getRegion(bucketInfo))
                // 访问端点
                .endpointOverride(getEndpoint(bucketInfo))
                // 客户端
                .s3Client(s3Client)
                // 凭证
                .credentialsProvider(getCredentialsProvider(bucketInfo))
                // S3协议配置
                .serviceConfiguration(getS3Configuration(bucketInfo)).build();
    }
}
