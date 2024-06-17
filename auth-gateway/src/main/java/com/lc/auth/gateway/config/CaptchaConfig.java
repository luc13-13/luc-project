package com.lc.auth.gateway.config;

import com.lc.auth.gateway.config.properties.CaptchaProperties;
import com.lc.auth.gateway.enums.CaptchaTypeEnum;
import com.lc.auth.gateway.factory.CaptchaGeneratorFactory;
import com.lc.framework.core.mvc.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static com.lc.framework.core.constants.RequestHeaderConstants.JSESSIONID;

/**
 * 配置验证码， 增加路由配置，对/captcha路径进行拦截，根据request中请求的验证码类型，从factory中利用指定的验证码生成器返回response
 *
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/9/1 17:31
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route(RequestPredicates.
                                GET(captchaProperties.getUrl())
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        request -> request.exchange().getSession().mapNotNull(webSessionMono -> {
                            String sessionId = request.headers().firstHeader(JSESSIONID);
                            if (!StringUtils.hasLength(sessionId) || !StringUtils.hasText(sessionId)) {
                                sessionId = webSessionMono.getId();
                            }
                            String captcha = CaptchaGeneratorFactory.get(request.queryParam("type")
                                    .orElse(CaptchaTypeEnum.DEFAULT.toString())).generate(sessionId);

                            return ServerResponse.ok().header(JSESSIONID, sessionId).body(BodyInserters.fromValue(WebResult.successData(captcha))).block();
                        }));
    }
}
