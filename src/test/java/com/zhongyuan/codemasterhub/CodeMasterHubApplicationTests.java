package com.zhongyuan.codemasterhub;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.zhongyuan.codemasterhub.common.RedisObject;
import com.zhongyuan.codemasterhub.mapper.UserMapper;
import com.zhongyuan.codemasterhub.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class CodeMasterHubApplicationTests {

    @Resource
    RedisTemplate<String,Object> redisTemplate;



    @Test
    public void tt() {
        User user = new User();
        user.setId(1L);
        RedisObject <String> redisObject =new RedisObject<>();
        redisObject.setObj(null);
        redisObject.setExpired(LocalDateTime.now().plusDays(1));

        redisTemplate.opsForValue().set("bbb",redisObject);
        RedisObject o =(RedisObject) redisTemplate.opsForValue().get("bbb");
        if(o!=null){
            System.out.println(o.getObj()==null);
        }
        else{
            System.out.println("Null");
        }

    }

}
