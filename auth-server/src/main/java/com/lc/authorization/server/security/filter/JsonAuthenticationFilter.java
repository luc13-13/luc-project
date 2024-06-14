package com.lc.authorization.server.security.filter;

import com.lc.authorization.server.domain.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 14:48
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter() {
        super();
    }

    /**
     * 前后端分离的项目中， loginFormUrl对应着后端登录接口
     * @author Lu Cheng
     * @create 2023/10/21
     */
    public JsonAuthenticationFilter(String loginApi) {
        super();
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginApi,
                "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getMethod().equals("POST") && request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            try (InputStream inputStream = request.getInputStream()) {
                LoginRequestDTO loginRequestDTO = objectMapper.readValue(inputStream, LoginRequestDTO.class);
                String username = loginRequestDTO.getUsername();
                String password = loginRequestDTO.getPassword();
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                        .unauthenticated(username, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.attemptAuthentication(request, response);
    }

    // 防止初始化后报错缺少authenticationManager
    @Override
    public void afterPropertiesSet() {

    }
}
