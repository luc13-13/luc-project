package com.lc.framework.storage.core.local;

import com.lc.framework.storage.client.StorageClientTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <pre>
 *     本地存储工具
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:20
 */
public class LocalStorageClientTemplate implements StorageClientTemplate {

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
