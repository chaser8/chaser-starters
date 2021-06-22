package top.chaser.framework.starter.web.autoconfigure;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import top.chaser.framework.common.base.exception.BusiException;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.response.R;

@Slf4j
@ConditionalOnClass(javax.servlet.Servlet.class)
@ConditionalOnMissingBean(DefaultGlobalExceptionHandlerAdvice.class)
@Order
@ControllerAdvice
public class DefaultGlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public R httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return R.fail(WebErrorType.PARAM_ERROR,ex.getLocalizedMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public R missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage());
        return R.fail(WebErrorType.PARAM_ERROR);
    }

    @ExceptionHandler(value = {MultipartException.class})
    public R uploadFileLimitException(MultipartException ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return R.fail(WebErrorType.UPLOAD_FILE_SIZE_LIMIT);
    }

    /**
     *
     * errorHandler
     *
     * @param ex
     * @return top.chaser.framework.common.web.response.R
     * @author
     * @date 2021/3/30 9:47 上午
     */

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R errorHandler(HttpMessageNotReadableException ex) {
        log.error("", ex);
        String message = ex.getMessage();
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
            message = "please check the parameters：" + invalidFormatException.getPathReference() + ",value：" + invalidFormatException.getValue();
        }
        return R.fail(WebErrorType.PARAM_ERROR, message);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R serviceException(MethodArgumentNotValidException ex) {
        log.error("service exception:{}", ex.getMessage());
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return R.fail(WebErrorType.PARAM_ERROR, fieldError.getField()+":"+fieldError.getDefaultMessage());
    }
    @ExceptionHandler(value = {DuplicateKeyException.class})
    public R duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return R.fail(WebErrorType.DUPLICATE_PRIMARY_KEY);
    }
    @ExceptionHandler(value = {BusiException.class})
    public R busiException(BusiException ex) {
        log.error("busi exception", ex);
        return R.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {SystemException.class})
    public R systemException(SystemException ex) {
        log.error("system exception", ex);
        return R.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {Exception.class, Throwable.class})
    public R exception(Throwable ex) throws Throwable {
        //不处理ServletException异常，在BasicErrorController中处理
        log.error("unknown exception", ex);
        return R.fail();
    }
}
