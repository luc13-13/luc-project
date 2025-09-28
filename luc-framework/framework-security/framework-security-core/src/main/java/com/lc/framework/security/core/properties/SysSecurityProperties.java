package com.lc.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * <pre>
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/3/20 16:43
 */
@Data
@ConfigurationProperties(prefix = SysSecurityProperties.PREFIX)
public class SysSecurityProperties {
    /**
     * 系统安全相关配置前缀
     */
    public static final String PREFIX = "sys.security";
    /**
     * 认证服务器地址，所有服务保持一致
     */
    private String issuer = "http://127.0.0.1:8809";

    /**
     * 资源服务器的白名单路径
     */
    private List<String> whitePaths;

    private boolean enableRedis = false;

    /**
     * 登陆有效时间
     */
    private Duration tokenTimeToLive = Duration.ofSeconds(3600L);

    /**
     * 加密公钥
     */
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWXsXu/lyPpXaPoRKbBrdJMnlboADDO/SosP7K5/oT5Ln1FKhOb719eHB4lufPYzjQhualbqgdf+A/fP1soa4w58jL9oeovWMTCoJNyQfZEB0d3+9KXk6NTiZMAGbm2sicCh9kviiEQ8bkZeTkhg5Es3SM0YKRkwHgoEwMysTaJwIDAQAB";
    /**
     * 加密私钥
     */
    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJZexe7+XI+ldo+hEpsGt0kyeVugAMM79Kiw/srn+hPkufUUqE5vvX14cHiW589jONCG5qVuqB1/4D98/WyhrjDnyMv2h6i9YxMKgk3JB9kQHR3f70peTo1OJkwAZubayJwKH2S+KIRDxuRl5OSGDkSzdIzRgpGTAeCgTAzKxNonAgMBAAECgYA++tjaHa00+O9sfuElDy4LOVm894n6O41gbil5YKnMVTtRm+JWX9S0zZIF3+adZQhxxl6qIWvGVz3cOYSHUDx9VZAMgLZNcm866AveGXW48GxQMjw97p8SigdEi8wNMWaN4B8+H/8YC5689TlPiML/wYU7w1u9CORuTywWiovISQJBAKBTFSyexVzboJce7gOFhE8pEMufgOA+00/gy7fT3xK6sr3prWfE81Ri5BIP7044G6SJfjlxGI5A/BXVdQMEnesCQQDwGvVVbMowXuIo3PX6sJh2QYUFWRpCnXCCe/F1ZpS3tSGAY84Lg8BJsNOgXvs5P8uos3p55IjW7bkJd16hr9m1AkBGK5b87JDdT5M+EV+DUxDRxNuBA9LYUycRswX281iTTfule31WCbGmoZHJBghrZ8tRfIwuf/2LZY3v0HliVMb5AkEA3QZj+vxHb5mYpTcqdFgmMQ4FTrQEOXhGvscrsf/5PNdPfKnjXbQtyWgul713k+7HIof9YcHfucJiCHXLNhnz1QJBAJ4rNLSPSFpELjDQR95Rzvr0d7fYScLxbH9LQAAadtoeRE14K64JxpdqFpo9U9qiypvTRLRm11nFDATjFDzdUG8=";

}
