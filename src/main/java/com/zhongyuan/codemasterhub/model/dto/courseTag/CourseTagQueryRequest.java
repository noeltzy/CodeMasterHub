package com.zhongyuan.codemasterhub.model.dto.courseTag;


import com.zhongyuan.codemasterhub.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
public class CourseTagQueryRequest extends PageRequest implements Serializable {
    /**
     * 标签名称
     */
    private String name;
}
