package com.lc.framework.redis.starter.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author : Lu Cheng
 * @Date : 2022/12/11 19:08
 * @Version : 1.0
 */
public class JsonShiroSessionSerializer<T> implements RedisSerializer<T> {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public Class<T> clazz;

    public JsonShiroSessionSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    static {
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(
                JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.SkipTransientField, false);
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if(o == null) {
            return new byte[0];
        }
        return JSON.toJSONString(o, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return JSON.parseObject(new String(bytes, DEFAULT_CHARSET), clazz);
    }
}
