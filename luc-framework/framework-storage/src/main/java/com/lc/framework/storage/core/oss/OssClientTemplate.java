package com.lc.framework.storage.core.oss;

import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StorageResult;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <pre>
 *     七牛云对象存储
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 13:50
 */
@AllArgsConstructor
public class OssClientTemplate implements StorageClientTemplate {

    private OssStorageProperties properties;

    /**
     * 七牛对象存储——断点续传
     * @param bucketName 存储桶
     * @param key 文件key
     * @param file 文件
     */
    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        StringMap putPolicy = new StringMap();
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
}
