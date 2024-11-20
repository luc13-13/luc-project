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
     * @param file 文件
     */
    void upload(MultipartFile file);

    /**
     * 上传文件——InputStream
     * @param inputStream
     */
    void upload(InputStream inputStream);

    String getUrl();
}
