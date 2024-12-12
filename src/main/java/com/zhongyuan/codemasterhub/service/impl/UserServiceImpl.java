package com.zhongyuan.codemasterhub.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.constant.UserConstant;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.VO.UserVo;
import com.zhongyuan.codemasterhub.model.domain.User;
import com.zhongyuan.codemasterhub.mapper.UserMapper;
import com.zhongyuan.codemasterhub.model.dto.user.UserAddRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserUpdateRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserLoginRequest;
import com.zhongyuan.codemasterhub.model.dto.user.UserQueryRequest;
import com.zhongyuan.codemasterhub.service.UserService;
import com.zhongyuan.codemasterhub.utils.NetUtils;
import com.zhongyuan.codemasterhub.utils.RequestParamsUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zhongyuan.codemasterhub.constant.RedisKeyConstant.PHONE_CODE_PREFIX;
import static com.zhongyuan.codemasterhub.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Windows11
 * @description 针对表【cmh_users(用户表)】的数据库操作Service实现
 * @createDate 2024-12-06 01:15:29
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录 TODO:登录成功就直接返回OK 不用返回用户信息
     *
     * @param loginRequest 登录请求参数
     * @param request      请求体
     * @return 登录的用户信息
     */
    @Override
    public void userLogin(UserLoginRequest loginRequest, HttpServletRequest request) {
        String code = loginRequest.getCode();
        String phone = loginRequest.getPhone();
        boolean hasMissingFields = StringUtils.isAnyBlank(code, phone);
        boolean formatError = !RequestParamsUtils.matchPhone(phone);
        ThrowUtils.throwIf(hasMissingFields || formatError, ErrorCode.PARAMS_ERROR);

        // 手机号码是否注册 TODO 若没用户可以直接注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhoneNumber, phone);
        User user = this.getOne(queryWrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "手机号码不存在");


        // 从Redis查询code是否过期 redis-key设计为 projectName:phoneNumber:code
        String codeFromRedis = this.getCodeFromRedis(phone);
        ThrowUtils.throwIf(!codeFromRedis.equals(code), ErrorCode.PARAMS_ERROR, "验证码不正确,请重新输入");
        //登录成功 记录登录状态，修改最后登录时间
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        //数据库修改当前登录时间 这个不应该影响正常登录流程，日志记录就行  TODO 可以优化 多线程OR任务调度

        String ip = NetUtils.getIpAddress(request);
        User updateUser = new User();
        updateUser.setLastLoginIp(ip);
        updateUser.setId(user.getId());
        Date now = new Date();
        updateUser.setLastLoginTime(now);
        boolean result = this.updateById(updateUser);
        log.info("User {} Login At {} IP: {}, Log Result:{} ", user, now, ip, result);
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号码
     * @return 验证码
     */
    @Override
    public String getCode(String phone) {

        String code = this.getCodeFromRedis(phone);
        // TODO 优化点 高并发优化
        ThrowUtils.throwIf(code != null, ErrorCode.OPERATION_ERROR, "频繁,请稍后再试");
        code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().setIfAbsent("cmh:code:" + phone, code, 60, TimeUnit.SECONDS);

        log.info("phone: {} code:{}", phone, code);
        return code;
    }

    /**
     * 获取当前登录用户
     *
     * @param request 请求体
     * @return 用户Vo
     */
    @Override
    public UserVo getCurrentUserVo(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return UserVo.Obj2VO(user);
    }

    public User getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(user==null, ErrorCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public void updateUserByUser(UserUpdateRequest updateUserRequest, HttpServletRequest request) {
        User currentUser = this.getCurrentUser(request);

        // 自己只能改自己的
        ThrowUtils.throwIf(!Objects.equals(currentUser.getId(), updateUserRequest.getId())
                , ErrorCode.NOT_LOGIN_ERROR);
        // 创建临时更新User
        User user = new User();
        BeanUtils.copyProperties(updateUserRequest, user);
        // 这两个字段普通用户不允许修改
        user.setStatus(currentUser.getStatus());
        user.setRole(currentUser.getRole());
        // 更新失败 操作失败
        ThrowUtils.throwIf(!this.updateById(user), ErrorCode.OPERATION_ERROR);

        User updatedUser = lambdaQuery().eq(User::getId, user.getId()).one();
        //写入缓存：
        request.getSession().setAttribute(USER_LOGIN_STATE, updatedUser);
    }

    @Override
    public Page<User> queryList(UserQueryRequest listUserRequest) {
        long current = listUserRequest.getCurrent();
        long size = listUserRequest.getPageSize();
        return this.page(new Page<>(current, size),
                getQueryWrapper(listUserRequest));
    }

    public Wrapper<User> getQueryWrapper(UserQueryRequest listUserRequest) {
        // like
        String phoneNumber = listUserRequest.getPhoneNumber();
        // like
        String username = listUserRequest.getUsername();
        //like
        String nickname = listUserRequest.getNickname();
        //eq
        Integer status = listUserRequest.getStatus();
        //大于等于
        Date lastLoginTimeFrom = listUserRequest.getLastLoginTimeFrom();
        //小于等于
        Date lastLoginTimeTo = listUserRequest.getLastLoginTimeTo();
        //like
        String lastLoginIp = listUserRequest.getLastLoginIp();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(phoneNumber != null, User::getPhoneNumber, phoneNumber);
        queryWrapper.like(username != null, User::getUsername, username);
        queryWrapper.like(nickname != null, User::getNickname, nickname);
        queryWrapper.eq(status != null, User::getStatus, status);
        queryWrapper.ge(lastLoginTimeFrom != null, User::getLastLoginTime, lastLoginTimeFrom);
        queryWrapper.le(lastLoginTimeTo != null, User::getLastLoginTime, lastLoginTimeTo);
        queryWrapper.like(lastLoginIp != null, User::getLastLoginIp, lastLoginIp);
        queryWrapper.orderByDesc(User::getLastLoginTime);
        return queryWrapper;
    }

    @Override
    public void add(UserAddRequest addUserRequest) {
        String phoneNumber = addUserRequest.getPhoneNumber();
        String username = addUserRequest.getUsername();
        String nickname = addUserRequest.getNickname();
        String role = addUserRequest.getRole();
        // 用户名和手机号码都不能重复
        Long count = this.query().eq("phoneNumber", phoneNumber)
                .or().eq("username",username).count();
        ThrowUtils.throwIf(count!=0,ErrorCode.PARAMS_ERROR,"用户重复");
        // 新建用户 插入
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setUsername(username);
        user.setNickname(nickname==null?"Default":nickname);
        user.setRole(role);
        user.setStatus(1);
        ThrowUtils.throwIf(!this.save(user),ErrorCode.SYSTEM_ERROR,"系统错误，添加失败");
    }

    @Override
    public void updateByAdmin(UserUpdateRequest updateUserRequest) {
        // 创建临时更新User
        User user = new User();
        BeanUtils.copyProperties(updateUserRequest, user);
        // 更新失败 操作失败
        ThrowUtils.throwIf(!this.updateById(user), ErrorCode.OPERATION_ERROR);
    }

    /**
     * 从redis 查询某个手机号码的验证码
     *
     * @param phone 手机号码
     * @return 验证码 ||NULL
     */
    private String getCodeFromRedis(String phone) {
        // 生成CODE 6 位数
        return (String) redisTemplate.opsForValue().get(PHONE_CODE_PREFIX + phone);

    }
}




