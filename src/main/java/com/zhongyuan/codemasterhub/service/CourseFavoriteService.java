package com.zhongyuan.codemasterhub.service;

import com.zhongyuan.codemasterhub.model.domain.CourseFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author Windows11
* @description 针对表【cmh_course_favorites(课程收藏表)】的数据库操作Service
* @createDate 2024-12-10 13:57:04
*/
public interface CourseFavoriteService extends IService<CourseFavorite> {

    List<Long> getFavoriteCourseIdSet(Long userId);

    void likeCourse(Long courseId, Long userId);
}
