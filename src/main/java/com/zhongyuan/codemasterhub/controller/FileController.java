package com.zhongyuan.codemasterhub.controller;


import com.zhongyuan.codemasterhub.common.BaseResponse;
import com.zhongyuan.codemasterhub.common.ResultUtils;
import com.zhongyuan.codemasterhub.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@Slf4j
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping("/avatar")
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String upload = fileService.uploadAvatar(file);
        return ResultUtils.success(upload);
    }
}
