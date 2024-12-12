package com.zhongyuan.codemasterhub.controller;
import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.VO.CourseVo;
import com.zhongyuan.codemasterhub.model.VO.UserVo;
import com.zhongyuan.codemasterhub.service.CourseFavoriteService;
import com.zhongyuan.codemasterhub.service.CourseService;
import com.zhongyuan.codemasterhub.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/course/favorite")
@Slf4j
public class CourseFavoriteController {
    @Resource
    CourseFavoriteService courseFavoriteService;
    @Resource
    UserService userService;

    @Resource
    CourseService courseService;

    @GetMapping("/all")
    public BaseResponse<List<CourseVo>> getMyFavoriteCourseVo(HttpServletRequest request) {
        UserVo currentUserVo = userService.getCurrentUserVo(request);
        Long userId = currentUserVo.getId();
        List<Long> favoriteCourseIdList = courseFavoriteService.getFavoriteCourseIdSet(userId);
        if (favoriteCourseIdList.isEmpty()) {
            return ResultUtils.success(List.of());
        }
        List<CourseVo> courseVoList = courseService.getCourseVoByIdList(favoriteCourseIdList);
        return ResultUtils.success(courseVoList);
    }

    @GetMapping("/ids")
    public BaseResponse<List<Long>> getMyFavoriteCourseIdList(HttpServletRequest request) {
        UserVo currentUserVo = userService.getCurrentUserVo(request);
        Long userId = currentUserVo.getId();
        //# 查询用户likes的标签Id：
        List<Long> favoriteCourseIdList = courseFavoriteService.getFavoriteCourseIdSet(userId);

        return ResultUtils.success(favoriteCourseIdList);
    }

    @PostMapping("/like/{id}")
    public BaseResponse<String> likeCourse(@PathVariable Long id, HttpServletRequest request) {
        UserVo user = userService.getCurrentUserVo(request);
        Long userId = user.getId();
        // 检查课程是否存在 TODO：Redis缓存更新
        boolean res = courseService.existById(id);
        ThrowUtils.throwIf(!res, ErrorCode.NOT_FOUND_ERROR, "收藏课程不存在");
        courseFavoriteService.likeCourse(id, userId);
        return ResultUtils.success();
    }
}
