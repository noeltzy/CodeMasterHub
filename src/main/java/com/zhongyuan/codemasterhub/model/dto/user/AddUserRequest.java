package com.zhongyuan.codemasterhub.model.dto.user;


import lombok.Data;

import java.io.Serializable;

@Data
public class AddUserRequest implements Serializable {
    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 角色（ADMIN:管理员/USER:普通用户）
     */
    private String role;
}
