package com.lc.auth.server.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/9/28 09:40
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TextEncryptor textEncryptor;

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    private final AuthenticationSuccessHandler loginSuccessHandler;

    private final AuthenticationFailureHandler loginFailureHandler;

    public AuthController(TextEncryptor textEncryptor, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository, AuthenticationSuccessHandler loginSuccessHandler, AuthenticationFailureHandler loginFailureHandler) {
        this.textEncryptor = textEncryptor;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @PostMapping("/login")
    public void login(@RequestParam("username") String username,
                      @RequestParam("password")String password,
                      HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        // RSA解密
        String decryptedPassword = textEncryptor.decrypt(password);
        // 认证流程
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                decryptedPassword);
        try {
            Authentication authenticationResult = authenticationManager.authenticate(authRequest);
            // 保存认证结果
            SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.getContextHolderStrategy().setContext(context);
            log.info("登陆成功, context: {}", context);
            securityContextRepository.saveContext(context, request, response);
            // 认证成功跳转
            loginSuccessHandler.onAuthenticationSuccess(request, response, authenticationResult);
        } catch (AuthenticationException ex) {
            // 认证失败清理context
            SecurityContextHolder.getContextHolderStrategy().clearContext();
            // 认证失败跳转
            loginFailureHandler.onAuthenticationFailure(request, response, ex);
        } catch (Exception ex) {
            SecurityContextHolder.getContextHolderStrategy().clearContext();
            loginFailureHandler.onAuthenticationFailure(request, response, new SessionAuthenticationException("登陆失败"));
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
    }
}
