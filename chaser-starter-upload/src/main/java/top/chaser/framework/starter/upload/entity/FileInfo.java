package top.chaser.framework.starter.upload.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.chaser.framework.starter.tkmybatis.model.TkBaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * (FileInfo)实体类
 *
 * @author makejava
 * @since 2020-08-23 18:01:33
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Table(name = "FILE_INFO")
public class FileInfo extends TkBaseEntity {
    private static final long serialVersionUID = -24036227936310494L;
    /**
     * 文件标识
     */
    @Column(name = "FILE_ID")
    @Id
    private String fileId;
    /**
     * 业务类型
     */
    @Column(name = "BIZ_TYPE")
    private String bizType;
    /**
     * 业务编码
     */
    @Column(name = "BIZ_ID")
    private String bizId;
    /**
     * 文件类型（image/jpg, image/png, video/mp4, xsl,doc等)
     */
    @Column(name = "FILE_TYPE")
    private String fileType;
    /**
     * 原始文件名称
     */
    @Column(name = "FILE_NAME")
    private String fileName;
    /**
     * 文件相对路径
     */
    @Column(name = "FILE_PATH")
    private String filePath;
    /**
     * 文件大小 单位 kb
     */
    @Column(name = "FILE_SIZE")
    private Long fileSize;
    /**
     * 状态：1000有效，2000无效
     */
    @Column(name = "STATUS")
    private String status;
    /**
     * 状态时间
     */
    @Column(name = "STATUS_DATE")
    private Date statusDate;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;
    /**
     * 创建人
     */
    @Column(name = "CREATE_STAFF")
    private String createStaff;
}