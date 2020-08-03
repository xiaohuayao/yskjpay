package com.yishang.yspay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayOrder;
import com.yishang.yspay.bean.PayRefund;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.bean.enums.PayState;
import com.yishang.yspay.service.PayOrderService;
import com.yishang.yspay.service.PayRefundService;
import com.yishang.yspay.service.SysUserService;
import com.yishang.yspay.util.R;
import com.yishang.yspay.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-28 09:11:10
 */
@RestController
@Api(tags = "订单退款表接口")
@RequestMapping("pay/refund")
public class PayRefundController {

    @Autowired
    PayOrderService payOrderService;

    @Autowired
    PayRefundService payRefundService;

    @Autowired
    SysUserService sysUserService;

//
//    /**
//     * 退款
//     * pageNow 当前是多少页
//     * pageNum 每页显示多少行
//     */
//    @ApiOperation(value = "退款操作", notes = "退款操作")
//    @ApiImplicitParam(name = "params", value = "money 金额, id 订单ID", required = true, dataType = "Map<String,String>", paramType = "body")
//    @PostMapping("/refundAmount")
//    public R refund(@RequestBody Map<String, String> params) {
//        String amount = params.get("money");
//        String id = params.get("id");
//        //校验参数
//        if (amount == null || StringUtils.isBlank(amount)) {
//            return R.error(400, "amount为空！");
//        }
//        if (id == null || StringUtils.isBlank(id)) {
//            return R.error(400, "订单id为空！");
//        }
//
//        PayOrder payOrder = payOrderService.getById(id);
//
//        BigDecimal refundAmount = new BigDecimal(amount);
//        int result = refundAmount.compareTo(payOrder.getMoney());
//        /**
//         * result = -1,表示refundAmount小于payOrder.getMoney();
//         * result = 0,表示refundAmount等于payOrder.getMoney();
//         * result = 1,表示refundAmount大于payOrder.getMoney();
//         */
//        if(result == 1){
//            return R.error("退款金额不能超过支付金额！");
//        }
//
//        PayRefund PayRefund = new PayRefund();
//
//        PayRefund.setId(UUID.randomUUID()+"");
////        String uuid = UUID.randomUUID()+"";
//        PayRefund.setRefundorderno(payOrder.getOrderno());
//        PayRefund.setRefundamount(refundAmount);
//        PayRefund.setPayorderno(payOrder.getOrderno());
//        PayRefund.setRefundtime(new Date());
//        payRefundService.save(PayRefund);
//
//        //更新订单
//        payOrder.setRefundorderno(PayRefund.getRefundorderno());
//        //更新订单状态
//        if(result == 0){
//            payOrder.setPaystate(PayState.ALL);
//        }
//        if(result == -1){
//            payOrder.setPaystate(PayState.PORTION);
//        }
//
//        payOrderService.updateById(payOrder);
//
//        return R.ok("操作成功！");
//    }

    /**
     * 订单退款列表
     * pageNow 当前是多少页
     * pageNum 每页显示多少行
     */
    @ApiOperation(value = "订单退款列表", notes = "订单退款列表")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String,String>", paramType = "body")
    @PostMapping("/refundlist")
    public R refundlist(@RequestBody Map<String, String> params){
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }

        Page<PayRefund> querypage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));

        SysUser user = sysUserService.getById(UserContext.get());

        Page<PayRefund> payRefundPage = payRefundService.payRefundListByRoot(querypage,params,user);
        return R.ok(payRefundPage);
    }
}
