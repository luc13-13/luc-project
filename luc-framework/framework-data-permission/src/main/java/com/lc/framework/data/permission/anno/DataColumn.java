package com.lc.framework.data.permission.anno;

import com.lc.framework.data.permission.entity.DataColumnType;
import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

import static com.lc.framework.data.permission.entity.DataColumnType.TENANT;

/**
 * 表示sql中用于数据权限过滤的字段别名，例如, 部门deptA的员工根据姓名模糊查询，mapper方法为
 * {@code @DataScope({DataColumn(alias = 'u.dept_id', type = DataColumnType.SYS_DEPT)}})
 * <pre>
 * {@code
 *      public class Mapper {
 *          // 对应的sql语句为
 *          // SELECT u.id, u.display_name, u.email, u.dept_id
 *          // FROM sys.user u
 *          // WHERE u.display_name LIKE CONCAT(#{name, jdbcType=VARCHAR},'%')
 *          @DataScope({@DataColumn(handler = DataColumnType.SYS_USER)})
 *          List<User> fuzzyQueryUserList(String displayName name);
 *          // 经过DataColumnType.SYS_DEPT对应的拦截器处理后的sql为
 *          // SELECT u.id, u.display_name, u.email, u.dept_id
 *          // FROM sys.user u
 *          // WHERE u.display_name LIKE CONCAT(#{name, jdbcType=VARCHAR},'%')
 *          // AND u.dept_id = #{dataScope.deptId, jdbcType=VARCHAR}
 *      }
 *  }
 *  </pre>
 * 这里dataScope为方法中参数名，
 * </pre>
 *
 * @author Lu Cheng
 * @create 2023-08-01 09:56
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(DataScope.class)
public @interface DataColumn {
    /**
     * 权限处理器类型
     */
    DataColumnType handler() default TENANT;

    /**
     * 数据库字段数据类型
     */
    JdbcType jdbcType() default JdbcType.VARCHAR;
}
