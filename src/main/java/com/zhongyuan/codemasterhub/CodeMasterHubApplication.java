package com.zhongyuan.codemasterhub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

@SpringBootApplication
@MapperScan("com.zhongyuan.codemasterhub.mapper")
public class CodeMasterHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeMasterHubApplication.class, args);
    }

}
