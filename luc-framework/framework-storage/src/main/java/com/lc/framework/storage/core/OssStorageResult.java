package com.lc.framework.storage.core;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/15 15:27
 * @version : 1.0
 */
public record OssStorageResult(BucketInfo bucketInfo, String filename, String accessUrl) implements StorageResult {

}
