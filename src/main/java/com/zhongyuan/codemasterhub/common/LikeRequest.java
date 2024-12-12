package com.zhongyuan.codemasterhub.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeRequest implements Serializable {
    Boolean liked;
}
