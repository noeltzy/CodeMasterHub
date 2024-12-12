package com.zhongyuan.codemasterhub.model.dto.course;

import com.zhongyuan.codemasterhub.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryRequest extends PageRequest implements Serializable {
    //标签列表
    String tags;

}
