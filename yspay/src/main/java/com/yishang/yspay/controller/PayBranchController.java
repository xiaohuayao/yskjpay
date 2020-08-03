package com.yishang.yspay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayBranch;
import com.yishang.yspay.bean.PayClub;
import com.yishang.yspay.bean.SysUser;
import com.yishang.yspay.service.PayBranchService;
import com.yishang.yspay.service.PayClubService;
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


import java.util.*;

@RestController
@RequestMapping("pay/branch")
@Api(tags = "网点表接口")
public class PayBranchController {

    @Autowired
    private PayBranchService payBranchService;
    @Autowired
    private PayClubService payClubService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 列表
     */
    @ApiOperation(value = "网点分页管理",notes = "获取网点列表")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String,String>", paramType = "body")
    @PostMapping("/list")
    public R list(@RequestBody Map<String, String> params){
        String pageNow = params.get("page");
        String pageNum = params.get("limit");
        //校验参数
        if (pageNow == null || StringUtils.isBlank(pageNow)) {
            return R.error(400, "page为空！");
        }
        if (pageNum == null || StringUtils.isBlank(pageNum)) {
            return R.error(400, "limit为空！");
        }
        //获取当前用户
        SysUser user = sysUserService.getById(UserContext.get());
        Page<PayBranch> page = new Page<>(Integer.parseInt(pageNow), Integer.parseInt(pageNum));
        if(user.getIsroot() == 1){
            List<PayClub> list = payClubService.list();
            return R.ok(payBranchService.getList(page,params)).put("clubList",list);
        }else if(user.getIsroot() == 2){
            List<PayClub> list = new ArrayList<>();
            list.add(payClubService.getById(user.getClubid()));
            params.put("clubId",user.getClubid());
            return R.ok(payBranchService.getList(page,params)).put("clubList",list);
        }
        return R.error(400,"权限不足，无法查看!");
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "编辑网点", notes = "编辑网点信息")
    @ApiImplicitParam(name = "payBranch", value = "网点对象", required = true, dataType = "PayBranch")
    @PostMapping("/edit")
    public R edit(@RequestBody PayBranch payBranch){

        //参数校验
        if (payBranch.getBranchname() == null || StringUtils.isBlank(payBranch.getBranchname())) {
            return R.error(400, "网点名称为空！");
        }
        if (payBranch.getPhone() == null|| StringUtils.isBlank(payBranch.getPhone())) {
            return R.error(400, "联系电话为空！");
        }
        if (payBranch.getPrincipal() == null|| StringUtils.isBlank(payBranch.getPrincipal())) {
            return R.error(400, "负责人为空！");
        }
        QueryWrapper<PayBranch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BranchName",payBranch.getBranchname());

        if(payBranchService.getOne(queryWrapper) !=null){
            return R.error(400,"已存在该网点名");
        }
        if(payBranch.getId() ==null || StringUtils.isBlank(payBranch.getId())){
            payBranch.setId(UUID.randomUUID() + "");
            payBranchService.save(payBranch);
            return R.ok("添加成功！");
        }

        payBranchService.updateById(payBranch);
        return R.ok("编辑成功！");
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "根据ID批量删除网点", notes = "删除网点")
    @ApiImplicitParam(name = "params", value = "网点ids", required = true)
    public R delete(@RequestBody Map<String,String> params){

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

        payBranchService.removeByIds(Arrays.asList(ids));
        return R.ok("删除成功！");
    }

    /**
     * 信息
     */
    @ApiOperation(value = "根据ID获取网点", notes = "获取单个网点信息")
    @ApiImplicitParam(name = "params", value = "网点ID", required = true, dataType = "Map<String, String>")
    @PostMapping("/info")
    public R info(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        PayBranch payBranch = payBranchService.getById(id);
        if (payBranch == null) {
            return R.error(400, "未查询到该结果！");
        }
        return R.ok().put("payBranch", payBranch);
    }
}
