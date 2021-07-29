package top.chaser.framework.starter.web.autoconfigure;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/errors/xxx.html");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errors/xxx.html");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.html");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errors/xxx.html");
        registry.addErrorPages(error400Page,error401Page,error404Page,error500Page);
    }
}
