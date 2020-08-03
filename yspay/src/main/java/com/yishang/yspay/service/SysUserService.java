package com.yishang.yspay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.bean.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
public interface SysUserService extends IService<SysUser> {

    SysUser myGetById(String id);

    IPage<SysUser> getList(Page<SysUser> page, @Param("params") Map<String, String> params,SysUser user);
    List<PayMerchant> roleMerch(String clubid);

    void updateNull(SysUser user);
}

