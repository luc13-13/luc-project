package ${package}.mapper;

import ${package}.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <pre>
 * 用户数据访问层
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND status = 1")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE email = #{email} AND status = 1")
    User findByEmail(@Param("email") String email);

    /**
     * 查询启用状态的用户列表
     *
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user WHERE status = 1 ORDER BY create_time DESC")
    List<User> findActiveUsers();

    /**
     * 根据状态查询用户数量
     *
     * @param status 状态
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE status = #{status}")
    Long countByStatus(@Param("status") Integer status);
}
