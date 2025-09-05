package com.lc.auth.server.security.core;

import com.lc.framework.core.mvc.WebResult;
import com.lc.system.api.SysUserDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lc.framework.core.constants.RequestHeaderConstants.USER_NAME;

/**
 * 测试用户详情服务
 * 用于支持 admin/admin 登录
 */
@Service
@Slf4j
public class TestUserDetailsService implements LoginUserDetailService {

    private final PasswordEncoder passwordEncoder;

    public TestUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final RestClient restClient = RestClient.builder().baseUrl("http://127.0.0.1:19003").build();

    @Override
    public LoginUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("尝试加载用户: {}", username);
        // TODO: 引入consul后，利用FeignDecoder特性，可直接返回结果
        WebResult<SysUserDetailDTO> webResult = restClient.get().uri(UriComponentsBuilder.fromUriString("/user/detail")
                        .queryParam("username", username).build().toUri())
                .httpRequest(request -> request.getHeaders().set(USER_NAME, username))
                .retrieve().body(new ParameterizedTypeReference<>() {
                });
        if (webResult != null && webResult.getData() != null) {
            SysUserDetailDTO sysUserDTO = webResult.getData();
            log.info("用户加载结果: {}", sysUserDTO);
            LoginUserDetail user = LoginUserDetail.builder()
                    .id(sysUserDTO.getUserId())
                    .username(sysUserDTO.getUserName())
                    .password(sysUserDTO.getPassword())
                    // 角色记authorities
                    .authorities(sysUserDTO.getRoleAuthoritiesMap().keySet().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()))
                    .accountNonExpired(sysUserDTO.getStatus())
                    .accountNonLocked(sysUserDTO.getStatus())
                    .credentialsIssuedAt(Instant.now())
                    .build();
            user.getAttributes().put("role_authorities", sysUserDTO.getRoleAuthoritiesMap());
            log.info("返回用户详情: {}", user);
            return user;
        }

        log.error("用户不存在: {}", username);
        throw new UsernameNotFoundException("用户不存在: " + username);
    }

    @Override
    public LoginUserDetail loadByMobile(String mobile) {
        try {
            // 简化处理：如果手机号是 13800138000，则映射到 admin 用户
            if ("13800138000".equals(mobile)) {
                LoginUserDetail user = LoginUserDetail.builder()
                        .username("admin")
                        .authorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")))
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsIssuedAt(Instant.now())
                        .build();

                log.info("根据手机号 {} 返回用户详情: {}", mobile, user);
                return user;
            }

            // 其他手机号暂时不支持
            throw new UsernameNotFoundException("手机号未绑定用户: " + mobile);

        } catch (UsernameNotFoundException e) {
            log.error("根据手机号 {} 查找用户失败", mobile);
            throw new BadCredentialsException("手机号未绑定用户");
        }
    }

    @Override
    public LoginUserDetail loadByMail(String mail) {
        return null;
    }

    @Override
    public LoginUserDetail loadByUserId(String userId) {
        return null;
    }

    @Override
    public boolean support(String clientId) {
        return false;
    }
}
