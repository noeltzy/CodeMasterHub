package com.zhongyuan.codemasterhub.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private String phone;
    private String code;
}
