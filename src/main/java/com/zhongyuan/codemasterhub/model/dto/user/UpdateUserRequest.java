package com.zhongyuan.codemasterhub.model.dto.user;


import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateUserRequest implements Serializable {

    private Long id;
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像URL
     */
    private String avatar;


    // 管理员能修改的字段：
    /**
     * 角色（ADMIN:管理员/USER:普通用户）
     */
    private String role;

    /**
     * 状态（1:正常 0:禁用）
     */
    private Integer status;

}
