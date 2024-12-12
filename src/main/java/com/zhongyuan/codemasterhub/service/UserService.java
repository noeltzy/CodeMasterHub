package com.zhongyuan.codemasterhub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhongyuan.codemasterhub.model.VO.UserVo;
import com.zhongyuan.codemasterhub.model.domain.User;
import com.zhongyuan.codemasterhub.model.dto.user.UserAddRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserUpdateRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserLoginRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserQueryRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Windows11
* @description 针对表【cmh_users(用户表)】的数据库操作Service
* @createDate 2024-12-06 01:15:29
*/
public interface UserService extends IService<User> {

    void userLogin(UserLoginRequest loginRequest, HttpServletRequest request);

    String getCode(String phone);

    UserVo getCurrentUserVo(HttpServletRequest request);

    void updateUserByUser(UserUpdateRequest updateUserRequest, HttpServletRequest request);

    Page<User> queryList(UserQueryRequest listUserRequest);


    void add(UserAddRequest addUserRequest);

    void updateByAdmin(UserUpdateRequest updateUserRequest);
}
