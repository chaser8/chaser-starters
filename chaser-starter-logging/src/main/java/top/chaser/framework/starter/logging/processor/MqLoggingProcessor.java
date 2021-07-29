package top.chaser.framework.starter.logging.processor;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import top.chaser.framework.common.web.http.response.ResponseWrapper;
import top.chaser.framework.starter.logging.LogInfo;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@EqualsAndHashCode
public class MqLoggingProcessor extends LoggingProcessor {

    public MqLoggingProcessor() {
    }

    @Override
    public void process(LogInfo logInfo, HttpServletRequest request, ResponseWrapper response) {
    }
}
