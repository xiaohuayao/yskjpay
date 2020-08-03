package com.yishang.yspay.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页
 */
@Data
public class HomePageVo {

    /**
     * 交易笔数
     */
    private Integer dealStock;

    /**
     * 交易金额
     */
    private BigDecimal dealMoney;

    /**
     * 退款笔数
     */
    private Integer refundStock;

    /**
     * 退款金额
     */
    private BigDecimal refundMoney;
}
