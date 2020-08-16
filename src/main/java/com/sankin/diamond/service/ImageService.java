package com.sankin.diamond.service;

import com.sankin.diamond.aliyunUtil.OSSUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    public String upDateImg(MultipartFile file) throws Exception {
        if (file == null || file.getSize() <= 0) {
            throw new Exception("file不能为空");
        }
        OSSUtil ossClient=new OSSUtil();
        String name = ossClient.uploadImg2Oss(file);
        String imgUrl = ossClient.getUploadUrl(name);
        String[] split = imgUrl.split("\\?");
        return split[0];
    }
}
