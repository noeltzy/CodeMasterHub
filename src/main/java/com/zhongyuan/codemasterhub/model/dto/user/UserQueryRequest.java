package com.zhongyuan.codemasterhub.model.dto.user;

import com.zhongyuan.codemasterhub.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
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
     * 状态（1:正常 0:禁用）
     */
    private Integer status;

    /**
     * 最后登录开始
     */
    private Date lastLoginTimeFrom;

    /**
     * 最后登录结束
     */
    private Date lastLoginTimeTo;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;
}
