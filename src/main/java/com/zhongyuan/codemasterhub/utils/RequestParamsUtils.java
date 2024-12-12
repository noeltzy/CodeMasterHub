package com.zhongyuan.codemasterhub.utils;

public class RequestParamsUtils {
    //TODO 只校验了简单的长度 其他校验可以继续 如引入正则
    public static boolean matchPhone(final String phone) {
        return phone != null && phone.length() == 11;
    }
}
