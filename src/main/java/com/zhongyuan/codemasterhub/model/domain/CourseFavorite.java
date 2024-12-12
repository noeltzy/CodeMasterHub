package com.zhongyuan.codemasterhub.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程收藏表
 * @TableName cmh_course_favorites
 */
@TableName(value ="cmh_course_favorites")
@Data
public class CourseFavorite  implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    //1 喜欢
    //0 不喜欢
    private Integer Liked;

    /**
     * 收藏时间
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