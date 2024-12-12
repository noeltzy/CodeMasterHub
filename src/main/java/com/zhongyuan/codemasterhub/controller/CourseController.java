package com.zhongyuan.codemasterhub.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.VO.CourseVo;
import com.zhongyuan.codemasterhub.model.dto.course.CourseQueryRequest;
import com.zhongyuan.codemasterhub.service.CourseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/list")
    public BaseResponse<Page<CourseVo>> getCourseList(@RequestBody CourseQueryRequest courseQueryRequest) {
        ThrowUtils.throwIf(courseQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<CourseVo> coursePage = courseService.queryVoList(courseQueryRequest);
        return ResultUtils.success(coursePage);
    }

    @GetMapping("/{id}")
    public BaseResponse<CourseVo> getCourseById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null||id<=0, ErrorCode.PARAMS_ERROR);
        CourseVo courseVo = courseService.getVoById(id);
        return ResultUtils.success(courseVo);
    }
}
