package top.chaser.framework.starter.uaa.authorization.controller;

import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeProcessorHolder;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.chaser.framework.common.web.annotation.PostMapping;
import top.chaser.framework.common.web.controller.BaseController;
import top.chaser.framework.common.web.response.R;
import top.chaser.framework.common.web.session.User;

/**
 * @author: chaser8
 * @date 2021/6/1 3:06 下午
 **/
@RequestMapping("/uaa")
@RestController
public class UaaController extends BaseController {

    @Autowired
    protected TokenStore tokenStore;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected ValidateCodeProcessorHolder validateCodeProcessorHolder;
    @Autowired
    protected AuthorizationServerProperties uaaProperties;


    @PostMapping(value = "/user")
    public R<User> getUser() {
        return R.success(tokenStore.getUserByUserCode(super.getCurrentUser().getUserCode()));
    }
}
