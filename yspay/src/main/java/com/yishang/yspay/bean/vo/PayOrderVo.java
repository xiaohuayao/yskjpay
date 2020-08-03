package com.yishang.yspay.bean.vo;

import lombok.Data;

@Data
public class PayOrderVo {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 1管理员，2行社，3网点，4商户
     */
    private Integer isRoot;

    /**
     * 所属行社ID
     */
    private String clubid;

    /**
     * 所属网点ID
     */
    private String branchid;

    /**
     * 所属商户ID
     */
    private String merchantid;

    /**
     * 支付方式
     */
    private String paytype;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 威富通商户号
     */
    private String swiffpassmerchno;

    /**
     * 开启通道，1=威富通，2=统一收单
     */
    private Integer paychannel;

    /**
     * 操作员
     */
    private String operauser;

    /**
     * 判断排序（1=升序,2=降序）
     */
    private Integer sorts;

    /**
     * 排序规则（1=支付时间，2=创建时间）
     */
    private Integer sortRule;
}
