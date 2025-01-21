package com.lc.framework.storage.oss.async;

import com.lc.framework.storage.core.BucketInfo;
import com.lc.framework.storage.core.StorageResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/16 15:21
 * @version : 1.0
 */
public class OssAsyncStorageResult implements StorageResult {

    private final BucketInfo bucketInfo;

    private final String filename;

    private final CompletableFuture<String> asyncAccessUrl;

    public OssAsyncStorageResult(BucketInfo bucketInfo, String filename, CompletableFuture<String> asyncAccessUrl) {
        this.bucketInfo = bucketInfo;
        this.filename = filename;
        this.asyncAccessUrl = asyncAccessUrl;
    }

    @Override
    public BucketInfo bucketInfo() {
        return bucketInfo;
    }

    @Override
    public String filename() {
        return filename;
    }

    @Override
    public String accessUrl() {
        try {
            return asyncAccessUrl.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
