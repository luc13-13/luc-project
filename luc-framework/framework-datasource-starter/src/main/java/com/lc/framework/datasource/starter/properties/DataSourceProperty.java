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
     * JDBC driver。<br/>未设置时，将由DataSource实现类自动获取.<br/> 
     * Druid根据url加载: 支持mysql、derby、logjdbc、mariadb、tidb等，具体见{@link com.alibaba.druid.util.JdbcUtils#getDriverClassName(String)}
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
     * 分库分表数据库的配置文件<br/>支持yml文件、nacos两种配置方式:<br/>（1）classpath:本地路径(同日志配置文件声明方式)<br/>（2）nacos:data-id<br/>未指定文件来源时，默认从classpath获取
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

    /**
     * 切点表达式，支持根据表达式切换至该数据源，支持的表达式同注解@Pointcut
     * <pre>表达式规则说明
     * modifier: 修饰符，public private等，省略时表示任意修饰符
     * ret-type: 返回类型, *表示任意返回类型
     * declaring-type: 匹配目标类，省略时匹配任意类型。
     *   - '..'表示匹配包及其子包，
     *   - '+'表示匹配类及其子类
     * name-pattern: 匹配方法名，
     *   - '*'表示任意方法，
     *   - 'set*'表示任意set开头的方法
     * param-pattern: 匹配参数类型和数量，
     *   - '()'表示无参方法，
     *   - '(..)'表示任意参数数量和类型的方法，
     *   - '(*)'表示有一个任意参数的方法，
     *   - '(*,String)'表示两个参数的方法且第一个参数为任意类型第二个参数为String
     * Examples:
     * (1) execution(modifier ret-type declaring-type?name-pattern(param-pattern) throws-pattern)，匹配方法、类、包
     *   - execution(* com.lc.order.mapper.。*(..)), 匹配任意修饰符的、任意返回类型的、com.lc.order.mapper包及其子包下的、任意方法名的、任意参数的方法
     * (2) within(declaring-type)，匹配指定类的方法
     *   - within(com.lc.order.service.*), 匹配com.lc.order.service包的任意类，不能匹配接口
     *   - within(com.lc.order.service..), 匹配com.lc.order.service包及其子包的任意类
     *   - within(com.lc.order.service.Order+), 匹配com.lc.order.service.Order类及其子类
     * (3) this(declaring-type)，匹配代理对象的运行时类型
     * (4) target(declaring-type)，匹配对象的编译类型
     * (5) args(param-pattern)，匹配方法参数
     *   - args(com.lc.order.model.OrderEntity,..), 匹配第一个参数为com.lc.order.model.OrderEntity、且有至少一个参数的方法
     * (6) bean(name-pattern)，匹配bean的名称
     * (7) @within(declaring-type)，匹配使用了指定类型注解的类，无法匹配接口
     *   - @within(com.lc.order.annotation.Test)，匹配被com.lc.order.annotation.Test注解标注的类
     * (8) @target(declaring-type)，运行时对象使用了指定注解，无法匹配接口
     * (9) @annotation(declaring-type)，方法上使用了指定注解，无法匹配接口
     * (10) @args(declaring-type)，方法参数使用了指定注解<pre/>
     */
    private String pointcut;
}
