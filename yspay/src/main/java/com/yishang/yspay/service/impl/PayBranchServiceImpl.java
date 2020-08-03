package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yishang.yspay.bean.PayBranch;
import com.yishang.yspay.mapper.PayBranchMapper;
import com.yishang.yspay.service.PayBranchService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayBranchServiceImpl extends ServiceImpl<PayBranchMapper, PayBranch> implements PayBranchService {

    @Autowired
    PayBranchMapper payBranchMapper;

    @Override
    public IPage<PayBranch> getList(Page<PayBranch> page,@Param("params") Map<String, String> params) {
        return payBranchMapper.getList(page,params);
    }
}
