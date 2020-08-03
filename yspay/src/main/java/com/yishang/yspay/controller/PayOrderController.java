package com.yishang.yspay.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayOrder;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.bean.vo.CollectVo;
import com.yishang.yspay.bean.vo.PayOrderVo;
import com.yishang.yspay.service.PayOrderService;
import com.yishang.yspay.service.SysUserService;
import com.yishang.yspay.util.ExportExcel;
import com.yishang.yspay.util.R;
import com.yishang.yspay.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@RestController
@Api(tags = "订单表表接口")
@RequestMapping("pay/order")
public class PayOrderController {
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ExportExcel exportExcel;

    /**
     * 查询时间（type=1 当天，type=2 昨天，type=3 本周，type=4 本月）
     * lqp
     *
     * @return
     */
    @PostMapping("/queryTime")
    @ApiOperation(value = "根据传入的type获取开始和结束时间", notes = "根据传入的type获取开始和结束时间")
    @ApiImplicitParam(name = "params", value = "type", required = true)
    public R queryTime(@RequestBody Map<String, String> params) {
        String type = params.get("type").trim();
        return R.ok().put("times", payOrderService.queryTime(type));
    }

    /**
     * 商户流水查询
     *
     * @return
     */
    @PostMapping("/streamList")
    @ApiOperation(value = "根据ID批量删除订单", notes = "删除订单")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String,String>", paramType = "body")
    public R streamList(@RequestBody Map<String, String> params) throws ParseException {
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }
        Page<PayOrder> orderPage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));
        PayOrderVo payOrderVo = new PayOrderVo();
        //获取当前用户
        SysUser user = sysUserService.getById(UserContext.get());
        payOrderVo.setIsRoot(user.getIsroot());
        payOrderVo.setMerchantid(user.getMerchantid());
        payOrderVo.setBranchid(user.getBranchid());
        payOrderVo.setClubid(user.getClubid());
        IPage<PayOrder> order = payOrderService.streamPage(orderPage, payOrderVo, params);
        Map<String, Object> map = payOrderService.statisticsOrderNum(payOrderVo, params);
        return R.ok(order).put("num", map);
    }

    /**
     * 商户交易汇总修改
     *
     * @return
     */
    @PostMapping("/collectListMerchPlus")
    @ApiOperation(value = "根据token获取角色ID查询对应的交易汇总", notes = "商户交易汇总")
    @ApiImplicitParam(name = "params", value = "起始日期，结束日期", required = true)
    public R collectListMerchPlus(@RequestBody Map<String, String> params) {
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }
        Page<CollectVo> collectVoPage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));
        SysUser user = sysUserService.getById(UserContext.get());
        IPage<CollectVo> collect = payOrderService.collectListMerchPlus(collectVoPage, params, user);
        return R.ok(collect).put("isroot", user.getIsroot());
    }


    /**
     * 网点交易汇总
     *
     * @return
     */
    @PostMapping("/collectListBranch")
    @ApiOperation(value = "根据token获取角色ID查询对应的交易汇总", notes = "网点交易汇总")
    @ApiImplicitParam(name = "params", value = "起始日期，结束日期", required = true)
    public R collectListBranch(@RequestBody Map<String, String> params) {
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }
        Page<CollectVo> collectVoPage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));
        SysUser user = sysUserService.getById(UserContext.get());
        IPage<CollectVo> collect = payOrderService.collectListBranch(collectVoPage, params, user);
        return R.ok(collect);
    }

    /**
     * 行社交易汇总
     *
     * @return
     */
    @PostMapping("/collectListClub")
    @ApiOperation(value = "根据token获取角色ID查询对应的交易汇总", notes = "行社交易汇总")
    @ApiImplicitParam(name = "params", value = "起始日期，结束日期", required = true)
    public R collectListClub(@RequestBody Map<String, String> params) {
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }
        Page<CollectVo> collectVoPage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));
        SysUser user = sysUserService.getById(UserContext.get());
        IPage<CollectVo> collect = payOrderService.collectListClub(collectVoPage, params, user);
        return R.ok(collect);
    }

    /**
     * 获取最近七天日期
     * lqp
     *
     * @return
     */
    @PostMapping("/latelyDate")
    @ApiOperation(value = "获取最近七天日期", notes = "获取最近七天日期")
    public R findLatelyDate() {
        List<String> list = payOrderService.findLatelyDate();
        return R.ok().put("lately", list);
    }

    /**
     * 获取最近七天汇总
     * lqp
     *
     * @return
     */
    @PostMapping("/latelyLine")
    @ApiOperation(value = "获取最近七天汇总", notes = "获取最近七天汇总")
    public R findLatelyLine() {
        List<String> list = payOrderService.findLatelyDate();
        SysUser user = sysUserService.getById(UserContext.get());
        Map<String, Object> map = payOrderService.findLatelyLine(user, list);
        return R.ok().put("lineMap", map);
    }

    /**
     * 获取首页统计数据（今日交易笔数、今日交易金额、今日新增商户数等）
     * lqp
     *
     * @return
     */
    @PostMapping("/homePageCollect")
    @ApiOperation(value = "根据token获取角色ID查询对应的汇总", notes = "今日、累计交易汇总")
    public R findHomePageCollect() {
        SysUser user = sysUserService.getById(UserContext.get());
        Map<String, Object> map = payOrderService.findHomePageCollect(user);
        return R.ok().put("collect", map);
    }

    @GetMapping("/exportOrder")
    public void exportOrderXlsx(HttpServletResponse response) throws IOException {
        Map<String, String> params = new HashMap<>();
        PayOrderVo payOrderVo = new PayOrderVo();
        //获取当前用户
//        SysUser user = sysUserService.getById(UserContext.get());
//        payOrderVo.setIsRoot(user.getIsroot());
//        payOrderVo.setMerchantid(user.getMerchantid());
//        payOrderVo.setBranchid(user.getBranchid());
//        payOrderVo.setClubid(user.getClubid());
        List<PayOrder> list = payOrderService.streamExcelList(payOrderVo, params);
        for (PayOrder po : list) {
            po.setPayStateNames(po.getPaystate().getName());
            if (po.getPaychannel() == 1) {
                po.setPayChannelName("威富通");
            } else {
                po.setPayChannelName("统一收单");
            }
        }
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //创建xlsx格式的
        //ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("orderno", "订单号");
        writer.addHeaderAlias("merchantName", "所属商户");
        writer.addHeaderAlias("authcode", "付款码");
        writer.addHeaderAlias("operauser", "操作员");
        writer.addHeaderAlias("paystateNames", "支付状态");
        writer.addHeaderAlias("paydate", "订单支付时间");
        writer.addHeaderAlias("paytype", "支付方式");
        writer.addHeaderAlias("paychannelName", "支付通道");
        writer.addHeaderAlias("refundorderno", "退款订单号");
        writer.addHeaderAlias("createtime", "订单创建时间");
        int[] arr = {25, 30, 25, 10, 10, 20, 10, 10, 30, 20};
        for (int i = 0; i < arr.length; i++) {
            writer.setColumnWidth(i, arr[i]);
        }
        // 合并单元格后的标题行，使用默认标题样式，从0开始
        writer.merge(arr.length - 1, "商户流水记录");
        //只导出有别名的字段
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        String name = "商户流水记录";
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "ISO-8859-1") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 订单报表导出
     *
     * @return
     */
    @GetMapping("/exportExcelOrder")
    @ApiOperation(value = "订单报表导出", notes = "订单报表导出")
    public void exportExcel(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        PayOrderVo payOrderVo = new PayOrderVo();
        //获取当前用户
        SysUser user = sysUserService.getById(UserContext.get());
        if (user == null) {
            return;
        }
        payOrderVo.setIsRoot(user.getIsroot());
        payOrderVo.setMerchantid(user.getMerchantid());
        payOrderVo.setBranchid(user.getBranchid());
        payOrderVo.setClubid(user.getClubid());
        List<PayOrder> list = payOrderService.streamExcelList(payOrderVo, params);
        List<String[]> alias = new ArrayList<>();
        alias.add(new String[]{"merchantName", "所属商户", "30"});
        alias.add(new String[]{"money", "付款金额", "10"});
        alias.add(new String[]{"swiffpassmerchno", "威富通商户号", "30"});
        alias.add(new String[]{"operauser", "操作员", "10"});
        alias.add(new String[]{"payStateNames", "支付状态", "10"});
        alias.add(new String[]{"paytype", "支付方式", "10"});
        alias.add(new String[]{"payChannelName", "支付通道", "10"});
        alias.add(new String[]{"paydate", "订单支付时间", "20"});
        alias.add(new String[]{"orderno", "订单号", "30"});
        alias.add(new String[]{"authcode", "付款码", "25"});
        alias.add(new String[]{"refundorderno", "退款订单号", "30"});
        alias.add(new String[]{"createtime", "订单创建时间", "20"});
        String title = "商户流水报表";
        String excelName = "商户流水表";
        exportExcel.exportExcel(alias, list, title, excelName, response);
    }
}
