package com.yishang.yspay.bean.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author lqp
 * 订单支付状态
 * @since 2020/6/28
 **/
public enum PayState implements IEnum<Integer> {
    NO(0, "未支付"),
    HAVE(1, "已支付"),
    PORTION(2, "部分退款"),
    ALL(3, "全额退款");

    PayState(final Integer value, final String name) {
        this.name = name;
        this.value = value;
    }

    private Integer value;
    private String name;

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
