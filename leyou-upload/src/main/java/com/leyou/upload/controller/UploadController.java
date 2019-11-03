package com.leyou.upload.controller;

import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * description
 *
 * @author 文攀 2019/10/21 14:04
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    /**
     * 上传图片功能
     * 图片上传功能是只要选择了图片就将图片上传到服务器上，然后返回图片的名称保存到数据库中
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){

        String url = this.uploadService.upload(file);

        if(!StringUtils.isNotBlank(url)){
            //如果url为空则上传失败
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //返回200并携带图片url路径
        return ResponseEntity.ok(url);
    }

}
