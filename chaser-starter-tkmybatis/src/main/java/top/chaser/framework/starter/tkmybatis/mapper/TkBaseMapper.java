package top.chaser.framework.starter.tkmybatis.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @program:
 * @description:
 * @create: 2019-08-08 10:13
 **/
public interface TkBaseMapper<T> extends Mapper<T>, InsertListMapper<T> {

}
