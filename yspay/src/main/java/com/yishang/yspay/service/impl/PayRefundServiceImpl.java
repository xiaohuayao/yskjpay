package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yishang.yspay.bean.PayRefund;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.mapper.PayRefundMapper;
import com.yishang.yspay.service.PayRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayRefundServiceImpl extends ServiceImpl<PayRefundMapper, PayRefund> implements PayRefundService {

    @Autowired
    PayRefundMapper payRefundMapper;
    @Override
    public Page<PayRefund> payRefundListByRoot(Page<PayRefund> querypage, Map<String, String> params, SysUser user) {
        return payRefundMapper.payRefundListByRoot(querypage,params,user);
    }
}
