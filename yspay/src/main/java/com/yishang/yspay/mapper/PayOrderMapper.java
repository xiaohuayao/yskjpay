package com.yishang.yspay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayOrder;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.bean.vo.CollectVo;
import com.yishang.yspay.bean.vo.HomePageVo;
import com.yishang.yspay.bean.vo.PayOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
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
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    /**
     * 商户流水查询
     * lqp
     *
     * @param orderPage
     * @param search
     * @return
     */
    IPage<PayOrder> streamPage(Page<PayOrder> orderPage, @Param("search") PayOrderVo search);

    /**
     * 根据查询条件求已支付订单金额总数
     * lqp
     *
     * @param search
     * @return
     */
    BigDecimal sumOrderMoney(@Param("search") PayOrderVo search);

    /**
     * 查询行社交易总汇
     *
     * @param page
     * @param params
     * @param user
     * @return
     */
    IPage<CollectVo> collectListClub(Page<CollectVo> page, Map<String, String> params, SysUser user);

    /**
     * 查询网点交易总汇
     *
     * @param page
     * @param params
     * @param user
     * @return
     */
    IPage<CollectVo> collectListBranch(Page<CollectVo> page, Map<String, String> params, SysUser user);


    /**
     * 查询商户交易总汇修改版
     *
     * @param page
     * @param params
     * @param user
     * @return
     */
    IPage<CollectVo> collectListMerchPlus(Page<CollectVo> page, Map<String, String> params, SysUser user);

    /**
     * 获取各状态下的订单数
     * lqp
     *
     * @param search
     * @return
     */
    List<CollectVo> statisticsOrder(@Param("search") PayOrderVo search);

    /**
     * 获取交易和退款汇总（交易笔数、交易金额、退款笔数、退款金额）
     * lqp
     *
     * @param user
     * @param todayStartTime
     * @param todayEndTime
     * @return
     */
    HomePageVo selectDealAndRefundCollect(@Param("user") SysUser user, @Param("startTime") Date todayStartTime, @Param("endTime") Date todayEndTime);

    /**
     * 获取商户流水导出报表的集合
     * lqp
     *
     * @param search
     * @return
     */
    List<PayOrder> selectStreamExcelList(@Param("search") PayOrderVo search);
}
