package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StoragePlatformAdaptor;
import com.lc.framework.storage.client.StorageResult;
import com.lc.framework.storage.core.StorageConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

/**
 * <pre>
 *     s3规则的对象存储平台，例如七牛、阿里云、minio
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 13:50
 */
@Slf4j
public class OssClientTemplate implements StorageClientTemplate, InitializingBean {

    @Setter
    private StoragePlatformAdaptor storagePlatformAdaptor;

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    private final AmazonS3Wrapper<S3Client> defaultS3;

    /**
     * 每个bucket的s3客户端
     */
    private final Map<String, AmazonS3Wrapper<S3Client>> s3Map;

    public OssClientTemplate(AmazonS3Wrapper<S3Client> defaultS3, Map<String, AmazonS3Wrapper<S3Client>> s3Map) {
        this.defaultS3 = defaultS3;
        this.s3Map = s3Map;
    }


    @Override
    public StorageResult upload(MultipartFile file) {
        try (S3Client s3Client = defaultS3.amazonS3()) {
            String filename = getDefaultFilename(file.getOriginalFilename());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(defaultS3.bucketName())
                    .key(filename).build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("upload file: {}, into default bucket: {}, endpoint:{}",filename, defaultS3.bucketName(), defaultS3.endpoint());
            return new OssStorageResult(null, null, putObjectResponse.versionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        AmazonS3Wrapper<S3Client> s3Wrapper = getAmazonS3Wrapper(bucketName);
        try (S3Client s3Client = s3Wrapper.amazonS3()){
            String filename = getFilename(key, file.getOriginalFilename());
            log.info("upload file: {}, into bucket: {}, endpoint:{}",filename, s3Wrapper.bucketName(), s3Wrapper.endpoint());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(filename).build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return new OssStorageResult(null, null, putObjectResponse.versionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult upload(String bucketName, String key, InputStream inputStream) {
        throw new UnsupportedOperationException("awssdk of S3Client not support upload with InputStream");
    }

    @Override
    public StorageResult getFile(String bucketName, String key) {
        AmazonS3Wrapper<S3Client> s3Wrapper = getAmazonS3Wrapper(bucketName);
        try (S3Presigner presigner = s3Wrapper.presigner()) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(key)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(30))
                    .getObjectRequest(request)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return new OssStorageResult(bucketName, key, presignedRequest.url().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AmazonS3Wrapper<S3Client> getAmazonS3Wrapper(String bucket) {
        return bucket == null || bucket.isEmpty() || !s3Map.containsKey(bucket) ? defaultS3 : s3Map.get(bucket);
    }


    @Override
    public void afterPropertiesSet() {
        // 检查默认bucket设置
        Assert.notNull(defaultS3, "defaultS3 must not be null in OssClientTemplate");
        Assert.state(!CollectionUtils.isEmpty(s3Map), "accessKey must not be null in OssClientTemplate");
        log.info("OssClientTemplate initiated successfully");
    }

    @Override
    public int getOrder() {
        return StorageConstants.StorageClientOrder.OSS_S3.getOrder();
    }
}
