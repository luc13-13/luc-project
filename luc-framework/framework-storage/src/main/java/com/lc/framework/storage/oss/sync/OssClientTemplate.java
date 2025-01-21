package com.lc.framework.storage.oss.sync;

import com.lc.framework.storage.adaptor.StoragePlatformAdaptor;
import com.lc.framework.storage.core.BucketInfo;
import com.lc.framework.storage.core.StorageResult;
import com.lc.framework.storage.oss.AbstractOssClientTemplate;
import com.lc.framework.storage.core.OssStorageResult;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.InputStream;

/**
 * <pre>
 *     基于同步S3客户端的对象存储操作
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 13:50
 */
@Slf4j
public class OssClientTemplate extends AbstractOssClientTemplate<S3Client> {

    public OssClientTemplate(StoragePlatformAdaptor storagePlatformAdaptor, AmazonS3Wrapper<S3Client> defaultS3) {
        super(storagePlatformAdaptor, defaultS3);
    }

    @Override
    protected StorageResult doUpload(S3Client s3Client, PutObjectRequest request, InputStream inputStream, Long size) {
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, size));
        return getFile(request.bucket(), request.key());

    }

    @Override
    protected StorageResult doGetFile(BucketInfo bucketInfo, String key, String url) {
        return new OssStorageResult(bucketInfo, key, url);
    }
}
