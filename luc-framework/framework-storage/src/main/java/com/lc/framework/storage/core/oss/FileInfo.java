package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageResult;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/21 15:54
 */
public record FileInfo(String bucketName, String fileName, String accessUrl) implements StorageResult {
}
