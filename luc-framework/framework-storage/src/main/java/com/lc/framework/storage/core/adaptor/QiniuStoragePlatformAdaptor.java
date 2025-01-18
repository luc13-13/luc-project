package com.lc.framework.storage.core.adaptor;

import com.lc.framework.storage.client.StoragePlatformAdaptor;
import com.lc.framework.storage.core.oss.properties.BucketInfo;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/18 20:41
 * @version : 1.0
 */
public class QiniuStoragePlatformAdaptor implements StoragePlatformAdaptor {

    @Override
    public String getAccessUrl(BucketInfo bucketInfo, String key) {
        // 签名
        Auth auth = Auth.create(bucketInfo.getAccessKey(), bucketInfo.getSecretKey());
        CdnManager cdnManager = new CdnManager(auth);
        try {
            CdnResult.PrefetchResult result = cdnManager.prefetchUrls(new String[]{key});
            return result.toString();
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}
