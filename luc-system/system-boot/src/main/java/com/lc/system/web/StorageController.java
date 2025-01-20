package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.StorageResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/20 15:03
 * @version : 1.0
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageClientTemplate storageClientTemplate;

    public StorageController(StorageClientTemplate storageClientTemplate) {
        this.storageClientTemplate = storageClientTemplate;
    }

    @PostMapping("upload")
    public WebResult<StorageResult> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam(name = "bucket", required = false) String bucket,
                                           @RequestParam(name = "prefix", required = false) String prefix) {
        StorageResult result = storageClientTemplate.upload(bucket, prefix, file);
        return WebResult.successData(result);
    }

    @GetMapping("getFile")
    public WebResult<String> getFile(@RequestParam("filename") String filename) {
        String url = storageClientTemplate.getFile(null, filename).accessUrl();
        return WebResult.successData(url);
    }

}
