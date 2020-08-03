package com.yishang.yspay.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.PayClub;
import com.yishang.yspay.bean.SysUser;
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

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("pay/club")
@Api(tags = "行社表接口")
public class PayClubController {

    @Autowired
    PayClubService payClubService;
    @Autowired
    SysUserService sysUserService;

    /**
     * 列表
     */
    @ApiOperation(value = "行社分页管理", notes = "获取行社列表")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String,String>", paramType = "body")
    @PostMapping("/list")
    public R list(@RequestBody Map<String, String> params) {
        String pageNow = params.get("page");
        String pageNum = params.get("limit");
        //校验参数
        if (pageNow == null || StringUtils.isBlank(pageNow)) {
            return R.error(400, "page为空！");
        }
        if (pageNum == null || StringUtils.isBlank(pageNum)) {
            return R.error(400, "limit为空！");
        }
        Page<PayClub> page = new Page<>(Integer.parseInt(pageNow), Integer.parseInt(pageNum));
        LambdaQueryWrapper<PayClub> wrapper = new LambdaQueryWrapper<>();
        SysUser user = sysUserService.getById(UserContext.get());
        if (user.getIsroot() != 1) {
            return R.error(400, "没有权限查看！");
        }
        //查询条件
        String clubname = params.get("clubname");
        if (clubname != null && StringUtils.isNotBlank(clubname)) {
            wrapper.like(PayClub::getClubname, clubname);
        }
        String principal = params.get("principal");
        if (principal != null && StringUtils.isNotBlank(principal)) {
            wrapper.like(PayClub::getPrincipal, principal);
        }

        return R.ok(payClubService.page(page, wrapper));
    }

    /**
     * 编辑
     */
    @ApiOperation(value = "编辑行社", notes = "编辑行社信息")
    @ApiImplicitParam(name = "payClub", value = "行社对象", required = true, dataType = "PayClub")
    @PostMapping("/edit")
    public R edit(@RequestBody PayClub payClub) {
        //参数校验
        if (payClub.getClass() == null || StringUtils.isBlank(payClub.getClubname())) {
            return R.error(400, "行社名称为空！");
        }
        if (payClub.getPhone() == null || StringUtils.isBlank(payClub.getPhone())) {
            return R.error(400, "联系电话为空！");
        }
        if (payClub.getPrincipal() == null || StringUtils.isBlank(payClub.getPrincipal())) {
            return R.error(400, "负责人为空！");
        }
        QueryWrapper<PayClub> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ClubName",payClub.getClubname());

        if(payClubService.getOne(queryWrapper) !=null){
            return R.error(400,"已存在该行社名");
        }
        if (payClub.getId() == null || StringUtils.isBlank(payClub.getId())) {
            payClub.setId(UUID.randomUUID() + "");
            payClubService.save(payClub);
            return R.ok("添加成功！");
        }
        payClubService.updateById(payClub);
        return R.ok("编辑成功！");
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "根据ID批量删除行社", notes = "删除行社")
    @ApiImplicitParam(name = "params", value = "行社ids", required = true)
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

        payClubService.removeByIds(Arrays.asList(ids));
        return R.ok("删除成功！");
    }

    /**
     * 信息
     */
    @ApiOperation(value = "根据ID获取行社", notes = "获取单个行社信息")
    @ApiImplicitParam(name = "params", value = "行社ID", required = true, dataType = "Map<String, String>")
    @PostMapping("/info")
    public R info(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        PayClub payClub = payClubService.getById(id);
        if (payClub == null) {
            return R.error(400, "未查询到该结果！");
        }
        return R.ok().put("payClub", payClub);
    }
}
