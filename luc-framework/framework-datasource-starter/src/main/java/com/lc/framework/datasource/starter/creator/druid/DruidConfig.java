package com.lc.framework.datasource.starter.creator.druid;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <pre>
 *     Druid参数配置，支持
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/10/15 14:52
 */
@Setter
@Getter
public class DruidConfig {
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    /**
     * 获取连接的最大等待时间，默认为-1
     */
    private Integer maxWait;
    /**
     * 关闭空闲连接的等待时间，单位毫秒
     */
    private Long timeBetweenEvictionRunsMillis;
    private Long timeBetweenLogStatsMillis;
    private Long keepAliveBetweenTimeMillis;
    private Integer statSqlMaxSize;
    private Long minEvictableIdleTimeMillis;
    private Long maxEvictableIdleTimeMillis;
    private String defaultCatalog;
    private Boolean defaultAutoCommit;
    private Boolean defaultReadOnly;
    private Integer defaultTransactionIsolation;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private String validationQuery;
    private Integer validationQueryTimeout;
    private Boolean useGlobalDataSourceStat;
    private Boolean asyncInit;
    private String filters;
    private Boolean clearFiltersEnable;
    private Boolean resetStatEnable;
    /**
     * 获取连接失败的重试次数，默认为0，
     */
    private Integer notFullTimeoutRetryCount;
    private Integer maxWaitThreadCount;
    /**
     * 获取、创建连接失败次数超过最大次数后，终止创建连接的线程，不建议设置为true，会导致服务终止，无法重新连接到数据库
     */
    private Boolean failFast;
    private Long phyTimeoutMillis;
    private Long phyMaxUseCount;

    private Boolean keepAlive;
    private Boolean poolPreparedStatements;
    private Boolean initVariants;
    private Boolean initGlobalVariants;
    private Boolean useUnfairLock;
    private Boolean killWhenSocketReadTimeout;
    private Properties connectionProperties;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String initConnectionSqls;
    private Boolean sharePreparedStatements;
    /**
     * 创建连接失败后的重试次数，默认为0, 表示一直重试
     */
    private Integer connectionErrorRetryAttempts;
    /**
     * 创建连接失败后终止创建线程，是true，否false. 默认为false，表示会在休眠一段时间后继续创建连接
     */
    private Boolean breakAfterAcquireFailure;
    private Boolean removeAbandoned;
    private Integer removeAbandonedTimeoutMillis;
    private Boolean logAbandoned;
    private Integer queryTimeout;
    private Integer transactionQueryTimeout;
    private String publicKey;
    private Integer connectTimeout;
    private Integer socketTimeout;
    /**
     * 连接重试间隔，单位毫秒
     */
    private Long timeBetweenConnectErrorMillis;

    private Map<String, Object> wall = new HashMap<>();
    private Map<String, Object> slf4j = new HashMap<>();
    private Map<String, Object> log4j = new HashMap<>();
    private Map<String, Object> log4j2 = new HashMap<>();
    private Map<String, Object> commonsLog = new HashMap<>();
    private Map<String, Object> stat = new HashMap<>();

    private String proxyFilters;
}
