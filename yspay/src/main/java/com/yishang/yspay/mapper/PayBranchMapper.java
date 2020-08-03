package com.yishang.yspay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayBranch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface PayBranchMapper extends BaseMapper<PayBranch> {

    /**
     * 查询网点列表
     * @param page
     * @param params
     * @return
     */
    IPage<PayBranch> getList(Page<PayBranch> page, @Param("params") Map<String, String> params);
}
