package com.zhongyuan.codemasterhub.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.IdRequest;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import com.zhongyuan.codemasterhub.model.dto.courseTag.CourseTagAddRequest;
import com.zhongyuan.codemasterhub.model.dto.courseTag.CourseTagQueryRequest;
import com.zhongyuan.codemasterhub.model.dto.courseTag.CourseTagUpdateRequest;
import com.zhongyuan.codemasterhub.service.CourseService;
import com.zhongyuan.codemasterhub.service.CourseTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.zhongyuan.codemasterhub.constant.RedisKeyConstant.TAG_MAP_REDIS;

@RestController
@RequestMapping("/admin/course/tag")
@Slf4j
public class AdminCourseTagController {
    @Resource
    CourseTagService courseTagService;
    @Autowired
    private CourseService courseService;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/{id}")
    public BaseResponse<CourseTag> getCourseTagById(@PathVariable Long id) {
        ThrowUtils.throwIf((id == null || id <= 0), ErrorCode.PARAMS_ERROR);
        CourseTag tag = courseTagService.getById(id);
        ThrowUtils.throwIf(tag == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(tag);
    }


    @PostMapping("/list")
    public BaseResponse<Page<CourseTagVo>> getCourseTagList(@RequestBody CourseTagQueryRequest courseTagQueryRequest) {
        ThrowUtils.throwIf(courseTagQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<CourseTagVo> courseTagVoPage = courseTagService.queryList(courseTagQueryRequest);
        return ResultUtils.success(courseTagVoPage);
    }

    //增
    @PostMapping("/add")
    public BaseResponse<String> addCourseTag(@RequestBody CourseTagAddRequest courseTagAddRequest) {
        ThrowUtils.throwIf(courseTagAddRequest == null, ErrorCode.PARAMS_ERROR);
        CourseTag courseTag = new CourseTag();
        BeanUtils.copyProperties(courseTagAddRequest, courseTag);
        // TODO 检查标签名字是否重复
        courseTagService.save(courseTag);
        // 删除缓存
        redisTemplate.delete(TAG_MAP_REDIS);
        return ResultUtils.success();
    }


    //update
    @PostMapping("/update")
    public BaseResponse<String> updateCourseTag(@RequestBody CourseTagUpdateRequest courseTagUpdateRequest) {
        ThrowUtils.throwIf(courseTagUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        CourseTag courseTag = new CourseTag();
        BeanUtils.copyProperties(courseTagUpdateRequest, courseTag);
        courseTagService.updateById(courseTag);
        // 删除缓存
        redisTemplate.delete(TAG_MAP_REDIS);
        return ResultUtils.success();
    }

    @PostMapping("/delete")
    public BaseResponse<String> deleteCourseTag(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean res = courseService.removeById(idRequest.getId());
        ThrowUtils.throwIf(!res, ErrorCode.NOT_FOUND_ERROR);
        //删除缓存
        redisTemplate.delete(TAG_MAP_REDIS);
        return ResultUtils.success();
    }

}
