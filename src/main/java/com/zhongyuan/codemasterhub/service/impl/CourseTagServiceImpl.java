package com.zhongyuan.codemasterhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import com.zhongyuan.codemasterhub.service.CourseTagService;
import com.zhongyuan.codemasterhub.mapper.CourseTagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
}




