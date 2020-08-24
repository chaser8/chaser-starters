package top.chaser.framework.boot.starter.upload.controller;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.chaser.framework.boot.starter.upload.autoconfigure.Storage;
import top.chaser.framework.boot.starter.upload.autoconfigure.UploadProperties;
import top.chaser.framework.boot.starter.upload.entity.FileInfo;
import top.chaser.framework.boot.starter.upload.service.FileInfoService;
import top.chaser.framework.boot.starter.upload.utils.FileStorageUtil;
import top.chaser.framework.boot.starter.upload.vo.FileInfoRes;
import top.chaser.framework.common.base.bean.Status;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.base.util.BeanUtil;
import top.chaser.framework.common.web.annotation.PostMapping;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/****
 * 文件上传下载 Controller
 * @program:
 * @description: 
 * @author: 
 * @date 2020/8/20 3:01 下午
 **/
@RestController
@Slf4j
@RequestMapping("file")
public class UploadController {
    @Autowired
    private UploadProperties uploadProperties;
    @Autowired
    private FileInfoService fileInfoService;

    @top.chaser.framework.common.web.annotation.GetMapping(value = "{fileId}")
    public R<FileInfoRes> get(@PathVariable String fileId){
        FileInfo fileInfo = fileInfoService.selectByPrimaryKey(fileId);
        return R.success(BeanUtil.toBean(fileInfo,FileInfoRes.class));
    }

    @top.chaser.framework.common.web.annotation.GetMapping(value = "display/{fileId}",produces = {})
    public void display(@PathVariable String fileId, HttpServletResponse response){
        FileInfo fileInfo = fileInfoService.selectByPrimaryKey(fileId);
        try(InputStream inputStream =  new FileInputStream(new File(fileInfo.getFilePath()));
            OutputStream outputStream = response.getOutputStream();){
            response.setContentType(fileInfo.getFileType());
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            throw new SystemException(SystemErrorType.SYSTEM_ERROR,e);
        }
    }
    @top.chaser.framework.common.web.annotation.GetMapping(value = "download/{fileId}",produces = {})
    public void download(@PathVariable String fileId, HttpServletResponse response){
        FileInfo fileInfo = fileInfoService.selectByPrimaryKey(fileId);
        try(InputStream inputStream =  new FileInputStream(new File(fileInfo.getFilePath()));
            OutputStream outputStream = response.getOutputStream();){
            response.setContentType(fileInfo.getFileType());
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileInfo.getFileName(), "UTF-8"));
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            throw new SystemException(SystemErrorType.SYSTEM_ERROR,e);
        }
    }

    /**
     *
     * 只支持每个文本域1个文件方式上传
     *
     * @return top.chaser.framework.common.web.response.R<java.util.List < top.chaser.framework.boot.starter.upload.entity.FileInfo>>
     * @author
     * @date 2020/8/20 3:01 下午
     */
    @PostMapping(value = "upload",consumes = {})
    public R<List<FileInfoRes>> upload(HttpServletRequest httpServletRequest){
        if(!(httpServletRequest instanceof MultipartHttpServletRequest)){
            return R.fail(WebErrorType.PARAM_ERROR,"please upload file");
        }
        R result = R.fail("upload failed");
        MultipartHttpServletRequest request =  (MultipartHttpServletRequest)httpServletRequest;
        Map<String, MultipartFile> fileMap = request.getFileMap();
        Iterator<String> fns=request.getFileNames();//获取上传的文件列表
        List<FileInfoRes> fileInfoResList=new ArrayList();
        while(fns.hasNext()) {
            String s = fns.next();
            List<MultipartFile> files = request.getFiles(s);
            for (MultipartFile file : files) {
                FileInfo fileInfo = null;
                if (file.isEmpty()) {
                    FileInfoRes fileInfoRes = new FileInfoRes()
                            .setSuccess(false)
                            .setErrorInfo("file is empty");
                    fileInfoResList.add(fileInfoRes);
                }else{
                    String fileId = UUID.randomUUID().toString(true);
                    Storage storageType = uploadProperties.getStorageType();
                    String storageAbsolutePath = "";
                    switch (storageType){
                        case LOCAL:
                            storageAbsolutePath = FileStorageUtil.localStorage(file,fileId,uploadProperties.getLocalStorage().getAbsolutePath());
                        default:
                            storageAbsolutePath = FileStorageUtil.localStorage(file,fileId,uploadProperties.getLocalStorage().getAbsolutePath());
                    }

                    fileInfo = new FileInfo()
                            .setCreateDate(new Date())
                            .setFileName(file.getOriginalFilename())
                            .setFilePath(storageAbsolutePath)
                            .setFileType(file.getContentType())
                            .setFileSize(file.getSize())
                            .setStatusDate(new Date())
                            .setStatus(Status.COMMON_EFFECTIVE)
                            .setFileId(fileId);
                    fileInfoService.insertSelective(fileInfo);
                    FileInfoRes fileInfoRes = BeanUtil.toBean(fileInfo, FileInfoRes.class);
                    fileInfoRes.setFormName(s);
                    fileInfoResList.add(fileInfoRes);
                }
            }
        }
        if(fileInfoResList.size()!=0){
            result = R.success(fileInfoResList);
        }
        return result;
    }
}