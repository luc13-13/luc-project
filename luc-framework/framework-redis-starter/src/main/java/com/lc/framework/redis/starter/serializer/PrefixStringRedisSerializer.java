package com.lc.framework.redis.starter.serializer;

import com.lc.framework.common.constants.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * key的命名规范：{spring.application.name}:{biz-value}，服务名:业务属性 <br/>
 * 统一将spring.application.name作为key的前缀，每个服务只能操作自己的key
 *
 * @Author : Lu Cheng
 * @Date : 2022/11/27 20:57
 * @Version : 1.0
 * @deprecated 使用该序列化方式缓存的key，只能被服务自己获取到，无法在服务之间共享。在每个服务RedisHelper时声明默认的前缀，在RedisHelper中提供采用默认前缀和不采用默认前缀的两种方法
 */
@Slf4j
@Deprecated()
public class PrefixStringRedisSerializer extends StringRedisSerializer {

    private final String KEY_PREFIX;

    public PrefixStringRedisSerializer(String KEY_PREFIX) {
        super();
        this.KEY_PREFIX = KEY_PREFIX;
    }

    private String generateKey(String key) {
        return KEY_PREFIX + StringConstants.COLON + key;
    }

    /**
     * 反序列化时，如果key包含{spring.application.name}作为前缀，则去除前缀后返回key；<br/>
     * 否则正常返回byte[]的反序列化结果
     * @author Lu Cheng
     * @create 2023/7/31
     */
    @Override
    public String deserialize(byte[] bytes) {
        if (Objects.isNull(bytes) || bytes.length == 0) {
            log.error("序列化对象为空");
            return "";
        }
        String key = new String(bytes);
        int index = key.indexOf(KEY_PREFIX);
        if (index != -1) {
            log.info("反序列化的key: {}"+key.substring(index));
            return key.substring(index);
        }
        return key;
    }

    @Override
    public byte[] serialize(String string) {
        string = generateKey(string);
        return super.serialize(string);
    }
}
