package com.yishang.yspay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yishang.yspay.bean.PayOrder;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.bean.vo.CollectVo;
import com.yishang.yspay.bean.vo.PayOrderVo;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
public interface PayOrderService extends IService<PayOrder> {

    /**
     * 商户流水查询
     * lqp
     *
     * @return
     */
    IPage<PayOrder> streamPage(Page<PayOrder> orderPage, PayOrderVo search, Map<String, String> params);

    /**
     * 查询行社交易总汇
     *
     * @return
     */
    IPage<CollectVo> collectListClub(Page<CollectVo> collectVoPage, Map<String, String> params, SysUser user);

    /**
     * 查询网点交易总汇
     *
     * @return
     */
    IPage<CollectVo> collectListBranch(Page<CollectVo> collectVoPage, Map<String, String> params, SysUser user);


    /**
     * 查询商户交易总汇
     *
     * @return
     */
    IPage<CollectVo> collectListMerchPlus(Page<CollectVo> collectVoPage, Map<String, String> params, SysUser user);

    /**
     * 获取最近七天日期
     * lqp
     *
     * @return
     */
    List<String> findLatelyDate();

    /**
     * 获取最近七天汇总
     * lqp
     *
     * @param user
     * @param list
     * @return
     */
    Map<String, Object> findLatelyLine(SysUser user, List<String> list);

    /**
     * 查询时间（type=1 当天，type=2 昨天，type=3 本周，type=4 本月）
     * lqp
     *
     * @param type
     * @return
     */
    Map<String, String> queryTime(String type);


    /**
     * 统计订单
     * lqp
     *
     * @param search
     * @return
     */
    Map<String, Object> statisticsOrderNum(PayOrderVo search, Map<String, String> params);

    /**
     * 获取首页统计数据（今日交易笔数、今日交易金额、今日新增商户数等）
     * lqp
     *
     * @param user
     * @return
     */
    Map<String, Object> findHomePageCollect(SysUser user);

    /**
     * 获取商户流水导出报表的集合
     * lqp
     *
     * @param payOrderVo
     * @param params
     * @return
     */
    List<PayOrder> streamExcelList(PayOrderVo payOrderVo, Map<String, String> params);
}

