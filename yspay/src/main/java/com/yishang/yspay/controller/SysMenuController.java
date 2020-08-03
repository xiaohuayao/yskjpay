package com.yishang.yspay.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yishang.yspay.bean.SysMenu;
import com.yishang.yspay.service.SysMenuService;
import com.yishang.yspay.util.MenuTree;
import com.yishang.yspay.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@RestController
@Api(tags = "菜单表接口")
@RequestMapping("pay/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 保存
     */
    @ApiOperation(value = "编辑菜单", notes = "编辑菜单信息")
    @ApiImplicitParam(name = "sysMenu", value = "菜单对象", required = true, dataType = "SysMenu", paramType = "body")
    @PostMapping("/save")
    public R save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);

        return R.ok("添加成功！");
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改菜单", notes = "修改菜单信息")
    @ApiImplicitParam(name = "sysMenu", value = "菜单对象", required = true, dataType = "SysMenu", paramType = "body")
    @PostMapping("/update")
    public R update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);

        return R.ok("修改成功！");
    }


    @ApiOperation(value = "获取菜单", notes = "获取菜单")
    @PostMapping("/findMenu")
    public R findMenu() {
        List<SysMenu> list = sysMenuService.list();
        MenuTree menuTree = new MenuTree(list);
        list = menuTree.builTree();
        /*转为json看看效果*/
        String jsonOutput = JSON.toJSONString(list);
        System.out.println(jsonOutput);
        return R.ok().put("tree", list);
    }

    /**
     * 获取所有树形菜单
     *
     * @return
     */
    @PostMapping("/menuTree")
    @ResponseBody
    @ApiOperation(value = "获取所有菜单", notes = "获取所有菜单")
    public R menuTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenu::getSort);
        List<SysMenu> list = sysMenuService.list(wrapper);
        MenuTree menuTree = new MenuTree(list);
        list = menuTree.builTree();
        return R.ok().put("data", list);
    }

    /**
     * 获取所有菜单集合
     *
     * @return
     */
    @PostMapping("/selectMenuList")
    @ResponseBody
    @ApiOperation(value = "获取所有菜单集合", notes = "获取所有菜单集合")
    public R selectMenuList() {
        List<SysMenu> list = sysMenuService.list();
        return R.ok().put("data", list);
    }

    /**
     * 根据菜单ID获取详细信息
     */
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable String id)
    {
        return R.ok().put("menu",sysMenuService.getById(id));
    }

    /**
     * 根据菜单ID删除菜单
     */
    @DeleteMapping("/{id}")
    public R remove(@PathVariable("id") String id){
        sysMenuService.removeById(id);
        return R.ok();
    }
}
