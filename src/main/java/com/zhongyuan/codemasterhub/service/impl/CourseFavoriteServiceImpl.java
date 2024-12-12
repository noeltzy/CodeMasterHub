package com.zhongyuan.codemasterhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.domain.CourseFavorite;
import com.zhongyuan.codemasterhub.service.CourseFavoriteService;
import com.zhongyuan.codemasterhub.mapper.CourseFavoriteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zhongyuan.codemasterhub.constant.SqlExpressionConstant.TOGGLE_LIKE_EXPRESSION;

/**
 * @author Windows11
 * @description 针对表【cmh_course_favorites(课程收藏表)】的数据库操作Service实现
 * @createDate 2024-12-10 13:57:04
 */
@Service
public class CourseFavoriteServiceImpl extends ServiceImpl<CourseFavoriteMapper, CourseFavorite>
        implements CourseFavoriteService {


    @Override
    public List<Long> getFavoriteCourseIdSet(Long userId) {
        LambdaQueryWrapper<CourseFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseFavorite::getUserId, userId)
                .eq(CourseFavorite::getLiked, 1)
                .select(CourseFavorite::getCourseId);
        return baseMapper.selectObjs(wrapper);
    }

    @Override
    public void likeCourse(Long courseId, Long userId) {
        //1. 查询收藏表中是否有记录
        boolean exists = this.lambdaQuery()
                .eq(CourseFavorite::getCourseId, courseId)
                .eq(CourseFavorite::getUserId, userId)
                .exists();
        //2.如果没没记录添加收藏记录 表示收藏然后返回
        if (!exists) {
            CourseFavorite courseFavorite = new CourseFavorite();
            courseFavorite.setCourseId(courseId);
            courseFavorite.setUserId(userId);
            courseFavorite.setLiked(1);
            ThrowUtils.throwIf(!this.save(courseFavorite), ErrorCode.SYSTEM_ERROR);
            return;
        }
        //3.取反 SET CASE语法
        boolean res = this.lambdaUpdate()
                .eq(CourseFavorite::getCourseId, courseId)  // 条件：课程ID
                .eq(CourseFavorite::getUserId, userId)      // 条件：用户ID
                .setSql(TOGGLE_LIKE_EXPRESSION).update();// 取反操作
        ThrowUtils.throwIf(!res, ErrorCode.SYSTEM_ERROR);
    }
}




