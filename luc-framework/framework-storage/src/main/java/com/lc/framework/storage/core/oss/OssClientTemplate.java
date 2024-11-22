package com.lc.framework.storage.core.oss;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.client.StorageResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

/**
 * <pre>
 *     s3规则的对象存储平台，例如七牛、阿里云、minio
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 13:50
 */
@Slf4j
public class OssClientTemplate implements StorageClientTemplate, InitializingBean {

    /**
     * 默认bucket，需要与bucketMap中的key匹配
     */
    private final AmazonS3Wrapper defaultS3;

    /**
     * 每个bucket的s3客户端
     */
    private final Map<String, AmazonS3Wrapper> s3Map;

    public OssClientTemplate(AmazonS3Wrapper defaultS3, Map<String, AmazonS3Wrapper> s3Map) {
        this.defaultS3 = defaultS3;
        this.s3Map = s3Map;
    }


    /**
     * 七牛对象存储——断点续传
     * @param bucketName 存储桶
     * @param key 文件key
     * @param file 文件
     */
    @Override
    public StorageResult upload(String bucketName, String key, MultipartFile file) {
        try {
            AmazonS3Wrapper s3Wrapper = getAmazonS3Wrapper(bucketName);
            String filename = getFilename(key, file.getOriginalFilename());
            log.info("upload file: {}, into bucket: {}, endpoint:{}",filename, s3Wrapper.bucketName(), s3Wrapper.amazonS3().getRegionName());
            s3Wrapper.amazonS3().putObject(s3Wrapper.bucketName(), filename, file.getInputStream(), new ObjectMetadata());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private AmazonS3Wrapper getAmazonS3Wrapper(String bucket) {
        return bucket == null || bucket.length() == 0 || !s3Map.containsKey(bucket) ? defaultS3 : s3Map.get(bucket);
    }

    private String getFilename(String key, String originalFilename) {
        if (StringUtils.hasText(key)) {
            return key;
        }
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + StrUtil.DOT + IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(originalFilename);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查默认bucket设置
        Assert.notNull(defaultS3, "defaultS3 must not be null in OssClientTemplate");
        Assert.state(!CollectionUtils.isEmpty(s3Map), "accessKey must not be null in OssClientTemplate");
        log.info("OssClientTemplate initiated successfully");
    }

    /**
     * 对AmazonS3进行封装，保存bucketNam
     * @param bucketName
     * @param amazonS3
     */
    public record AmazonS3Wrapper(String bucketName, AmazonS3 amazonS3) {}
}
