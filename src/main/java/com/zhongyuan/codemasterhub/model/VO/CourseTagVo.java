package com.zhongyuan.codemasterhub.model.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhongyuan.codemasterhub.model.domain.CourseTag;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseTagVo implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    public static CourseTagVo ObjToVo(CourseTag tag) {
        CourseTagVo vo = new CourseTagVo();
        vo.setId(tag.getId());
        vo.setName(tag.getName());
        return vo;
    }

}
