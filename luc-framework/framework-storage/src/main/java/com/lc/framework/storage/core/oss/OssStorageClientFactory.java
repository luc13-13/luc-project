package com.lc.framework.storage.core.oss;

import com.lc.framework.core.utils.ValidatorUtil;
import com.lc.framework.storage.client.StorageClientFactory;
import com.lc.framework.storage.client.StorageClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    @Override
    public StorageClientTemplate newInstance(OssStorageProperties properties) {
        // 转换为OssStorageProperties
        Assert.notNull(properties, "StorageProperties must not be bull");
        ValidatorUtil.validate(properties);
        // 构建AmazonS3
        Map<String, OssClientTemplate.AmazonS3Wrapper> s3ClientMap = new ConcurrentHashMap<>();
        for (BucketInfo bucketInfo : properties.getBuckets()) {
            // 封装全局属性
            mergeGlobalProperty(properties, bucketInfo);
            // 校验bucket属性
            ValidatorUtil.validate(bucketInfo);
            // 创建AmazonS3
            OssClientTemplate.AmazonS3Wrapper s3ClientWrapper = new OssClientTemplate.AmazonS3Wrapper(bucketInfo.getEndpoint(), bucketInfo.getName(), createS3Client(bucketInfo));
            s3ClientMap.put(bucketInfo.getName(), s3ClientWrapper);
            log.info("AmazonS3 client created, bucketName:{}, endpoint:{}", bucketInfo.getName(), bucketInfo.getEndpoint());
        }
        OssClientTemplate.AmazonS3Wrapper defaultS3Client = s3ClientMap.get(properties.getDefaultBucketName());
        return new OssClientTemplate(defaultS3Client, s3ClientMap);
    }

    private void mergeGlobalProperty(OssStorageProperties global, BucketInfo bucketInfo) {
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

    private S3Client createS3Client(BucketInfo bucketInfo) {
        // 1、S3客户端配置
        ClientOverrideConfiguration clientConfiguration = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofSeconds(60))
                .apiCallAttemptTimeout(Duration.ofSeconds(60))
                .retryStrategy(RetryMode.STANDARD)
                .build();

        // 2、http客户端配置
        SdkHttpClient httpClient = ApacheHttpClient.builder()
                .maxConnections(20)
                .connectionTimeout(Duration.ofMinutes(2))
                .connectionAcquisitionTimeout(Duration.ofMinutes(1))
                .build();

        // 3、凭证配置
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(bucketInfo.getAccessKey(), bucketInfo.getSecretKey()));

        // 4、S3协议配置
        S3Configuration s3Configuration = S3Configuration.builder().pathStyleAccessEnabled(bucketInfo.isPathStyleEnabled()).build();

        return S3Client.builder()
                .region(Region.of(bucketInfo.getRegion()))
                .overrideConfiguration(clientConfiguration)
                .endpointOverride(URI.create(bucketInfo.getEndpoint()))
                .credentialsProvider(credentialsProvider)
                .httpClient(httpClient)
                .serviceConfiguration(s3Configuration)
                .build();
    }
}
