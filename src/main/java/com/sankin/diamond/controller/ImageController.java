package com.sankin.diamond.controller;

import com.sankin.diamond.DTO.FileDTO;
import com.sankin.diamond.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/addImage", method = RequestMethod.POST)
    public FileDTO addImage(@RequestParam(name = "imageData", required = false) MultipartFile file) {
        //文件上传
        if (!file.isEmpty()) {
            try {
                String url = imageService.upDateImg(file);
                FileDTO fileDTO = new FileDTO();
                fileDTO.setSuccess(1);
                fileDTO.setUrl(url);
                return fileDTO;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(0);
        return fileDTO;
    }
}
