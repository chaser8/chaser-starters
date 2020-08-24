package top.chaser.framework.boot.starter.web.example.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chaser.framework.boot.starter.web.example.model.MallInfo;
import top.chaser.framework.boot.starter.web.example.service.MallInfoService;
import top.chaser.framework.common.web.annotation.GetMapping;
import top.chaser.framework.common.web.annotation.PostMapping;
import top.chaser.framework.common.web.controller.BaseController;
import top.chaser.framework.common.web.response.R;

/**
 * @author yzb
 * @since 2019-04-15
 */
@Api(value = "AttrValueApi", description = "属性值规格")
@RestController
@RequestMapping("/mall")
@Slf4j
public class MallController extends BaseController {

    @Autowired
    MallInfoService mallInfoService;

    @ApiOperation(value = "新增")
    @PostMapping("add")
    public R<MallInfo> add(@Validated @RequestBody MallInfo mall){
        mallInfoService.insertSelective(mall);
        return R.success(mall);
    }

    @ApiOperation(value = "查询")
    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    public R<MallInfo> get(@PathVariable String id){
        if(id.equals("1")){
            throw new RuntimeException("21");
        }
        MallInfo mallInfo = mallInfoService.selectRand();
        return R.success(mallInfo);
    }

    @ApiOperation(value = "查询")
    @org.springframework.web.bind.annotation.GetMapping("t/{id}")
    public String get1(@PathVariable String id){
        if(id.equals("1")){
            throw new RuntimeException("21");
        }
        return "/errors/xxx.html";
    }

    @ApiOperation(value = "查询")
    @GetMapping("/viee/{id}")
    public String view(@PathVariable String id){
        return "/errors/404.html";
    }

}
