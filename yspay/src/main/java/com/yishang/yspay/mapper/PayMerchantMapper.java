package com.yishang.yspay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayMerchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yishang.yspay.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Mapper
public interface PayMerchantMapper extends BaseMapper<PayMerchant> {

    /**
     * 查询商户信息
     * @param id
     * @return
     */
    PayMerchant getMerchantById(String id);

    /**
     * 根据当前登录用户获取当前商户列表
     * @param page
     * @param params
     * @param user
     * @return
     */

    Page<PayMerchant> selectMerchantListByRoot(Page<PayMerchant> page,@Param("params") Map<String, String> params, SysUser user);
}
