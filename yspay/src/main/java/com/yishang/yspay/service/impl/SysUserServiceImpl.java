package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.mapper.SysUserMapper;
import com.yishang.yspay.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    SysUserMapper SysUserMapper;

    @Override
    public SysUser myGetById(String id) {

        return SysUserMapper.myGetById(id);
    }

    @Override
    public IPage<SysUser> getList(Page<SysUser> page, Map<String, String> params,SysUser user) {
        return SysUserMapper.getList(page,params,user);
    }

    @Override
    public List<PayMerchant> roleMerch(String clubid) {
        return SysUserMapper.roleMerch(clubid);
    }

    @Override
    public void updateNull(SysUser user) {
        UpdateWrapper wrapper = new UpdateWrapper<>();
        wrapper.set("ClubId",null);
        wrapper.set("BranchId",null);
        wrapper.set("MerchantId",null);
        wrapper.eq("ID",user.getId());
        this.update(wrapper);
    }
}
