package ${package}.service;

import ${package}.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <pre>
 * 用户服务接口
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 查询启用状态的用户列表
     *
     * @return 用户列表
     */
    List<User> findActiveUsers();

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 创建结果
     */
    boolean createUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 启用/禁用用户
     *
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @return 操作结果
     */
    boolean updateUserStatus(Long userId, Integer status);
}
