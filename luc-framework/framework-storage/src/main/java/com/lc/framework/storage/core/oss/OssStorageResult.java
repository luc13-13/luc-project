package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageResult;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/15 15:27
 * @version : 1.0
 */
public record OssStorageResult(String bucketName, String filename, String accessUrl) implements StorageResult {

}
