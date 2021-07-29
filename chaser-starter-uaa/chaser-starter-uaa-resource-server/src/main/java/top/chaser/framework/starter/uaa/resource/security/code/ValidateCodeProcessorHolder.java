package top.chaser.framework.starter.uaa.resource.security.code;

import top.chaser.framework.uaa.base.code.AuthCodeType;
import org.springframework.beans.factory.InitializingBean;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.base.exception.SystemException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证码处理
 *
 * @author: chaser8
 * @date 2021/6/3 11:12 上午
 **/
public class ValidateCodeProcessorHolder implements InitializingBean {

    /**
     * 所有的验证码处理器
     */
    private List<ValidateCodeProcessor> allProcessors;

    private Map<AuthCodeType, ValidateCodeProcessor> processorMap;

    public ValidateCodeProcessorHolder(List<ValidateCodeProcessor> allProcessors) {
        this.allProcessors = allProcessors;
    }

    public ValidateCodeProcessor getProcessor(AuthCodeType type) {
        ValidateCodeProcessor processor = processorMap.get(type);
        if (processor == null) {
            throw new SystemException(SystemErrorType.SYSTEM_ERROR,"not support such validateCode(" + type + ")");
        }
        return processor;
    }


    @Override
    public void afterPropertiesSet() {
        processorMap = new HashMap<>(allProcessors.size());
        for (ValidateCodeProcessor processor : allProcessors) {
            processorMap.put(processor.getType(), processor);
        }
    }

    public List<ValidateCodeProcessor> getProcessors() {
        return allProcessors;
    }
}
