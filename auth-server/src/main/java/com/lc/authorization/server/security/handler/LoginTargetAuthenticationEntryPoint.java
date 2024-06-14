package com.lc.authorization.server.security.handler;

import com.lc.authorization.server.utils.JsonUtils;
import com.lc.authorization.server.utils.SecurityUtils;
import com.lc.framework.core.mvc.Status;
import com.lc.framework.core.mvc.WebResult;
import com.lc.framework.redis.starter.utils.RedisHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-10-19 11:23
 */
@Slf4j
public class LoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private final String deviceActivateUri;

    private final RedisHelper redisHelper;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public LoginTargetAuthenticationEntryPoint(String loginFormUrl, String deviceActivateUri, RedisHelper redisHelper) {
        super(loginFormUrl);
        this.deviceActivateUri = deviceActivateUri;
        this.redisHelper = redisHelper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String deviceVerificationUri = "/oauth2/device_verification";
        // 兼容设备码前后端分离
        if (request.getRequestURI().equals(deviceVerificationUri)
                && request.getMethod().equals(HttpMethod.POST.name())
                && UrlUtils.isAbsoluteUrl(deviceActivateUri)) {
            // 如果是请求验证设备激活码(user_code)时未登录并且设备码验证页面是前后端分离的那种则写回json
            WebResult<String> success = WebResult.response(Status.generate(HttpStatus.UNAUTHORIZED.value(), ("登录已失效，请重新打开设备提供的验证地址")), "");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtils.objectCovertToJson(success));
            response.getWriter().flush();
            return;
        }

        // 获取登录表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        log.info("获取登陆地址: {}", loginForm);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            // 不是绝对路径调用父类方法处理
            super.commence(request, response, authException);
            return;
        }

        String requestUri = request.getRequestURI();
        StringBuilder paramBuffer = new StringBuilder();
        if (!ObjectUtils.isEmpty(request.getQueryString())) {
            paramBuffer.append("?").append(request.getQueryString());
        }

        // 2023-07-11添加逻辑：重定向地址添加nonce参数，该参数的值为sessionId
        // 绝对路径在重定向前添加target参数
        String redirectToUrl = URLEncoder.encode( "/api" + requestUri + paramBuffer, StandardCharsets.UTF_8);
        // 请求进入filter链路后， 会被前置filter经过层层包装转换为HttpServletRequestWrapper的子类， 如果不进行转换则获取不到session
//        String sessionId = request instanceof HttpServletRequestWrapper ? ((HttpServletRequestWrapper)request).getSession(false).getId() : request.getSession(Boolean.FALSE).getId() ;
        String targetUrl = loginForm; // + "?target=" + redirectToUrl + "&" + JSESSIONID + "=" + sessionId;
//        log.info("sessindId:{}, 重定向至登录页面， 登录成功后跳转路径：{}", sessionId, redirectToUrl);
//        redisHelper.set(sessionId, redirectToUrl, "redirect_url");
        redirectWithCookie(request, response, targetUrl);
    }

    private void redirectWithCookie(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws IOException {

        SecurityUtils.copyRequestCookieToResponse(request, response);
        this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }

}
