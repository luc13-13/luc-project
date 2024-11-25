package com.lc.framework.storage.core.oss;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.lc.framework.core.utils.ValidatorUtil;
import com.lc.framework.storage.client.StorageClientFactory;
import com.lc.framework.storage.client.StorageClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
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
            OssClientTemplate.AmazonS3Wrapper s3ClientWrapper = new OssClientTemplate.AmazonS3Wrapper(bucketInfo.getName(), createS3Client(bucketInfo));
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
            StringBuilder protocol = new StringBuilder();
            protocol.append("http")
                    .append(bucketInfo.isUseHttps() ? "s://" : "://");
            StringBuilder endpoint = new StringBuilder();
            endpoint.append("s3.")
                    .append(bucketInfo.getRegion())
                    .append(".")
                    .append(global.getDefaultDomainOfBucket());
            if (bucketInfo.isPathStyleEnabled()) {
                protocol.append(endpoint)
                        .append("/")
                        .append(bucketInfo.getName());
            } else {
                protocol.append(bucketInfo.getName())
                        .append(".")
                        .append(endpoint);
            }
            bucketInfo.setEndpoint(protocol.toString());
        }
    }

    private AmazonS3 createS3Client(BucketInfo bucketInfo) {
        // 1、客户端配置
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(bucketInfo.isUseHttps() ? Protocol.HTTPS : Protocol.HTTP);

        // 2、端点配置，注意，serviceEndpoint不包含bucketName
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                "https://s3.cn-south-1.qiniucs.com", RegionUtils.getRegion(bucketInfo.getRegion()).getName());

        // 3、凭证配置
        AWSCredentials credentials = new BasicAWSCredentials(bucketInfo.getAccessKey(), bucketInfo.getSecretKey());

        return AmazonS3Client.builder()
                .withClientConfiguration(clientConfiguration)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(bucketInfo.isPathStyleEnabled())
                .build();
    }
}
