package top.chaser.framework.boot.starter.web.autoconfigure;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import top.chaser.framework.common.base.exception.BusiException;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.response.R;

@Slf4j
@ConditionalOnClass(javax.servlet.Servlet.class)
@ConditionalOnMissingBean(DefaultGlobalExceptionHandlerAdvice.class)
@Order
@RestControllerAdvice
public class DefaultGlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public R httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return R.fail(WebErrorType.PARAM_ERROR,ex.getLocalizedMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public R missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage());
        return R.fail(WebErrorType.PARAM_ERROR);
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseBody
    public R uploadFileLimitException(MultipartException ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return R.fail(WebErrorType.UPLOAD_FILE_SIZE_LIMIT);
    }

    /**
     * errorHandler 前台传参json转换错误
     *
     * @param ex
     * @return com.fishingtime.dev1.common.base.api.Response
     * @throws
     * @Description:
     * @author
     * @date 2019/4/10 08:55
     */
    @ResponseBody
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
    @ResponseBody
    public R serviceException(MethodArgumentNotValidException ex) {
        log.error("service exception:{}", ex.getMessage());
        return R.fail(WebErrorType.PARAM_ERROR, ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    @ResponseBody
    public R duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return R.fail(WebErrorType.DUPLICATE_PRIMARY_KEY);
    }

    @ExceptionHandler(value = {BusiException.class})
    @ResponseBody
    public R busiException(BusiException ex) {
        log.error("busi exception", ex);
        return R.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {SystemException.class})
    @ResponseBody
    public R systemException(SystemException ex) {
        log.error("system exception", ex);
        return R.fail(ex.getErrorType());
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class, Throwable.class})
    public R exception(Throwable ex) {
        log.error("ResponseBody", ex);
        return R.fail();
    }
}