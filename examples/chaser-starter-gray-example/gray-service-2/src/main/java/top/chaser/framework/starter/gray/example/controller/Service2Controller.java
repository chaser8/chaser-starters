package top.chaser.framework.starter.gray.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.chaser.framework.common.base.util.JSONUtil;

import java.util.HashMap;

/**
 * @author yzb
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class Service2Controller {

    @GetMapping("/{id}")
    public String get(@PathVariable String id, @RequestBody HashMap request){
        log.debug(JSONUtil.toPrettyString(request));
        return "Service2";
    }
}
