package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.mapper.PayMerchantMapper;
import com.yishang.yspay.mapper.SysUserMapper;
import com.yishang.yspay.service.PayMerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yishang.yspay.util.R;
import com.yishang.yspay.util.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayMerchantServiceImpl extends ServiceImpl<PayMerchantMapper, PayMerchant> implements PayMerchantService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    PayMerchantMapper payMerchantMapper;

    @Override
    public R getMerchantListByRoot(Map<String, String> params) {
        String pageNow = params.get("page");
        String pageNum = params.get("limit");

        //校验参数
        if (pageNow == null || StringUtils.isBlank(pageNow)) {
            return R.error(400, "page为空！");
        }
        if (pageNum == null || StringUtils.isBlank(pageNum)) {
            return R.error(400, "limit为空！");
        }

        //分页工具
        Page<PayMerchant> page = new Page<>(Integer.parseInt(pageNow), Integer.parseInt(pageNum));

        //获取当前用户
        SysUser user = sysUserMapper.selectById(UserContext.get());

        Page<PayMerchant> merchantPage = payMerchantMapper.selectMerchantListByRoot(page,params,user);

        return R.ok(merchantPage).put("isroot",user.getIsroot());
    }

    @Override
    public PayMerchant getMerchantById(String id) {

        return payMerchantMapper.getMerchantById(id);
    }
}
