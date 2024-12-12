package com.zhongyuan.codemasterhub.controller;


import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.service.CourseTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class CourseTagController {
    @Resource
    CourseTagService courseTagService;

    // #TODO 父子标签重构再说
    @GetMapping("/all")
    public BaseResponse<List<CourseTagVo>> getTags() {
        List<CourseTagVo> list = courseTagService.getAllTagVo();
        return ResultUtils.success(list);
    }
}
