package com.lc.framework.web.config;

import com.lc.framework.web.feign.FeignDecoder;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Deprecated for Spring boot 4.x. In favor of {@link HttpExchange}
 * @author lucheng
 * @since 2024/6/14
 * @deprecated
 * @see HttpExchange
 * @see org.springframework.web.service.registry.ImportHttpServices
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public class FeignDecoderConfig {

    @Bean
    public Decoder feignDecoder(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        return new OptionalDecoder((new ResponseEntityDecoder(new FeignDecoder(new SpringDecoder(messageConverters)))));
    }

}
