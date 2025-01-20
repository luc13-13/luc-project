package com.lc.framework.storage.client;

import com.lc.framework.core.FileUtils;
import com.lc.framework.core.constants.StringConstants;
import com.lc.framework.storage.core.BucketInfo;
import com.lc.framework.storage.core.StorageResult;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 9:01
 */
public interface StorageClientTemplate extends Ordered {

    /**
     * 上传至默认bucket
     * @param file 文件
     * @return 上传结果
     */
    StorageResult upload(MultipartFile file);

    /**
     * 上传文件——MultipartFile
     * @param bucketName 存储桶
     * @param prefix 文件key
     * @param file 文件
     */
    StorageResult upload(String bucketName, String prefix, MultipartFile file);

    /**
     * 上传文件——InputStream
     * @param bucketName 存储桶
     * @param key 文件key
     * @param inputStream 字节流
     */
    StorageResult upload(String bucketName, String key, InputStream inputStream, Long size);

    /**
     * 获取文件外链，默认外链有效期为600s
     * @param bucketName 存储桶
     * @param key 文件key
     * @return 文件外链
     */
    StorageResult getFile(String bucketName, String key);

    /**
     * 生成文件名 todo: 分到工具类中
     * @param prefix 前缀
     * @param originalFilename 原始文件名
     * @return 生成端存储端文件名, 格式为[prefix/][date].[UUID].[ext]
     */
    default String getFilename(String prefix, String originalFilename) {
        return (StringUtils.hasText(prefix) ? prefix + StringConstants.SLASH : StringConstants.EMPTY_STRING) + getDefaultFilename(originalFilename);
    }

    /**
     * 生成默认文件名 todo: 分到工具类中
     * @param originalFilename 原始文件名
     * @return 生成端存储端文件名
     */
    default String getDefaultFilename(String originalFilename) {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + StringConstants.DOT + UUID.randomUUID() + StringConstants.DOT + FileUtils.getExtName(originalFilename);
    }

    /**
     * 对AmazonS3进行封装，保存bucketNam
     * @param bucketInfo bucket信息
     * @param amazonS3
     * @param presigner 预签名
     */
    record AmazonS3Wrapper<T extends AwsClient>(BucketInfo bucketInfo, T amazonS3, S3Presigner presigner) {

        public String endpoint() {
            return bucketInfo().getEndpoint();
        }

        public String bucketName() {
            return bucketInfo().getName();
        }
    }
}
