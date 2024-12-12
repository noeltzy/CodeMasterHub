package com.zhongyuan.codemasterhub.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 课程表
 * @TableName cmh_courses
 */
@TableName(value ="cmh_courses")
@Data
public class Course implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 讲师ID，关联讲师表
     */
    private Long instructorId;

    /**
     * 标签ID列表，多个以逗号分隔
     */
    private String tags;

    /**
     * 课程价格
     */
    private BigDecimal price;

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
    private Integer duration;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 学习人数
     */
    private Integer studentCount;

    /**
     * 状态(1:上架 0:下架)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（1:是 0:否）
     */
    @TableLogic
    private Integer isDeleted;

}