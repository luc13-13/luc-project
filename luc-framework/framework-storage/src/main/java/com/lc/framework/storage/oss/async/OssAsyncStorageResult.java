package com.lc.framework.storage.oss.async;

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

    private final String bucketName;

    private final String filename;

    private final CompletableFuture<String> asyncAccessUrl;

    public OssAsyncStorageResult(String bucketName, String filename, CompletableFuture<String> asyncAccessUrl) {
        this.bucketName = bucketName;
        this.filename = filename;
        this.asyncAccessUrl = asyncAccessUrl;
    }

    @Override
    public String bucketName() {
        return bucketName;
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
