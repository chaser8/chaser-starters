package top.chaser.framework.starter.web.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.chaser.framework.starter.tkmybatis.mapper.TkBaseMapper;
import top.chaser.framework.starter.web.example.model.MallInfo;

@Mapper
public interface MallInfoMapper extends TkBaseMapper<MallInfo> {
    public MallInfo selectRand();
}