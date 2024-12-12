package com.zhongyuan.codemasterhub.utils;


import com.zhongyuan.codemasterhub.common.RedisObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RedisUtils {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 将数据缓存到Redis Hash结构中
     * @param data 数据
     * @param key Hash表的Key
     * @param hashKey Hash表的字段
     * @param expireTime 逻辑过期时间,现在过后XX 秒
     * @param <T> 存储数据的类型
     */
    private <T> void saveWithLogicExpired(T data,String key,String hashKey,long expireTime) {
        RedisObject<T> redisCourseVo = new RedisObject<>();
        redisCourseVo.setExpired(LocalDateTime.now().plusMinutes(expireTime));
        redisCourseVo.setObj(data);
        redisTemplate.opsForHash().put(key,hashKey,redisCourseVo);
    }

}
