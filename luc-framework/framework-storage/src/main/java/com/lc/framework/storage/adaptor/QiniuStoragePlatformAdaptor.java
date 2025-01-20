package com.lc.framework.storage.adaptor;

import com.lc.framework.core.constants.StringConstants;
import com.lc.framework.storage.core.BucketInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 20:41
 * @version : 1.0
 */
@Slf4j
public class QiniuStoragePlatformAdaptor implements StoragePlatformAdaptor {


    @Override
    public String getAccessUrl(BucketInfo bucketInfo, String key) {
        // 签名
        Auth auth = getAuth(bucketInfo.getAccessKey(), bucketInfo.getSecretKey());
        // 文件链接
        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String url = auth.privateDownloadUrl(bucketInfo.getAccessDomain() + StringConstants.SLASH  + encodedKey, 3600);
        log.info("创建外链：{}", url);
        return url;
    }

    private Auth getAuth(String accessKey, String secretKey) {
        return Auth.create(accessKey, secretKey);
    }
}
