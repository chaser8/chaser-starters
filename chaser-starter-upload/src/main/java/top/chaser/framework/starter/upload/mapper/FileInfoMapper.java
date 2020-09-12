package top.chaser.framework.starter.upload.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.chaser.framework.starter.tkmybatis.mapper.TkBaseMapper;
import top.chaser.framework.starter.upload.entity.FileInfo;

/**
 * (FileInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-20 15:59:25
 */
@Mapper
public interface FileInfoMapper extends TkBaseMapper<FileInfo> {
}