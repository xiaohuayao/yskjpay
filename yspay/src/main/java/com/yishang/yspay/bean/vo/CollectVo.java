package com.yishang.yspay.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易汇总
 */
@Data
public class CollectVo {

    /**
     * 订单日期
     */
    private String orderDate;

    /**
     * 总金额
     */
    private BigDecimal money;

    /**
     * 订单数
     */
    private String orderCount;

    /**
     * 商户名称
     */
    private String merchname;

    /**
     * 网点名称
     */
    private String branchname;

    /**
     * 行社名称
     */
    private String clubname;

    /**
     * 退款金额
     */
    private BigDecimal refundMoney;


    /**
     * 退款订单数
     */
    private String refundCount;

    /**
     * 威富通账号
     */
    private String swiffpassmerchno;

    /**
     * 威富通账号总金额
     */
    private BigDecimal swiffMoney;

    /**
     * 威富通账号订单总数
     */
    private String swiffCount;

    /**
     * 威富通账号退款金额
     */
    private BigDecimal swiffRefundMoney;

    /**
     * 威富通账号退款笔数
     */
    private String swiffRefundCount;

    /**
     * 威富通账号实际金额
     */
    private BigDecimal swiffMoneyTrue;


    /**
     * 统一收单账号
     */
    private String merid;

    /**
     * 统一收单账号总金额
     */
    private BigDecimal merMoney;

    /**
     * 统一收单账号订单总数
     */
    private String merCount;

    /**
     * 统一收单账号退款金额
     */
    private BigDecimal merRefundMoney;

    /**
     * 统一收单账号退款笔数
     */
    private String merRefundCount;

    /**
     * 统一收单账号实际金额
     */
    private BigDecimal merMoneyTrue;



    /**
     * 当前订单支付通道，1=威富通，2=统一收单
     */
    private Integer paychannel;

    /**
     * 商户ID
     */
    private String pmId;

    /**
     * 实际金额
     */
    private BigDecimal moneyTrue;
    /**
     * 支付状态
     */
    private Integer payState;

    /**
     * 汇总
     */
    private Integer num;

}
