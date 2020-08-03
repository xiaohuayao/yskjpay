package com.yishang.yspay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yishang.yspay.bean.PayRefund;
import com.yishang.yspay.bean.SysUser;

import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-28 09:11:10
 */
public interface PayRefundService extends IService<PayRefund> {
    Page<PayRefund> payRefundListByRoot(Page<PayRefund> querypage, Map<String, String> params, SysUser user);
}
