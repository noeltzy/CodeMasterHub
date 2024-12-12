package com.zhongyuan.codemasterhub.constant;

public interface SqlExpressionConstant {
    String TOGGLE_LIKE_EXPRESSION = "liked = CASE WHEN liked = 1 THEN 0 WHEN liked = 0 THEN 1 ELSE liked END";
}
