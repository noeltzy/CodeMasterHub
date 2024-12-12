package com.zhongyuan.codemasterhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.model.VO.CourseVo;
import com.zhongyuan.codemasterhub.model.domain.Course;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import com.zhongyuan.codemasterhub.model.dto.courseTag.CourseTagQueryRequest;
import com.zhongyuan.codemasterhub.service.CourseTagService;
import com.zhongyuan.codemasterhub.mapper.CourseTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author Windows11
* @description 针对表【cmh_course_tags(课程标签表)】的数据库操作Service实现
* @createDate 2024-12-09 21:13:23
*/
@Service
public class CourseTagServiceImpl extends ServiceImpl<CourseTagMapper, CourseTag>
    implements CourseTagService{



    @Override
    public List<CourseTagVo> getAllTagVo() {
        return list().stream().map(CourseTagVo::ObjToVo).toList();
    }

    @Override
    public Page<CourseTagVo> queryList(CourseTagQueryRequest courseTagQueryRequest) {

        int current = courseTagQueryRequest.getCurrent();
        int size = courseTagQueryRequest.getPageSize();
        String name = courseTagQueryRequest.getName();
        LambdaQueryWrapper<CourseTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null ,CourseTag::getName, courseTagQueryRequest.getName());
        // 查询Course列表
        Page<CourseTag> page = this.page(new Page<>(current, size), wrapper);

        // 转化成Vo
        List<CourseTagVo> list = page.getRecords().stream().
                map(CourseTagVo::ObjToVo).collect(Collectors.toList());
        // 封装成VoPage
        Page<CourseTagVo> courseTagVo = new Page<>(current, size, page.getTotal());
        courseTagVo.setRecords(list);
        return courseTagVo;

    }
}




