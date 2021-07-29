package top.chaser.framework.starter.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Date;

@Getter
@Setter
public class LogInfo {
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 服务端IP
     */
    private String serverIp;
    /**
     * 是否正常
     */
    private boolean success;
    /**
     * 用户信息
     */
    private Object user;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date requestTime;
    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date responseTime;

    /**
     * 耗时
     */
    private long usedMilliseconds;
    /**
     * 请求地址
     */
    private String uri;
    /**
     * 请求方法
     */
    private HttpMethod method;
    /**
     * 请求地址栏后的入参
     */
    private String queryString;
    /**
     * 请求body
     */
    private String requestContent;
    /**
     * 响应body
     */
    private String responseContent;
}
