package top.chaser.framework.starter.upload.vo;

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
    /**
     * 表单文本域名
     */
    private String formName;
    /**
     * 文件标识
     */
    private String fileId;
    /**
     * 文件存储路径
     */
    private String filePath;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 上传时的文件名
     */
    private String fileName;
    /**
     * 文件大小kb
     */
    private Long fileSize;
    private boolean success = true;
    private String errorInfo;
}
