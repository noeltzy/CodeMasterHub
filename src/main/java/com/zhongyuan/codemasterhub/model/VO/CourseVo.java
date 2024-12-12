package com.zhongyuan.codemasterhub.model.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.model.domain.Course;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
public class CourseVo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 讲师ID，关联讲师表
     */
    private String instructorName;


    private List<CourseTagVo> tags;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 封面图片URL
     */
    private String coverImage;


    /**
     * 课程描述
     */
    private String description;

    /**
     * 学习人数
     */
    private Integer studentCount;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 视频时长(秒)
     */
    private Integer duration;

    public static CourseVo ObjToVo(Course course, Map<String, CourseTagVo> tagsMap) {
        if(course == null) {
            return null;
        }
        String tagsString = course.getTags();
        // 如果tag为空 返回空List
        List<CourseTagVo> tagVoList = new ArrayList<>();
        // 不为空往里添数据
        if (StringUtils.isNotBlank(tagsString)) {
            String[] split = course.getTags().split(",");
            for (String tag : split) {
                CourseTagVo tagVo = tagsMap.get(tag);
                tagVoList.add(tagVo);
            }
        }
        //设置值，TODO 不明确对象嵌套BeanUtils如何使用
        CourseVo courseVo = new CourseVo();
        courseVo.setId(course.getId());
        courseVo.setName(course.getName());
        // TODO 扩展点,接入授课老师表
        courseVo.setInstructorName("默认老师");
        courseVo.setTags(tagVoList);
        courseVo.setPrice(course.getPrice());
        courseVo.setCoverImage(course.getCoverImage());
        courseVo.setDescription(course.getDescription());
        courseVo.setStudentCount(course.getStudentCount());
        courseVo.setVideoUrl(course.getVideoUrl());
        courseVo.setDuration(course.getDuration());
        return courseVo;
    }


}
