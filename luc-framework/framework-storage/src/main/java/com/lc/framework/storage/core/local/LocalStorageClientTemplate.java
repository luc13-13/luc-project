package com.lc.framework.storage.core.local;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StorageResult;
import com.lc.framework.storage.core.StorageConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <pre>
 *     本地存储工具
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:20
 */
public class LocalStorageClientTemplate implements StorageClientTemplate {

    @Override
    public StorageResult upload(MultipartFile file) {
        return null;
    }

    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        return null;
    }

    @Override
    public StorageResult upload(String bucketName, String key, InputStream inputStream) {
        return null;
    }

    @Override
    public StorageResult getFile(String bucketName, String key) {
        return null;
    }

    @Override
    public int getOrder() {
        return StorageConstants.StorageClientOrder.LOCAL.getOrder();
    }
}
