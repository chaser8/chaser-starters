package top.chaser.framework.starter.logging.processor;


import top.chaser.framework.common.web.http.response.ResponseWrapper;
import top.chaser.framework.starter.logging.LogInfo;

import javax.servlet.http.HttpServletRequest;

public abstract class LoggingProcessor {
    public abstract void process(LogInfo log, HttpServletRequest request, ResponseWrapper response);

    public abstract int hashCode();
}
