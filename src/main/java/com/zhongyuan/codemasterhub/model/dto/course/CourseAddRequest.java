package com.zhongyuan.codemasterhub.model.dto.course;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseAddRequest implements Serializable {

    private String name;

    /**
     * 讲师ID，关联讲师表
     */
    private Long instructorId = 1L;

    /**
     * 标签ID列表，多个以逗号分隔
     */
    private String tags;

    /**
     * 课程价格
     */
    private BigDecimal price = BigDecimal.valueOf(9999);

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 视频时长(秒)
     */
    private Integer duration = 3600;

    /**
     * 课程描述
     */
    private String description;


    /**
     * 状态(1:上架 0:下架)
     */
    private Integer status = 0;

}
