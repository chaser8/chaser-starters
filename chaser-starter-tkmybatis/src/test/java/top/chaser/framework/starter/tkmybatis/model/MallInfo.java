package top.chaser.framework.starter.tkmybatis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺信息
 */
@Getter
@Setter
@ToString
@Table(name = "MALL_INFO")
public class MallInfo extends TkBaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * MALL_ID
     */
    @Id
    @Column(name = "MALL_ID")
    @GeneratedValue(generator = "JDBC")
    private Integer mallId;

    /**
     * 代理商标识
     */
    @Column(name = "AGENT_ID")
    private String agentId;

    /**
     * 电商平台
     */
    @Column(name = "MALL_TYPE")
    private String mallType;

    /**
     * 店铺编号
     */
    @Column(name = "MALL_NBR")
    private String mallNbr;

    /**
     * 店铺名称
     */
    @Column(name = "MALL_NAME")
    private String mallName;
}