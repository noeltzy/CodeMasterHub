package com.zhongyuan.codemasterhub.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程标签表
 * @TableName cmh_course_tags
 */
@TableName(value ="cmh_course_tags")
@Data
public class CourseTag implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态(1:启用 0:禁用)
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