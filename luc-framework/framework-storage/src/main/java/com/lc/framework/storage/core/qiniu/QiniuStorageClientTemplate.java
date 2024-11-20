package com.lc.framework.storage.core.qiniu;

import com.lc.framework.storage.client.StorageClientTemplate;
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
public class QiniuStorageClientTemplate implements StorageClientTemplate {

    @Override
    public void upload(MultipartFile file) {

    }

    @Override
    public void upload(InputStream inputStream) {

    }

    @Override
    public String getUrl() {
        return null;
    }
}
