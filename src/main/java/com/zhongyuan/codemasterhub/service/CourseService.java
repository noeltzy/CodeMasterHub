package com.zhongyuan.codemasterhub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhongyuan.codemasterhub.model.VO.CourseVo;
import com.zhongyuan.codemasterhub.model.domain.Course;
import com.zhongyuan.codemasterhub.model.dto.course.CourseAddRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseQueryRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseUpdateRequest;

import java.util.List;

/**
* @author Windows11
* @description 针对表【cmh_courses(课程表)】的数据库操作Service
* @createDate 2024-12-09 21:10:40
*/
public interface CourseService extends IService<Course> {

    Page<CourseVo> queryVoList(CourseQueryRequest courseQueryRequest);

    CourseVo getVoById(Long id);

    List<CourseVo> getCourseVoByIdList(List<Long> favoriteCourseIdList);

    boolean existById(Long id);

    void add(CourseAddRequest addCourseRequest);

    void updateByAdmin(CourseUpdateRequest updateCourseRequest);

    Page<Course> queryList(CourseQueryRequest listCourseRequest);
}
