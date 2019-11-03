package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.controller.UploadController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author 文攀 2019/10/21 14:05
 */
@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    //注入一个FDFS客户端
    @Autowired
    FastFileStorageClient storageClient;

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 2、保存图片（保存到FDFS文件服务器上去）
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            // 2.2、上传
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);

            logger.info("http://image.leyou.com/" + storePath.getFullPath());
            // 2.3、返回完整路径
            return "http://image.leyou.com/" + storePath.getFullPath();

            //下面是将文件保存到本地的方法
            // 2.1、生成保存目录
            /*File dir = new File("E:\\StudyResource\\fileupload");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2.2、保存图片
            //file.getOriginalFilename() 为获取图片名称，其实可以使用IP + 时间戳自动生成图片名，以保证上传图片不会重复覆盖
            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            String url = "http://image.leyou.com/" + file.getOriginalFilename();

            return url;*/
        } catch (Exception e) {
            return null;
        }
    }
}
