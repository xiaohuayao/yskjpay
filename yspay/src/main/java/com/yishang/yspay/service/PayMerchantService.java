package com.yishang.yspay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.util.R;

import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
public interface PayMerchantService extends IService<PayMerchant> {

    /**
     * 查询商户信息
     * @param id
     * @return
     */
    PayMerchant getMerchantById(String id);


    /**
     * 根据当前登录用户查询所拥有的商户
     * @param params
     * @return
     */
    R getMerchantListByRoot(Map<String, String> params);
}

