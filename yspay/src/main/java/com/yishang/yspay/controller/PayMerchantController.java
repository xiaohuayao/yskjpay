package com.yishang.yspay.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yishang.yspay.bean.PayBranch;
import com.yishang.yspay.bean.PayClub;
import com.yishang.yspay.bean.PayMerchant;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.service.PayBranchService;
import com.yishang.yspay.service.PayClubService;
import com.yishang.yspay.service.PayMerchantService;
import com.yishang.yspay.service.SysUserService;
import com.yishang.yspay.util.R;
import com.yishang.yspay.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@RestController
@RequestMapping("pay/merchant")
@Api(tags = "商户表接口")
public class PayMerchantController {
    @Autowired
    private PayMerchantService payMerchantService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayClubService payClubService;
    @Autowired
    PayBranchService payBranchService;
    //商户文件上传路径
    @Value("${merchant.file.path}")
    private String TEMPLATEPATH;


    /**
     * 列表
     */
    @ApiOperation(value = "商户分页管理", notes = "获取商户列表")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String,String>", paramType = "body")
    @PostMapping("/list")
    public R list(@RequestBody Map<String, String> params) {
        //根据当前登录用户获取拥有的商户
        return payMerchantService.getMerchantListByRoot(params);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "根据ID获取商户", notes = "获取单个商户信息")
    @ApiImplicitParam(name = "params", value = "商户ID", required = true, dataType = "Map<String, String>")
    @PostMapping("/info")
    public R info(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }

        PayMerchant payMerchant = payMerchantService.getMerchantById(id);
        if (payMerchant == null) {
            return R.error(400, "未查询到该结果！");
        }
        SysUser user = sysUserService.getById(UserContext.get());
        return R.ok().put("payMerchant", payMerchant).put("isroot", user.getIsroot());
    }

    /**
     * 编辑
     *
     * @param payMerchant
     * @return
     */
    @ApiOperation(value = "编辑商户", notes = "编辑商户信息")
    @ApiImplicitParam(name = "payMerchant", value = "商户对象", required = true, dataType = "PayMerchant", paramType = "body")
    @PostMapping("/edit")
    public R edit(@RequestBody PayMerchant payMerchant) {
        //参数校验
        if (payMerchant.getAppkey() == null || StringUtils.isBlank(payMerchant.getAppkey())) {
            return R.error(400, "应用秘钥为空！");
        }
        if (payMerchant.getMerchname() == null || StringUtils.isBlank(payMerchant.getMerchname())) {
            return R.error(400, "商户名称为空！");
        }
        if (payMerchant.getSwiffpassmerchno() == null || StringUtils.isBlank(payMerchant.getSwiffpassmerchno())) {
            return R.error(400, "威富通商户号为空！");
        }
        if (payMerchant.getSwiffpasspaysecert() == null || StringUtils.isBlank(payMerchant.getSwiffpasspaysecert())) {
            return R.error(400, "商户秘钥为空！");
        }
        if (payMerchant.getOpenchannel() == null) {
            return R.error(400, "未选择开启通道！");
        }

        if (payMerchant.getBranchid() == null || StringUtils.isBlank(payMerchant.getBranchid())) {
            return R.error(400, "未选择所属网点！");
        }

        QueryWrapper<PayMerchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("MerchName",payMerchant.getMerchname());

        if(payMerchantService.getOne(queryWrapper) !=null){
            return R.error(400,"已存在该商户名");
        }

        if (payMerchant.getId() == null || StringUtils.isBlank(payMerchant.getId())) {
            payMerchant.setId(UUID.randomUUID() + "");

            SysUser user = sysUserService.getById(UserContext.get());
            payMerchant.setCreateuser(user.getUsername());
            payMerchant.setCreatetime(new Date());
            payMerchantService.save(payMerchant);
            return R.ok("添加成功！");
        }
        payMerchantService.updateById(payMerchant);
        return R.ok("修改成功！");
    }


    /**
     * 删除
     */
    @ApiOperation(value = "根据id批量删除银行", notes = "删除银行")
    @ApiImplicitParam(name = "params", value = "银行ids ", required = true)
    @PostMapping("/delete")
    public R delete(@RequestBody Map<String, String> params) {
        String[] ids = params.get("ids").split(",");
        //参数校验
        if (ids.length <= 0) {
            return R.error(400, "ids长度<=0");
        }
        for (String id : ids) {
            if (id == null || StringUtils.isBlank(id)) {
                return R.error(400, "id为空");
            }
        }
        payMerchantService.removeByIds(Arrays.asList(ids));
        return R.ok("删除成功！");
    }



    /**
     * 修改商户状态
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改状态", notes = "修改状态")
    @ApiImplicitParam(name = "params", value = "需传入(id-商户ID,status-状态 必传)", required = true)
    @PostMapping("/updataMerchantStatus")
    public R updataMerchantStatus(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String status = params.get("status");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        if (status == null || StringUtils.isBlank(status)) {
            return R.error(400, "状态为空！");
        }

        PayMerchant payMerchant = new PayMerchant();
        payMerchant.setId(id);
        if(status.equals("1")){
            payMerchant.setStatus(0);
        }
        if(status.equals("0")){
            payMerchant.setStatus(1);
        }
        payMerchantService.updateById(payMerchant);
        return R.ok("修改状态成功！");
    }


    /**
     * 修改商户支付通道
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改商户支付通道", notes = "修改商户支付通道")
    @ApiImplicitParam(name = "params", value = "需传入(id-商户ID,openchannel-状态 必传)", required = true)
    @PostMapping("/updataMerchantOpenChannel")
    public R updataMerchantOpenChannel(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String openchannel = params.get("openchannel");

        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        if (openchannel == null || StringUtils.isBlank(openchannel)) {
            return R.error(400, "支付通道为空！");
        }

        PayMerchant payMerchant = new PayMerchant();
        payMerchant.setId(id);
        if(openchannel.equals("1")){
            payMerchant.setOpenchannel(2);
        }
        if(openchannel.equals("2")){
            payMerchant.setOpenchannel(1);
        }
        payMerchantService.updateById(payMerchant);
        return R.ok("修改状态成功！");
    }


    /**
     * 获取当前登录用户的所属行社
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户的所属行社", notes = "获取当前登录用户的所属行社")
    @PostMapping("/clublist")
    public R clublist() {
        SysUser user = sysUserService.getById(UserContext.get());
        if(user.getIsroot().equals(1)){
            //查所有行社
            return R.ok(payClubService.list());
        }else{
            //查当前用户所属的行社
            String clubId = null;
            if(user.getIsroot().equals(2)){
                clubId = user.getClubid();
            }else if(user.getIsroot().equals(3)){
                PayBranch payBranch = payBranchService.getById(user.getBranchid());
                clubId = payBranch.getClubid();
            }else{
                PayMerchant payMerchant = payMerchantService.getById(user.getMerchantid());
                PayBranch payBranch = payBranchService.getById(payMerchant.getBranchid());
                clubId = payBranch.getClubid();
            }
            QueryWrapper<PayClub> wrapper = new QueryWrapper<>();
            wrapper.eq("id",clubId);
            return R.ok(payClubService.list(wrapper));
        }
    }


    /**
     * 获取网点集合
     *
     * @return
     */
    @ApiOperation(value = "获取网点集合", notes = "获取网点集合")
    @PostMapping("/branchlist")
    public R branchlist() {
        SysUser user = sysUserService.getById(UserContext.get());
        List<PayBranch> list = new ArrayList<>();
        if(user.getIsroot() == 1){
            return R.ok(payBranchService.list());
        }else if(user.getIsroot() == 2){
            LambdaQueryWrapper<PayBranch> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PayBranch::getClubid,user.getClubid());
            return R.ok(payBranchService.list(wrapper));
        } else if (user.getIsroot() == 3){
            list.add(payBranchService.getById(user.getBranchid()));
            return R.ok(list);
        }else{
            PayMerchant payMerchant = payMerchantService.getById(user.getMerchantid());
            list.add(payBranchService.getById(payMerchant.getBranchid()));
            return R.ok(list);
        }
    }


    @ApiOperation("上传文件")
    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public R uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return R.error("上传文件为空");
        }
        // 获取文件全名a.py
        String fileName = file.getOriginalFilename();

        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        //获取文件名
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));

        // 解决中文问题,liunx 下中文路径,图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        File dest0 = new File(TEMPLATEPATH);
        File dest = new File(dest0, prefixName + File.separator + fileName);
        //文件上传-覆盖
        try {
            // 检测是否存在目录
            if (!dest0.getParentFile().exists()) {
                dest0.getParentFile().mkdirs();
                //检测文件是否存在
            }
            if (!dest.exists()) {
                dest.mkdirs();
            }
            file.transferTo(dest);
            //文件存储路径
            String filePath = TEMPLATEPATH+prefixName+"/"+fileName;

            return R.ok("上传成功！").put("filePath",filePath);
        } catch (Exception e) {
            return R.error("上传失败");
        }

    }


}
