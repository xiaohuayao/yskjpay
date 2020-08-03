package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yishang.yspay.bean.enums.PayState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退款表
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-28 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Pay_Refund")
public class PayRefund {

    /**
     * $column.comments
     */
    @TableId
    private String id;
    /**
     * 退款订单号
     */
    private String refundorderno;
    /**
     * 退款金额
     */
    private BigDecimal refundamount;
    /**
     * 原交易订单号
     */
    private String payorderno;
    /**
     * 退款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date refundtime;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 所属商户
     */
    @TableField(exist = false)
    private String merchantName;

    /**
     * 付款金额
     */
    @TableField(exist = false)
    private BigDecimal money;

    /**
     * 支付状态，0未支付，1已支付,2部分退款，3全额退款
     */
    @TableField(exist = false)
    @EnumValue
    private PayState paystate;

    public String getPaystateName() {
        if(paystate!=null){
            return paystate.getName();
        }
        return null;
    }
}
