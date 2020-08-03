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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Pay_Orders")
public class PayOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.comments
     */
    @TableId
    private String id;
    /**
     * 订单号
     */
    private String orderno;
    /**
     * 付款金额
     */
    private BigDecimal money;
    /**
     * 所属商户
     */
    private String appkey;
    /**
     * 付款吗
     */
    private String authcode;
    /**
     * 操作员
     */
    private String operauser;

    /**
     * 支付状态，0未支付，1已支付,2部分退款，3全额退款
     */
    @EnumValue
    private PayState paystate;

    public String getPaystateName() {
        if (paystate != null) {
            return paystate.getName();
        }
        return null;
    }

    @TableField(exist = false)
    private String payStateNames;
//	/**
//	 * 支付状态，0未支付，1已支付
//	 */
//	private Integer paystate;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paydate;
    /**
     * 支付方式，如：微信、支付宝、银联
     */
    private String paytype;
    /**
     * 当前订单支付通道，1=威富通，2=统一收单
     */
    private Integer paychannel;

    private String payChannelName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    /**
     * 所属商户
     */
    @TableField(exist = false)
    private String merchantName;

    /**
     * 威富通商户号
     */
    @TableField(exist = false)
    private String swiffpassmerchno;

    /**
     * 退款订单号
     */
    private String refundorderno;
}
