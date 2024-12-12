package com.zhongyuan.codemasterhub.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.IdRequest;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.domain.Course;
import com.zhongyuan.codemasterhub.model.dto.course.CourseQueryRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseAddRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseUpdateRequest;
import com.zhongyuan.codemasterhub.service.CourseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/course")
@Slf4j
public class AdminCourseController {

    @Resource
    private CourseService courseService;

    @GetMapping("/{id}")
    public BaseResponse<Course> getCourseById(@PathVariable Long id) {
        ThrowUtils.throwIf((id == null || id <= 0), ErrorCode.PARAMS_ERROR);
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(course);
    }


    @PostMapping("/list")
    public BaseResponse<Page<Course>> getCourseList(@RequestBody CourseQueryRequest listCourseRequest) {
        ThrowUtils.throwIf(listCourseRequest == null, ErrorCode.PARAMS_ERROR);
        Page<Course> coursePage = courseService.queryList(listCourseRequest);
        return ResultUtils.success(coursePage);
    }

    //增
    @PostMapping("/add")
    public BaseResponse<String> addCourse(@RequestBody CourseAddRequest addCourseRequest) {
        ThrowUtils.throwIf(addCourseRequest == null, ErrorCode.PARAMS_ERROR);
        courseService.add(addCourseRequest);
        return ResultUtils.success();
    }


    //update
    @PostMapping("/update")
    public BaseResponse<String> updateCourse(@RequestBody CourseUpdateRequest updateCourseRequest) {
        ThrowUtils.throwIf(updateCourseRequest == null, ErrorCode.PARAMS_ERROR);
        courseService.updateByAdmin(updateCourseRequest);
        return ResultUtils.success();
    }

    @PostMapping("/delete")
    public BaseResponse<String> deleteCourse(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean res = courseService.removeById(idRequest.getId());
        ThrowUtils.throwIf(!res, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success();
    }
}
