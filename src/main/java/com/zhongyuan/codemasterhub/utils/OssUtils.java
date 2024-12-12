package com.zhongyuan.codemasterhub.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.config.OssConfig;
import com.zhongyuan.codemasterhub.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class OssUtils {
    public static final String IMG_AVATAR = "cmh/img/avatars/";

    public static final List<String> ImageTypes = Arrays.asList("image/jpeg", "image/png");


    @Resource
    private OssConfig ossConfig;
    /**
     * 实现上传图片到OSS
     */

    public String upload(MultipartFile multipartFile,String fileType) {

        // 获取上传的文件的输入流
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = fileType +UUID.randomUUID().toString() +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        //上传文件到 OSS TODO 优化点,单例模式，不要反复创建客户端
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),ossConfig.getAccessKeySecret());
        ossClient.putObject(ossConfig.getBucketName(), fileName, inputStream);
        //文件访问路径
        String url = ossConfig.getEndpoint().split("//")[0] + "//" + ossConfig.getBucketName() + "."
                + ossConfig.getEndpoint().split("//")[1] + "/" + fileName;
        System.out.println(url);
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
