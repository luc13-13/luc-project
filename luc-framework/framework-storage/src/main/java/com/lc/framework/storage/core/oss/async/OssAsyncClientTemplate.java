package com.lc.framework.storage.core.oss.async;

import cn.hutool.core.lang.Assert;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StoragePlatformAdaptor;
import com.lc.framework.storage.client.StorageResult;
import com.lc.framework.storage.core.StorageConstants;
import com.lc.framework.storage.core.oss.OssClientTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/16 14:44
 * @version : 1.0
 */
@Slf4j
public class OssAsyncClientTemplate implements StorageClientTemplate {

    @Setter
    private StoragePlatformAdaptor storagePlatformAdaptor;

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    private final OssClientTemplate.AmazonS3Wrapper<S3AsyncClient> defaultS3;

    /**
     * 每个bucket的s3客户端
     */
    private final Map<String, AmazonS3Wrapper<S3AsyncClient>> s3Map;

    private final ExecutorService executor;

    public OssAsyncClientTemplate(AmazonS3Wrapper<S3AsyncClient> defaultS3, Map<String, OssClientTemplate.AmazonS3Wrapper<S3AsyncClient>> s3Map, ExecutorService executor) {
        this.defaultS3 = defaultS3;
        this.s3Map = s3Map;
        this.executor = executor;
    }

    @Override
    public StorageResult upload(MultipartFile file) {
        return upload(defaultS3.bucketName(), null, file);
    }

    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        try {
            // 提取文件名
            String filename = getFilename(key, file.getOriginalFilename());
            // 获取文件流， 异步不可以将流的try-with-resources中，防止流被提前关闭导致异步线程无法执行
            InputStream inputStream = file.getInputStream();
            return upload(bucketName, filename, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步上传字节流
     * @param bucketName 存储桶
     * @param key 文件key
     * @param inputStream 字节流, 不可被放在try-with-resources语句中，否则流被关闭将导致异步线程无法执行
     * @return 上传结果
     */
    @Override
    public StorageResult upload(String bucketName, String key, InputStream inputStream) {
        Assert.isTrue(StringUtils.hasText(key), "key must not be empty");
        AmazonS3Wrapper<S3AsyncClient> s3Wrapper = getAmazonS3Wrapper(bucketName);
        try (S3AsyncClient s3AsyncClient = s3Wrapper.amazonS3()) {
            // 创建请求
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(defaultS3.bucketName())
                    .key(key).build();
            // 获取异步返回值
            CompletableFuture<PutObjectResponse> response = s3AsyncClient.putObject(request, AsyncRequestBody.fromInputStream(inputStream, null, executor));
            // 构建返回对象
            return new OssAsyncStorageResult(s3Wrapper.bucketName(), key, response.exceptionally(throwable -> {
                log.error("upload error", throwable);
                throw new RuntimeException(throwable);
            }).thenApply(PutObjectResponse::versionId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StorageResult getFile(String bucketName, String key) {
        AmazonS3Wrapper<S3AsyncClient> s3Wrapper = getAmazonS3Wrapper(bucketName);
        try (S3Presigner presigner = s3Wrapper.presigner()) {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(s3Wrapper.bucketName())
                    .key(key)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(30))
                    .getObjectRequest(request)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return new OssAsyncStorageResult(bucketName, key,
                    CompletableFuture
                            .supplyAsync(() -> presignedRequest.url().toString(), executor).exceptionally(throwable -> {
                                log.error("get file error", throwable);
                                return throwable.getMessage();})
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AmazonS3Wrapper<S3AsyncClient> getAmazonS3Wrapper(String bucket) {
        return bucket == null || bucket.isEmpty() || !s3Map.containsKey(bucket) ? defaultS3 : s3Map.get(bucket);
    }

    @Override
    public int getOrder() {
        return StorageConstants.StorageClientOrder.OSS_ASYNC.getOrder();
    }
}
