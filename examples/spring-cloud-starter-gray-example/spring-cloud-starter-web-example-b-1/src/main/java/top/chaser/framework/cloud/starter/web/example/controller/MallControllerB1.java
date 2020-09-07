package top.chaser.framework.cloud.starter.web.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.chaser.framework.common.web.annotation.DeleteMapping;
import top.chaser.framework.common.web.annotation.GetMapping;
import top.chaser.framework.common.web.controller.BaseController;
import top.chaser.framework.common.web.response.R;

import java.util.HashMap;

/**
 * @author yzb
 * @since 2019-04-15
 */
@Api(value = "AttrValueApi", description = "属性值规格")
@RestController
@RequestMapping("/mall")
@Slf4j
public class MallControllerB1 extends BaseController {

    @Value("${name:ttt}")
    private String name;


    @ApiOperation(value = "查询")
    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    public R get(@PathVariable String id, @RequestBody HashMap request){
        return R.success("B"+name);
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "feign")
    public R feignServer(){
        return R.success("feignServer1.1"+name);
    }
}
