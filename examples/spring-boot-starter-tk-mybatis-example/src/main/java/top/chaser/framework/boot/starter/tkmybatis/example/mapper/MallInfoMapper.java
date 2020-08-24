package top.chaser.framework.boot.starter.tkmybatis.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.chaser.framework.boot.starter.tkmybatis.example.model.MallInfo;
import top.chaser.framework.boot.starter.tkmybatis.mapper.TkBaseMapper;

@Mapper
public interface MallInfoMapper extends TkBaseMapper<MallInfo> {
}