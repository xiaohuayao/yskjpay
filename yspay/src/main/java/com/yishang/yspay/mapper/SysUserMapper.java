package com.yishang.yspay.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayMerchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yishang.yspay.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser myGetById(String id);
    /**
     * 查询用户列表
     * @param page
     * @param params
     * @return
     */
    IPage<SysUser> getList(Page<SysUser> page, @Param("params") Map<String, String> params,SysUser user);

    List<PayMerchant> roleMerch(String clubid);

}
