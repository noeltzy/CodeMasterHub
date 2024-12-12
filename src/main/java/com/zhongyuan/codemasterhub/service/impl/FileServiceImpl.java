package com.zhongyuan.codemasterhub.service.impl;

import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.service.FileService;
import com.zhongyuan.codemasterhub.utils.OssUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileServiceImpl implements FileService {
    @Resource
    private OssUtils ossUtils;


    @Override
    public String uploadAvatar(MultipartFile file) {
        //基础校验
        ThrowUtils.throwIf(!OssUtils.ImageTypes.contains(file.getContentType()),
                ErrorCode.PARAMS_ERROR,"头像类型文件错误");
        return ossUtils.upload(file,OssUtils.IMG_AVATAR);
    }
}
