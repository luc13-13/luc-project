package ${package}.service.impl;

import ${package}.domain.User;
import ${package}.mapper.UserMapper;
import ${package}.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <pre>
 * 用户服务实现类
 * </pre>
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsername(String username) {
        log.debug("根据用户名查询用户: {}", username);
        return baseMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        log.debug("根据邮箱查询用户: {}", email);
        return baseMapper.findByEmail(email);
    }

    @Override
    public List<User> findActiveUsers() {
        log.debug("查询启用状态的用户列表");
        return baseMapper.findActiveUsers();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        log.info("创建用户: {}", user.getUsername());
        
        // 检查用户名是否已存在
        User existingUser = findByUsername(user.getUsername());
        if (existingUser != null) {
            log.warn("用户名已存在: {}", user.getUsername());
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (user.getEmail() != null) {
            User existingEmailUser = findByEmail(user.getEmail());
            if (existingEmailUser != null) {
                log.warn("邮箱已存在: {}", user.getEmail());
                throw new RuntimeException("邮箱已存在");
            }
        }
        
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(1); // 默认启用
        
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        log.info("更新用户信息: {}", user.getId());
        
        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        log.info("删除用户: {}", userId);
        return removeById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long userId, Integer status) {
        log.info("更新用户状态: userId={}, status={}", userId, status);
        
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        
        return updateById(user);
    }
}
