package com.zhongyuan.codemasterhub.service;

import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Windows11
* @description 针对表【cmh_course_tags(课程标签表)】的数据库操作Service
* @createDate 2024-12-09 21:13:23
*/
public interface CourseTagService extends IService<CourseTag> {

    List<CourseTagVo> getAllTagVo();
}
