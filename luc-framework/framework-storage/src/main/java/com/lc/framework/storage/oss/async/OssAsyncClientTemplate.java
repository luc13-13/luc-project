package com.lc.framework.storage.oss.async;

import com.lc.framework.storage.adaptor.StoragePlatformAdaptor;
import com.lc.framework.storage.core.BucketInfo;
import com.lc.framework.storage.core.StorageResult;
import com.lc.framework.storage.core.StorageConstants;
import com.lc.framework.storage.oss.AbstractOssClientTemplate;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * <pre>
 *     基于异步S3客户端的对象存储操作
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/16 14:44
 * @version : 1.0
 */
@Slf4j
public class OssAsyncClientTemplate extends AbstractOssClientTemplate<S3AsyncClient> {

    private final ExecutorService executor;

    public OssAsyncClientTemplate(StoragePlatformAdaptor storagePlatformAdaptor, AmazonS3Wrapper<S3AsyncClient> defaultS3, ExecutorService executor) {
        super(storagePlatformAdaptor, defaultS3);
        this.executor = executor;
    }

    @Override
    protected StorageResult doUpload(S3AsyncClient s3Client, PutObjectRequest request, InputStream inputStream, Long size) {
        // 获取异步返回值
        CompletableFuture<PutObjectResponse> response = s3Client.putObject(request, AsyncRequestBody.fromInputStream(inputStream, size, executor));
        // 构建返回对象
        return new OssAsyncStorageResult(getS3Map().get(request.bucket()).bucketInfo(), request.key(),
                response.thenApply(it -> getFile(request.bucket(), request.key()).accessUrl())
                        .exceptionally(throwable -> {
                            log.error("upload error", throwable);
                            throw new RuntimeException(throwable);
                        }));

    }

    @Override
    protected StorageResult doGetFile(BucketInfo bucketInfo, String key, String url) {
        // 获取异步返回值
        return new OssAsyncStorageResult(bucketInfo, key,
                CompletableFuture
                        .supplyAsync(() -> url, executor).exceptionally(throwable -> {
                            log.error("get file error", throwable);
                            return throwable.getMessage();})
        );
    }

    @Override
    public int getOrder() {
        return StorageConstants.StorageClientOrder.OSS_ASYNC.getOrder();
    }
}
