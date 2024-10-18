package com.lc.framework.datasource.starter.properties;

import com.lc.framework.datasource.starter.creator.druid.DruidConfig;
import com.lc.framework.datasource.starter.creator.hikari.HikariCpConfig;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * <pre>
 *     通用的数据源属性配置。目前支持Druid和Hikari两种数据源
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/15 13:55
 */
@Data
public class DataSourceProperty {

    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String poolName;

    /**
     * 连接池类型，如果不设置自动查找 Druid(额外引入) > Hikari(Spring默认) > Sharding(默认不开启)
     */
    private Class<? extends DataSource> type;

    /**
     * JDBC driver
     */
    private String driverClassName;

    /**
     * JDBC url 地址
     */
    private String url;

    /**
     * JDBC 用户名
     */
    private String username;

    /**
     * JDBC 密码
     */
    private String password;

    /**
     * 是否启用seata，后续考虑分布式事务时需要该属性
     */
    private Boolean seata = false;

    /**
     * lazy init datasource
     */
    private Boolean lazy = false;

    /**
     * 是否开启分库分表，默认不开启
     */
    private Boolean sharding = false;

    /**
     * 分库分表数据库的配置文件路径
     */
    private String shardingConfig;

    /**
     * 当前数据源的Druid参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();

    /**
     * 当前数据源的HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    /**
     * 解密公匙(如果未设置默认使用全局的)
     */
    private String publicKey;
}
