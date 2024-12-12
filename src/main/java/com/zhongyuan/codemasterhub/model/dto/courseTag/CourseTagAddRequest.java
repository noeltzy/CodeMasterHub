package com.zhongyuan.codemasterhub.model.dto.courseTag;


import lombok.Data;

import java.io.Serializable;

@Data
public class CourseTagAddRequest implements Serializable {
    /**
     * 标签名称
     */
    private String name;
}
