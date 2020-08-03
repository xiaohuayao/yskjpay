package com.yishang.yspay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yishang.yspay.bean.PayBranch;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PayBranchService extends IService<PayBranch> {

    /**
     * 查询网点列表
     * @param page
     * @param params
     * @return
     */
    IPage<PayBranch> getList(Page<PayBranch> page,Map<String, String> params);
}
