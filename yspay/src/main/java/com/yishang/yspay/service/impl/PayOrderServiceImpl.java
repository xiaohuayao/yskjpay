package com.yishang.yspay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.bean.PayOrder;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.bean.vo.CollectVo;
import com.yishang.yspay.bean.vo.HomePageVo;
import com.yishang.yspay.bean.vo.PayOrderVo;
import com.yishang.yspay.mapper.PayOrderMapper;
import com.yishang.yspay.service.PayMerchantService;
import com.yishang.yspay.service.PayOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    @Autowired
    PayOrderMapper payOrderMapper;
    @Autowired
    private PayMerchantService payMerchantService;

    @Override
    public IPage<PayOrder> streamPage(Page<PayOrder> orderPage, PayOrderVo search, Map<String, String> params) {
        //查询条件
        String startTimeStr = params.get("startTime");
        if (startTimeStr != null && StringUtils.isNotBlank(startTimeStr)) {
            search.setStartTime(startTimeStr);
        }
        String endTimeStr = params.get("endTime");
        if (endTimeStr != null && StringUtils.isNotBlank(endTimeStr)) {
            search.setEndTime(endTimeStr);
        }
        String paytype = params.get("paytype");
        if (paytype != null && StringUtils.isNotBlank(paytype)) {
            search.setPaytype(paytype);
        }
        String merchantName = params.get("merchantName");
        if (merchantName != null && StringUtils.isNotBlank(merchantName)) {
            search.setMerchantName(merchantName);
        }
        String swiffpassmerchno = params.get("swiffpassmerchno");
        if (swiffpassmerchno != null && StringUtils.isNotBlank(swiffpassmerchno)) {
            search.setSwiffpassmerchno(swiffpassmerchno);
        }
        String paychannel = params.get("paychannel");
        if (paychannel != null && StringUtils.isNotBlank(paychannel)) {
            search.setPaychannel(Integer.parseInt(paychannel));
        }
        String operauser = params.get("operauser");
        if (operauser != null && StringUtils.isNotBlank(operauser)) {
            search.setOperauser(operauser);
        }
        String sorts = params.get("sorts");
        if (sorts != null && StringUtils.isNotBlank(sorts)) {
            search.setSorts(Integer.parseInt(sorts));
        }
        String sortRule = params.get("sortRule");
        if (sortRule != null && StringUtils.isNotBlank(sortRule)) {
            search.setSortRule(Integer.parseInt(sortRule));
        }
        return payOrderMapper.streamPage(orderPage, search);
    }

    @Override
    public IPage<CollectVo> collectListClub(Page<CollectVo> page, Map<String, String> params, SysUser user) {
        return payOrderMapper.collectListClub(page, params, user);
    }

    @Override
    public IPage<CollectVo> collectListBranch(Page<CollectVo> page, Map<String, String> params, SysUser user) {
        return payOrderMapper.collectListBranch(page, params, user);
    }

    @Override
    public IPage<CollectVo> collectListMerchPlus(Page<CollectVo> collectVoPage, Map<String, String> params, SysUser user) {
        return payOrderMapper.collectListMerchPlus(collectVoPage, params, user);
    }

    @Override
    public List<String> findLatelyDate() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 7; i > 0; i--) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -i);
            Date time = c.getTime();
            String preDay = sdf.format(time);
            list.add(preDay);
        }
        return list;
    }

    @Override
    public Map<String, Object> findLatelyLine(SysUser user, List<String> list) {
        Map<String, Object> map = new HashMap<>();
        List<BigDecimal> dealList = new ArrayList<>();
        List<BigDecimal> timesList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String s : list) {
            String startTime = s + " 00:00:00";
            String endTime = s + " 23:59:59";
            try {
//                Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
//                Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                HomePageVo homePageVo = payOrderMapper.selectDealAndRefundCollect(user, startDate, endDate);
                System.out.println(homePageVo.toString());
                dealList.add(homePageVo.getDealMoney());
                timesList.add(homePageVo.getRefundMoney());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("expectedData", dealList);
        map.put("actualData", timesList);
        return map;
    }

    @Override
    public Map<String, String> queryTime(String type) {
        Map<String, String> map = new HashMap<>();
        //获取当天起始时间
        if ("1".equals(type)) {
            Calendar calNow = Calendar.getInstance();
            calNow.set(Calendar.HOUR_OF_DAY, 0);
            calNow.set(Calendar.SECOND, 0);
            calNow.set(Calendar.MINUTE, 0);
            calNow.set(Calendar.MILLISECOND, 0);
            String startTime = calNow.getTime().toLocaleString();
            calNow.set(Calendar.HOUR_OF_DAY, 23);
            calNow.set(Calendar.SECOND, 59);
            calNow.set(Calendar.MINUTE, 59);
            calNow.set(Calendar.MILLISECOND, 59);
            String endTime = calNow.getTime().toLocaleString();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return map;
        } else if ("2".equals(type)) {
            //获取昨天起始时间
            Calendar calYesterday = Calendar.getInstance();
            calYesterday.set(Calendar.HOUR_OF_DAY, -24);
            calYesterday.set(Calendar.SECOND, 0);
            calYesterday.set(Calendar.MINUTE, 0);
            calYesterday.set(Calendar.MILLISECOND, 0);
            String startTime = calYesterday.getTime().toLocaleString();
            calYesterday.set(Calendar.HOUR_OF_DAY, 23);
            calYesterday.set(Calendar.SECOND, 59);
            calYesterday.set(Calendar.MINUTE, 59);
            calYesterday.set(Calendar.MILLISECOND, 59);
            String endTime = calYesterday.getTime().toLocaleString();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return map;
        } else if ("3".equals(type)) {
            //获取本周
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            //获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayWeek == 1) {
                dayWeek = 8;
            }
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
            String startTime = cal.getTime().toLocaleString();

            cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.MILLISECOND, 59);
            String endTime = cal.getTime().toLocaleString();

            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return map;
        } else {
            //获取本月
            Calendar calMonth = Calendar.getInstance();
            calMonth.set(calMonth.get(Calendar.YEAR), calMonth.get(Calendar.MONDAY), calMonth.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            calMonth.set(Calendar.DAY_OF_MONTH, calMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
            String startTime = calMonth.getTime().toLocaleString();
            calMonth.set(calMonth.get(Calendar.YEAR), calMonth.get(Calendar.MONDAY), calMonth.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            calMonth.set(Calendar.DAY_OF_MONTH, calMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            calMonth.set(Calendar.HOUR_OF_DAY, 23);
            calMonth.set(Calendar.SECOND, 59);
            calMonth.set(Calendar.MINUTE, 59);
            calMonth.set(Calendar.MILLISECOND, 59);
            String endTime = calMonth.getTime().toLocaleString();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return map;
        }
    }

    @Override
    public Map<String, Object> statisticsOrderNum(PayOrderVo search, Map<String, String> params) {
        //查询条件
        String startTimeStr = params.get("startTime");
        if (startTimeStr != null && StringUtils.isNotBlank(startTimeStr)) {
            search.setStartTime(startTimeStr);
        }
        String endTimeStr = params.get("endTime");
        if (endTimeStr != null && StringUtils.isNotBlank(endTimeStr)) {
            search.setEndTime(endTimeStr);
        }
        String paytype = params.get("paytype");
        if (paytype != null && StringUtils.isNotBlank(paytype)) {
            search.setPaytype(paytype);
        }
        String merchantName = params.get("merchantName");
        if (merchantName != null && StringUtils.isNotBlank(merchantName)) {
            search.setMerchantName(merchantName);
        }
        String swiffpassmerchno = params.get("swiffpassmerchno");
        if (swiffpassmerchno != null && StringUtils.isNotBlank(swiffpassmerchno)) {
            search.setSwiffpassmerchno(swiffpassmerchno);
        }
        String paychannel = params.get("paychannel");
        if (paychannel != null && StringUtils.isNotBlank(paychannel)) {
            search.setPaychannel(Integer.parseInt(paychannel));
        }
        String operauser = params.get("operauser");
        if (operauser != null && StringUtils.isNotBlank(operauser)) {
            search.setOperauser(operauser);
        }
        Map<String, Object> map = new HashMap<>();
        //获取各个状态的订单数
        List<CollectVo> list = payOrderMapper.statisticsOrder(search);
        for (CollectVo c : list) {
            if (c.getPayState() == 0) {
                map.put("notPaid", c.getNum());
            } else if (c.getPayState() == 1) {
                map.put("havePaid", c.getNum());
            } else if (c.getPayState() == 2) {
                map.put("portionNum", c.getNum());
            } else {
                map.put("allNum", c.getNum());
            }
        }
        //交易总金额
        BigDecimal moneyNum = payOrderMapper.sumOrderMoney(search);
        map.put("moneyNum", moneyNum);
        return map;
    }

    @Override
    public Map<String, Object> findHomePageCollect(SysUser user) {
        //获取今天开始结束时间
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date todayStartTime = todayStart.getTime();
        System.out.println(todayStartTime.toString());
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        Date todayEndTime = todayEnd.getTime();
        System.out.println(todayEndTime.toString());
        //获取交易、退款汇总
        HomePageVo today = payOrderMapper.selectDealAndRefundCollect(user, todayStartTime, todayEndTime);
        HomePageVo total = payOrderMapper.selectDealAndRefundCollect(user, null, null);
        Map<String, Object> map = new HashMap<>();
        map.put("todayDealStock", today.getDealStock());
        map.put("todayDealMoney", today.getDealMoney());
        map.put("todayRefundStock", today.getRefundStock());
        map.put("todayRefundMoney", today.getRefundMoney());
        map.put("totalDealStock", total.getDealStock());
        map.put("totalDealMoney", total.getDealMoney());
        map.put("totalRefundStock", total.getRefundStock());
        map.put("totalRefundMoney", total.getRefundMoney());
        LambdaQueryWrapper<PayMerchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(PayMerchant::getCreatetime, todayStartTime);
        wrapper.le(PayMerchant::getCreatetime, todayEndTime);
        //商户数
        Integer todayMerchant = payMerchantService.count(wrapper);
        Integer totalMerchant = payMerchantService.count();
        map.put("todayMerchant", todayMerchant);
        map.put("totalMerchant", totalMerchant);
        map.put("isroot", user.getIsroot());
        return map;
    }

    @Override
    public List<PayOrder> streamExcelList(PayOrderVo search, Map<String, String> params) {
        //查询条件
        String startTimeStr = params.get("startTime");
        if (startTimeStr != null && StringUtils.isNotBlank(startTimeStr) && !"undefined".equals(startTimeStr)) {
            search.setStartTime(startTimeStr);
        }
        String endTimeStr = params.get("endTime");
        if (endTimeStr != null && StringUtils.isNotBlank(endTimeStr) && !"undefined".equals(startTimeStr)) {
            search.setEndTime(endTimeStr);
        }
        String paytype = params.get("paytype");
        if (paytype != null && StringUtils.isNotBlank(paytype)) {
            search.setPaytype(paytype);
        }
        String merchantName = params.get("merchantName");
        if (merchantName != null && StringUtils.isNotBlank(merchantName)) {
            search.setMerchantName(merchantName);
        }
        String swiffpassmerchno = params.get("swiffpassmerchno");
        if (swiffpassmerchno != null && StringUtils.isNotBlank(swiffpassmerchno)) {
            search.setSwiffpassmerchno(swiffpassmerchno);
        }
        String paychannel = params.get("paychannel");
        if (paychannel != null && StringUtils.isNotBlank(paychannel)) {
            search.setPaychannel(Integer.parseInt(paychannel));
        }
        String operauser = params.get("operauser");
        if (operauser != null && StringUtils.isNotBlank(operauser)) {
            search.setOperauser(operauser);
        }
        String sorts = params.get("sorts");
        if (sorts != null && StringUtils.isNotBlank(sorts)) {
            search.setSorts(Integer.parseInt(sorts));
        }
        String sortRule = params.get("sortRule");
        if (sortRule != null && StringUtils.isNotBlank(sortRule)) {
            search.setSortRule(Integer.parseInt(sortRule));
        }
        return payOrderMapper.selectStreamExcelList(search);
    }

}
