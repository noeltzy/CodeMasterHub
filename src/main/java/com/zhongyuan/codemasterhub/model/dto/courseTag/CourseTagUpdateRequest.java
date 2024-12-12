package com.zhongyuan.codemasterhub.model.dto.courseTag;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class CourseTagUpdateRequest implements Serializable {

    private Long id;
    /**
     * 标签名称
     */
    private String name;
}