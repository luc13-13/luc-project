package com.lc.framework.storage.client;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:01
 */
public interface StorageClientTemplate {

    /**
     * 上传至默认bucket
     * @param file 文件
     * @return 上传结果
     */
    StorageResult upload(MultipartFile file);

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

    /**
     * 生成文件名 todo: 分到工具类中
     * @param key 文件key
     * @param originalFilename 原始文件名
     * @return 生成端存储端文件名
     */
    default String getFilename(String key, String originalFilename) {
        if (StringUtils.hasText(key)) {
            return key;
        }
        return getDefaultFilename(originalFilename);
    }

    /**
     * 生成默认文件名 todo: 分到工具类中
     * @param originalFilename 原始文件名
     * @return 生成端存储端文件名
     */
    default String getDefaultFilename(String originalFilename) {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + StrUtil.DOT + IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(originalFilename);
    }
}
