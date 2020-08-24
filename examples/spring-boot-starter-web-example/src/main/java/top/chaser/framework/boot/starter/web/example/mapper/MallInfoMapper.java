package top.chaser.framework.boot.starter.web.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.chaser.framework.boot.starter.tkmybatis.mapper.TkBaseMapper;
import top.chaser.framework.boot.starter.web.example.model.MallInfo;

@Mapper
public interface MallInfoMapper extends TkBaseMapper<MallInfo> {
    public MallInfo selectRand();
}