package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StorageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    private final AmazonS3Wrapper defaultS3;

    /**
     * 每个bucket的s3客户端
     */
    private final Map<String, AmazonS3Wrapper> s3Map;

    public OssClientTemplate(AmazonS3Wrapper defaultS3, Map<String, AmazonS3Wrapper> s3Map) {
        this.defaultS3 = defaultS3;
        this.s3Map = s3Map;
    }


    @Override
    public StorageResult upload(MultipartFile file) {
        try {
            String filename = getDefaultFilename(file.getOriginalFilename());
            AmazonS3Wrapper s3Wrapper = defaultS3;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(filename).build();
            PutObjectResponse putObjectResponse = s3Wrapper.amazonS3().putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("upload file: {}, into default bucket: {}, endpoint:{}",filename, s3Wrapper.bucketName(), s3Wrapper.endpoint());
            return new OssStorageResult(null, null, putObjectResponse.versionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        try {
            AmazonS3Wrapper s3Wrapper = getAmazonS3Wrapper(bucketName);
            String filename = getFilename(key, file.getOriginalFilename());
            log.info("upload file: {}, into bucket: {}, endpoint:{}",filename, s3Wrapper.bucketName(), s3Wrapper.endpoint());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(filename).build();
            PutObjectResponse putObjectResponse = s3Wrapper.amazonS3().putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return new OssStorageResult(null, null, putObjectResponse.versionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult upload(String bucketName, String key, InputStream inputStream) {
        return null;
    }

    @Override
    public StorageResult getFile(String bucketName, String key) {
        return null;
    }

    private AmazonS3Wrapper getAmazonS3Wrapper(String bucket) {
        return bucket == null || bucket.isEmpty() || !s3Map.containsKey(bucket) ? defaultS3 : s3Map.get(bucket);
    }


    @Override
    public void afterPropertiesSet() {
        // 检查默认bucket设置
        Assert.notNull(defaultS3, "defaultS3 must not be null in OssClientTemplate");
        Assert.state(!CollectionUtils.isEmpty(s3Map), "accessKey must not be null in OssClientTemplate");
        log.info("OssClientTemplate initiated successfully");
    }

    /**
     * 对AmazonS3进行封装，保存bucketNam
     * @param endpoint bucket的访问端点
     * @param bucketName
     * @param amazonS3
     */
    public record AmazonS3Wrapper(String endpoint, String bucketName, S3Client amazonS3) {}
}
