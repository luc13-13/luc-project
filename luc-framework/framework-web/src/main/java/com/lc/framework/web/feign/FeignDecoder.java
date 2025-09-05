package com.lc.framework.web.feign;

import com.lc.framework.core.mvc.WebResult;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import jakarta.annotation.Nonnull;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.lc.framework.core.mvc.StatusConstants.CODE_BIZ_ERROR;

/**
 * @author Lu Cheng
 * @since 2024/6/23
 */
public class FeignDecoder implements Decoder {

    private final SpringDecoder decoder;

    public FeignDecoder(SpringDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Method method = response.request().requestTemplate().methodMetadata().method();
        boolean notTheSame = method.getReturnType() != WebResult.class;
        if (notTheSame) {
            Type newType = new ParameterizedType() {

                @Nonnull
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{type};
                }

                @Nonnull
                @Override
                public Type getRawType() {
                    return WebResult.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
            WebResult<?> result = (WebResult<?>) this.decoder.decode(response, newType);
            if (WebResult.isSuccess(result)) {
                return result.getData();
            } else {
                throw new DecodeException(CODE_BIZ_ERROR, result.getMsg(), response.request());
            }
        }
        return this.decoder.decode(response, type);
    }
}
