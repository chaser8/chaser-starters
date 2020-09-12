package top.chaser.framework.starter.upload.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.base.exception.SystemException;

import java.util.Date;

/***
 *
 * 文件存储工具
 *
 * @program:
 * @description: 
 * @author: 
 * @date 2020/8/24 2:10 下午
 **/
public class FileStorageUtil {
    public static String localStorage(MultipartFile file, String fileId,String storageAbsolutePath){
        try {
            String newFileName = fileId+"."+ FileUtil.extName(file.getOriginalFilename());
            String storagePath = getStorageAbsolutePath(storageAbsolutePath);
            String fileStorageAbsolutePath = storagePath+newFileName;
            FileUtil.writeFromStream(file.getInputStream(),fileStorageAbsolutePath);
            return fileStorageAbsolutePath;
        } catch (Exception e) {
            throw new SystemException(SystemErrorType.SYSTEM_ERROR,e);
        }
    }

    public static String getStorageAbsolutePath(String path){
        String storageAbsolutePath = path+"/"+ DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT)+"/";
        if(!FileUtil.isDirectory(storageAbsolutePath)){
            FileUtil.mkParentDirs(storageAbsolutePath);
        }
        return storageAbsolutePath;
    }
}
