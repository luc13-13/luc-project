package com.lc.auth.server.security.authentication.extension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * <pre>
 *     拓展登陆方式，将此Filter添加至UsernamePasswordFilter之前，
 * 利用{@link AbstractAuthenticationProcessingFilter#attemptAuthentication(HttpServletRequest, HttpServletResponse)}进行认证
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/8/4 11:31
 * @version : 1.0
 */
public class MultiTypeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/unified";

    private static final RequestMatcher DEFAULT_MATCHER = PathPatternRequestMatcher.withDefaults().matcher(DEFAULT_FILTER_PROCESSES_URI);

    public MultiTypeAuthenticationFilter(List<AuthenticationConverter> converters) {
        super(DEFAULT_MATCHER);
        super.setAuthenticationConverter(new DelegatingAuthenticationConverter(converters));
    }

    public MultiTypeAuthenticationFilter(AuthenticationConverter converter) {
        super(DEFAULT_MATCHER);
        super.setAuthenticationConverter(converter);
    }

    public MultiTypeAuthenticationFilter(String processesUri, AuthenticationConverter converter) {
        super(PathPatternRequestMatcher.withDefaults().matcher(processesUri));
        super.setAuthenticationConverter(converter);
    }
}
