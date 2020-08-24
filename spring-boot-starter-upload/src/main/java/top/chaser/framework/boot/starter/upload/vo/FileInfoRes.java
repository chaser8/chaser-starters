package top.chaser.framework.boot.starter.upload.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/****
 * 
 * @program:
 * @description: 上传文件后返回
 * @author: 
 * @date 2020/8/20 2:43 下午
 **/
@Getter
@Setter
@Accessors(chain = true)
public class FileInfoRes {
    private String formName;
    private String fileId;
    private String filePath;
    private String fileType;
    private String fileName;
    private Long fileSize;
    private boolean success = true;
    private String errorInfo;
}
