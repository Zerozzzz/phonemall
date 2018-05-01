package com.mmall.service.impl;

import com.mmall.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/24.
 */
public class FileServiceImplements implements IFileService {

    public String upload(MultipartFile file, String path) {
        Logger logger = LoggerFactory.getLogger(FileServiceImplements.class);
        String originFileName = file.getOriginalFilename();
        //扩展名
        String fileExtentinName = originFileName.substring(originFileName.lastIndexOf(".")+1);

        //上传文件名（不重复）
        String fileUploadName = UUID.randomUUID().toString() + fileExtentinName;
        logger.info("文件名{}，扩展名{}，新文件名{}", originFileName, fileExtentinName, fileUploadName);
        //上传目录
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,fileUploadName);
        try {
            file.transferTo(targetFile);//文件上传一成功
            // TODO: 2018/4/24 将targetFile 上传到FTP服务器

            // TODO: 2018/4/24 将upload目录下的文件删除
        } catch (IOException e) {
            logger.error("上传文件错误",e);
            return null;
        }

        return targetFile.getName();
    }

}
