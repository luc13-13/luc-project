package com.lc.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/21 14:26
 * @version : 1.0
 */
@Data
@Builder
@Schema(description = "存储结果封装")
public class StorageResultDTO {

    @Schema(name = "bucket名称")
    private String bucketName;

    @Schema(name = "区域")
    private String region;

    @Schema(name = "文件key在存储平台的key")
    private String key;

    @Schema(name = "存储类型：s3、localhost")
    private String storageType;

    @Schema(name = "外链")
    private String link;

    @Schema(name = "外链过期时间，精确到秒")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime linkExpiredAt;

    @Schema(name = "文件名")
    private String filename;


}
