package com.lc.system.web;

import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.storage.client.StorageClientTemplate;
import com.lc.framework.storage.core.StorageResult;
import com.lc.system.domain.dto.StorageResultDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/20 15:03
 * @version : 1.0
 */
@Tag(name = "云存储")
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageClientTemplate storageClientTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StorageController(StorageClientTemplate storageClientTemplate) {
        this.storageClientTemplate = storageClientTemplate;
    }

    @PostMapping("upload")
    public WebResult<StorageResultDTO> upload(@RequestParam("file") MultipartFile file,
                                              @RequestParam(name = "bucket", required = false) String bucket,
                                              @RequestParam(name = "prefix", required = false) String prefix) {
        StorageResult result = storageClientTemplate.upload(bucket, prefix, file);
        return WebResult.success(StorageResultDTO.builder()
                .storageType(Objects.isNull(result.bucketInfo()) ? null : result.bucketInfo().getPlatform())
                .bucketName(Objects.isNull(result.bucketInfo()) ? null : result.bucketInfo().getName())
                .region(Objects.isNull(result.bucketInfo()) ? null : result.bucketInfo().getRegion())
                .key(result.filename())
                .link(result.accessUrl())
                .filename(file.getOriginalFilename()).build());
    }

    @GetMapping("getFile")
    public WebResult<String> getFile(@RequestParam("bucket") String bucket,
                                     @RequestParam("filename") String filename) {
        String url = storageClientTemplate.getFile(bucket, filename).accessUrl();
        return WebResult.success(url);
    }

}
