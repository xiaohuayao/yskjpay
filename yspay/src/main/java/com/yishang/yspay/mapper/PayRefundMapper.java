package com.yishang.yspay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayRefund;
import com.yishang.yspay.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-28 09:11:10
 */
@Mapper
public interface PayRefundMapper extends BaseMapper<PayRefund> {
    Page<PayRefund> payRefundListByRoot(Page<PayRefund> querypage, Map<String, String> params, SysUser user);
}
