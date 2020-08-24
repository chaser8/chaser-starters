package top.chaser.framework.boot.starter.web.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chaser.framework.boot.starter.tkmybatis.service.TkServiceImpl;
import top.chaser.framework.boot.starter.web.example.mapper.MallInfoMapper;
import top.chaser.framework.boot.starter.web.example.model.MallInfo;

@Service
public class MallInfoService extends TkServiceImpl<MallInfo> {
    @Autowired
    MallInfoMapper mallInfoMapper;
    public MallInfo selectRand(){
        return mallInfoMapper.selectRand();
    }
}