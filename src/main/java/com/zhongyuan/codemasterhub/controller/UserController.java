package com.zhongyuan.codemasterhub.controller;

import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.constant.UserConstant;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.VO.UserVo;
import com.zhongyuan.codemasterhub.model.dto.user.UpdateUserRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserLoginRequest;
import com.zhongyuan.codemasterhub.service.UserService;
import com.zhongyuan.codemasterhub.utils.RequestParamsUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @param phone 手机号码
     * @return 验证码
     */
    @GetMapping("/code")
    public BaseResponse<String> code(@RequestParam String phone) {
        // 校验错误
        ThrowUtils.throwIf(!RequestParamsUtils.matchPhone(phone),
                ErrorCode.PARAMS_ERROR, "手机号码错误");
        String code = userService.getCode(phone);
        return ResultUtils.success(code);
    }

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(loginRequest == null, ErrorCode.PARAMS_ERROR);
        userService.userLogin(loginRequest, request);
        return ResultUtils.success();
    }
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        log.info("logout");
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success();
    }

    @GetMapping("/current")
    public BaseResponse<UserVo> getCurrentUser(HttpServletRequest request) {
        UserVo vo = userService.getCurrentUserVo(request);
        return ResultUtils.success(vo);
    }

    @PostMapping("/update")
    public BaseResponse<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(updateUserRequest == null, ErrorCode.PARAMS_ERROR);
        userService.updateUserByUser(updateUserRequest,request);
        return ResultUtils.success();
    }
}
