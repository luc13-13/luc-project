package com.lc.framework.storage.client;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:01
 */
public interface StorageClientTemplate {

    /**
     * 上传文件——MultipartFile
     * @param bucketName 存储桶
     * @param key 文件key
     * @param file 文件
     */
    StorageResult upload(String bucketName, String key, MultipartFile file);

    /**
     * 上传文件——InputStream
     * @param bucketName 存储桶
     * @param key 文件key
     * @param inputStream 字节流
     */
    StorageResult upload(String bucketName, String key, InputStream inputStream);

    /**
     * 获取文件外链，默认外链有效期为600s
     * @param bucketName 存储桶
     * @param key 文件key
     * @return 文件外链
     */
    StorageResult getFile(String bucketName, String key);
}
