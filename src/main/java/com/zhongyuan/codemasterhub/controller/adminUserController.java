package com.zhongyuan.codemasterhub.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.IdRequest;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.domain.User;
import com.zhongyuan.codemasterhub.model.dto.user.AddUserRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UpdateUserRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserQueryRequest;
import com.zhongyuan.codemasterhub.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@Slf4j
public class adminUserController {

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        ThrowUtils.throwIf((id == null || id <= 0), ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    @PostMapping("/list")
    public BaseResponse<Page<User>> getUserList(@RequestBody UserQueryRequest listUserRequest) {
        ThrowUtils.throwIf(listUserRequest == null, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.queryUserList(listUserRequest);
        return ResultUtils.success(userPage);
    }

    //增
    @PostMapping("/add")
    public BaseResponse<String> addUser(@RequestBody AddUserRequest addUserRequest) {
        ThrowUtils.throwIf(addUserRequest == null, ErrorCode.PARAMS_ERROR);
        userService.addUser(addUserRequest);
        return ResultUtils.success();
    }


    //update
    @PostMapping("/update")
    public BaseResponse<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        ThrowUtils.throwIf(updateUserRequest == null, ErrorCode.PARAMS_ERROR);
        userService.updateUserByAdmin(updateUserRequest);
        return ResultUtils.success();
    }

    @PostMapping("/delete")
    public BaseResponse<String> deleteUser(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null||idRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        boolean res = userService.removeById(idRequest.getId());
        ThrowUtils.throwIf(!res, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success();
    }
}
