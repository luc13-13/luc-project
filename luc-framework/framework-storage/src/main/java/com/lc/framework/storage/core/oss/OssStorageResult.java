package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageResult;
import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/15 15:27
 * @version : 1.0
 */
public class OssStorageResult implements StorageResult {

    private final String bucketName;

    private final String filename;

    private final String accessUrl;



    public OssStorageResult(String bucketName, String filename, String accessUrl) {
        this.bucketName = bucketName;
        this.filename = filename;
        this.accessUrl = accessUrl;
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
        return accessUrl;
    }
}
