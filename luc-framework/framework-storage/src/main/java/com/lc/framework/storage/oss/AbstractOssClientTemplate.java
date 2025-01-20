package com.lc.framework.storage.oss;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.adaptor.StoragePlatformAdaptor;
import com.lc.framework.storage.core.StorageResult;
import com.lc.framework.storage.core.StorageConstants;
import com.lc.framework.storage.oss.sync.OssClientTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     基于对象存储平台的抽象类，封装Aws S3标准协议下的对象操作，兼容不同对象存储平台的实现
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/19 14:09
 * @version : 1.0
 */
@Slf4j
@Getter
public abstract class AbstractOssClientTemplate<T extends AwsClient> implements StorageClientTemplate, InitializingBean {

    /**
     * 对象存储平台适配器
     */
    private final StoragePlatformAdaptor storagePlatformAdaptor;

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    private final OssClientTemplate.AmazonS3Wrapper<T> defaultS3;

    /**
     * 每个bucket的s3客户端
     */
    private final transient Map<String, AmazonS3Wrapper<T>> s3Map = new ConcurrentHashMap<>();

    public AbstractOssClientTemplate(StoragePlatformAdaptor storagePlatformAdaptor, AmazonS3Wrapper<T> defaultS3) {
        this.storagePlatformAdaptor = storagePlatformAdaptor;
        this.defaultS3 = defaultS3;
    }

    @Override
    public StorageResult upload(MultipartFile file) {
        return upload(defaultS3.bucketName(), null, file);
    }

    @Override
    public StorageResult upload(String bucketName, String prefix, MultipartFile file) {
        try {
            // 提取文件名
            String filename = getFilename(prefix, file.getOriginalFilename());
            // 获取文件流， 异步不可以将流的try-with-resources中，防止流被提前关闭导致异步线程无法执行
            InputStream inputStream = file.getInputStream();
            return upload(bucketName, filename, inputStream, file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult upload(String bucketName, String key, InputStream inputStream, Long size) {
        Assert.isTrue(StringUtils.hasText(key), "key must not be empty");
        AmazonS3Wrapper<T> s3Wrapper = getAmazonS3Wrapper(bucketName);
        try (T s3Client = s3Wrapper.amazonS3()) {
            // 创建上传请求
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(key).build();
            return doUpload(s3Client, request, inputStream, size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行上传方法，由子类实现
     * @param s3Client Aws S3客户端
     * @param request 上传请求
     * @param inputStream 字节流
     * @param size 字节流大小
     * @return 上传结果
     */
    protected abstract StorageResult doUpload(T s3Client, PutObjectRequest request, InputStream inputStream, Long size);

    @Override
    public StorageResult getFile(String bucketName, String key) {
        AmazonS3Wrapper<T> s3Wrapper = getAmazonS3Wrapper(bucketName);
        if (Objects.nonNull(storagePlatformAdaptor)) {
            log.info("从适配器获取外链：{}", storagePlatformAdaptor.getClass().getSimpleName());
            return doGetFile(bucketName, key, storagePlatformAdaptor.getAccessUrl(s3Wrapper.bucketInfo(), key));
        }
        try (S3Presigner presigner = s3Wrapper.presigner()) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(key)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(30))
                    .getObjectRequest(request)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return doGetFile(bucketName, key, presignedRequest.url().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract StorageResult doGetFile(String bucketName, String key, String url);

    @Override
    public int getOrder() {
        Integer order = StorageConstants.StorageClientOrder.getOrder(this.getClass());
        log.info("对象存储操作模板：{}, 加载顺序：{}", this.getClass(), order);
        return order;
    }

    public void putS3Wrapper(AmazonS3Wrapper<T> s3) {
        s3Map.put(s3.bucketName(), s3);
    }

    public void putAllS3Wrappers(Collection<AmazonS3Wrapper<T>> s3List) {
        Assert.notNull(s3List, "parameter 's3List' must not be null");
        s3List.forEach(this::putS3Wrapper);
    }

    protected AmazonS3Wrapper<T> getAmazonS3Wrapper(String bucket) {
        return bucket == null || bucket.isEmpty() || !s3Map.containsKey(bucket) ? defaultS3 : s3Map.get(bucket);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(defaultS3, "defaultS3 must not be null");

        s3Map.putIfAbsent(defaultS3.bucketName(), defaultS3);
        log.info("{} initiated successfully!", this.getClass().getSimpleName());
    }
}
