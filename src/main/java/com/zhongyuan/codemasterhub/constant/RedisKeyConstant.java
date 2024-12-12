package com.zhongyuan.codemasterhub.constant;

public interface RedisKeyConstant {
    String PHONE_CODE_PREFIX = "cmh:code:";
    String TAG_MAP_REDIS = "cmh:course:tags:map";
    String COURSE_DETAIL_REDIS_PREFIX = "cmh:course:detail:hash:";
    long COURSE_DETAIL_REDIS_EXPIRED_TIME = 30L;
}
