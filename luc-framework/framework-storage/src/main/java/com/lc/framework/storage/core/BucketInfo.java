package com.lc.framework.storage.core;

import com.lc.framework.core.constants.StringConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.util.StringUtils;


/**
 * <pre>
 *     不同厂商提供的兼容s3协议的bucket信息封装
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/11/20 14:53
 */
@Data
public class BucketInfo {

    /**
     * 对象存储平台：qiniu、aliyun、txcloud
     */
    @NotBlank(message = "platform must not be null, supported values are qiniu、aliyun、txcloud")
    private String platform;

    /**
     * 存储平台提供的cdn加速服务，取存储平台配置的cdn域名
     */
    private String cdn;

    /**
     * ak，没有设置则取全局属性{@link OssStorageProperties#getAccessKey()}
     */
    @NotBlank(message = "accessKey must not be null")
    private String accessKey;

    /**
     * sk，没有设置则取全局属性{@link OssStorageProperties#getSecretKey()}
     */
    @NotBlank(message = "secretKey must not be null")
    private String secretKey;

    /**
     * bucket名称
     */
    @NotBlank(message = "name must not be null")
    private String name;

    /**
     * s3协议的endpoint，由各oss厂商提供，改地址仅供S3客户端使用<br/>
     * 如果没有设置，取{@link OssStorageProperties#getDefaultDomainOfBucket()}创建，具体规则如下：<br/>
     * 注意，根据s3协议，endpoint必须为http(s)://s3.区域id.厂商后缀
     */
    @NotBlank(message = "endpoint must not be null")
    private String endpoint;

    /**
     * bucket的区域，来自厂商提供的取值
     */
    @NotBlank(message = "region must not be null")
    private String region;

    /**
     * bucket是否使用https
     */
    private boolean useHttps = false;

    /**
     * 是否将bucket名称作为路径名，true是，false否<br/>
     * 以七牛为例，bucket的s3访问域名为https://bucket名.s3.区域id.qiniucs.com，则不是pathStyle，需设置为false<br/>
     * 如果是https://s3.区域id.qiniucs.com/bucket名称，则设置pathStyle=true
     */
    private boolean pathStyleEnabled = false;

    /**
     * 是否开启分段上传
     */
    private boolean multipartEnabled = false;

    /**
     * 获取存储桶中的文件访问地址前缀
     * @return url string
     */
    public String getAccessDomain() {
        String protocol = (this.isUseHttps() ? StringConstants.HTTPS : StringConstants.HTTP) + StringConstants.COLON + StringConstants.SLASH + StringConstants.SLASH;
        String domain = this.getEndpoint().substring(protocol.length());
        if(StringUtils.hasText(this.cdn)) {
            domain = this.cdn;
        } else {
            if (this.isPathStyleEnabled()) {
                domain = domain + StringConstants.SLASH + this.getName();
            } else {
                domain = this.getName() + StringConstants.DOT + domain;
            }
        }
        return protocol + domain;
    }
}
