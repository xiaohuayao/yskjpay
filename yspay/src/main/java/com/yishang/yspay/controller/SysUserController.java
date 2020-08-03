package com.yishang.yspay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yishang.yspay.bean.*;
import com.yishang.yspay.service.*;
import com.yishang.yspay.util.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;


/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@RestController
@RequestMapping("pay/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private PayBranchService payBranchService;
    @Autowired
    private PayClubService payClubService;
    @Autowired
    private PayMerchantService payMerchantService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    SysRolemenumappingService sysRolemenumappingService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${user.init.password}")
    private String INITUSERPASSWORD;

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/getcode")
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VerificationCode verificationCode = new VerificationCode();
        //获取验证码图片
        BufferedImage image = verificationCode.getImage();
        //获取验证码内容
        String text = verificationCode.getText();
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        randomCode.append(text);
        //将验证码保存到缓存中
        Jedis jedis = redisUtil.getJedis();
        //并设置过期时间
        //获取ip
        String currentIp = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if (StringUtils.isBlank(currentIp)) {
            currentIp = request.getRemoteAddr();// 从request中获取ip
            if (StringUtils.isBlank(currentIp)) {
                currentIp = "127.0.0.1";
            }
        }
        jedis.setex("ysPayCode:" + currentIp, 3 * 60, randomCode.toString());

        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(image, "jpeg", sos);
        sos.flush();
        sos.close();
    }


    /**
     * 登录
     *
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "登录方法", notes = "登陆成功返回token")
    @ApiImplicitParam(name = "params", value = "条件查询对象(userName,password)", required = true, dataType = "Map<String,String>", paramType = "body")
    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params, HttpServletRequest request) {

        String username = params.get("userName");
        String password = params.get("password");
        String code = params.get("code");
        //参数验证
        if (username == null || StringUtils.isBlank(username)) {
            return R.error(400, "用户名为空！");
        }
        if (password == null || StringUtils.isBlank(password)) {
            return R.error(400, "密码为空！");
        }
        if (code == null || StringUtils.isBlank(code)) {
            return R.error(400, "验证码为空！");
        }

        //获取ip
        String currentIp = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if (StringUtils.isBlank(currentIp)) {
            currentIp = request.getRemoteAddr();// 从request中获取ip
            if (StringUtils.isBlank(currentIp)) {
                currentIp = "127.0.0.1";
            }
        }
        Jedis jedis = redisUtil.getJedis();
        String getcode = jedis.get("ysPayCode:" + currentIp);

        if (getcode == null) {
            return R.error("验证码已过期,请刷新验证码！");
        }

        if (!code.equalsIgnoreCase(getcode)) {
            return R.error("验证码错误,登陆失败！");
        }


        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.eq("UserName", username);
        SysUser user = sysUserService.getOne(wrapper);

        //验证账号是否存在
        if (user == null) {
            return R.error(400, "登录失败，账号不存在！");
        }

        //验证用户是否为后台禁用
        if (user.getState() == 0 && user.getDefeatedtime() == null) {
            return R.error(400, "该账户已禁用,登录失败！");
        }

        //如果存在最后一次登录失败时间
        if (user.getDefeatedtime() != null) {
            user.getDefeatedtime().setTime(user.getDefeatedtime().getTime() + 30 * 60 * 1000);
            Date defeatedTime = user.getDefeatedtime();
            Date date = new Date();
            int res = date.compareTo(defeatedTime);
            if (res == -1) {
                Long dates = defeatedTime.getTime() - date.getTime() + 60 * 1000;
                Date date1 = new Date();
                date1.setTime(dates);
                return R.error(400, "该账号已被锁定，请" + date1.getMinutes() + "分钟后再尝试");
            } else {
                user.setLogincount(0);
                user.setState(1);
                user.setDefeatedtime(null);
                UpdateWrapper<SysUser> up = new UpdateWrapper<>();
                up.set("DefeatedTime", null);
                up.eq("id", user.getId());
                sysUserService.update(user, up);
            }
        }
        if (!DigestUtils.md5Hex(password).equals(user.getPassword()) && user.getLogincount() < 5) {
            //登陆失败 用户登陆失败次数加1
            user.setLogincount(user.getLogincount() + 1);
            if (user.getLogincount() >= 5) {
                user.setLogincount(5);
                user.setDefeatedtime(new Date());
                user.setState(0);
            }
            sysUserService.updateById(user);
            if (user.getLogincount() >= 5) {
                return R.error(400, "该账号已被锁定，请稍后再试！");
            }
            return R.error(400, "登录失败，密码错误,您还有" + (5 - user.getLogincount()) + "次登录机会！");
        }

        String token = null;
        //登陆成功
        // 用jwt制作token
        token = JwtTokenUtils.createToken(user.getId(), false);
        //登录成功 重置用户登录失败次数
        user.setLogincount(0);
        sysUserService.updateById(user);
        return R.ok(token);
    }


    /**
     * 信息
     *
     * @return
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    @ApiOperation(value = "根据token获取用户", notes = "获取单个用户信息")
    public R getUserInfo() {
        SysUser user = sysUserService.myGetById(UserContext.get());

        //根据roleid将rolemenu中间表中menuid取出来
        List<String> menuids = new ArrayList<>();
        QueryWrapper<SysRolemenumapping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RoleId",user.getRoleid());
        List<SysRolemenumapping> roleMenuList = sysRolemenumappingService.list(queryWrapper);
        for (SysRolemenumapping sysRolemenumapping : roleMenuList) {
            menuids.add(sysRolemenumapping.getMenuid());
        }

        //根据menuid取出菜单并排序
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("Sort");
        wrapper.in("Id", menuids);
        List<SysMenu> list = sysMenuService.list(wrapper);

        MenuTree menuTree = new MenuTree(list);
        list = menuTree.builTree();

        if (user == null) {
            return R.error(400, "未查询到该结果！");
        }
        return R.ok().put("user", user).put("menuList", list);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "分页获取用户", notes = "获取用户列表")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(page-当前页,limit-每页条数 必传)", required = true, dataType = "Map<String, String>", paramType = "body")
    @PostMapping("/list")
    public R list(@RequestBody Map<String, String> params) {
        SysUser user = sysUserService.getById(UserContext.get());
        String page = params.get("page");
        String limit = params.get("limit");
        //校验参数
        if (page == null || StringUtils.isBlank(page)) {
            return R.error(400, "page为空！");
        }
        if (limit == null || StringUtils.isBlank(limit)) {
            return R.error(400, "limit为空！");
        }
        if (user.getIsroot() == 4) {
            return R.error(400, "权限不足，无法查看！");
        }
        Page<SysUser> userPage = new Page<>(Integer.parseInt(page), Integer.parseInt(limit));
        IPage<SysUser> sysUserIPage = sysUserService.getList(userPage, params, user);
        QueryWrapper<SysRole> wrapper = new QueryWrapper();
        List<SysRole> list = sysRoleService.list();
        if (user.getIsroot() == 2) {
            wrapper.eq("rolename","商户管理员").or().eq("rolename","网点管理员").or().eq("rolename","行社管理员");
            list = sysRoleService.list(wrapper);
        }
        if (user.getIsroot() == 3) {
            wrapper.eq("rolename","商户管理员").or().eq("rolename","网点管理员");
            list = sysRoleService.list(wrapper);
        }
        return R.ok(sysUserIPage).put("roleList", list).put("isroot", user.getIsroot());

    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @ApiOperation(value = "编辑用户", notes = "编辑用户信息")
    @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "SysUser", paramType = "body")
    @PostMapping("/edit")
    public R edit(@RequestBody SysUser user) {
        if (user.getUsername() == null || StringUtils.isBlank(user.getUsername())) {
            return R.error(400,"用户名为空!");
        }
        if (user.getIsroot() == null || StringUtils.isBlank(user.getIsroot() + "")) {
            return R.error(400,"角色为空!");
        }
        if ((user.getRootid() == null || StringUtils.isBlank(user.getRootid())) && user.getIsroot() != 1) {
            return R.error(400,"用户所属为空!");
        }
        if (user.getState() == null || StringUtils.isBlank(user.getState() + "")) {
            return R.error(400,"用户状态为空!");
        }
        if (user.getRoleid() == null || StringUtils.isBlank(user.getRoleid() + "")) {
            return R.error(400,"用户账号等级为空!");
        }
        if(user.getId().equals(UserContext.get())){
            return R.error(400,"您编辑的用户是当前登录用户，无法对其操作");
        }
        if (user.getId() == null || StringUtils.isBlank(user.getId())) {
            //新增用户
            QueryWrapper<SysUser> wrapper = new QueryWrapper();
            wrapper.eq("UserName", user.getUsername());
            SysUser userLogin = sysUserService.getOne(wrapper);
            if (userLogin != null) {
                return R.error("账号已存在!");
            }
            user.setId(UUID.randomUUID() + "");
            user.setPassword(DigestUtils.md5Hex(INITUSERPASSWORD));
            user.setCreatetime(new Date());
            if (user.getIsroot().equals(2)) {
                user.setClubid(user.getRootid());
            }
            if (user.getIsroot().equals(3)) {
                user.setBranchid(user.getRootid());
            }
            if (user.getIsroot().equals(4)) {
                user.setMerchantid(user.getRootid());
            }
            SysUser createUser = sysUserService.myGetById(UserContext.get());
            user.setCreateuser(createUser.getUsername());
            sysUserService.save(user);
            return R.ok("添加成功！");
        }
        if (!sysUserService.getById(user.getId()).getUsername().equals(user.getUsername())) {
            QueryWrapper<SysUser> wrapper = new QueryWrapper();
            wrapper.eq("UserName", user.getUsername());
            SysUser userLogin = sysUserService.getOne(wrapper);
            if (userLogin != null) {
                return R.error("账号已存在!");
            }
        }
        sysUserService.updateNull(user);
        if (user.getIsroot().equals(2)) {
            user.setClubid(user.getRootid());
            user.setBranchid(null);
            user.setMerchantid(null);
        }
        if (user.getIsroot().equals(3)) {
            user.setBranchid(user.getRootid());
            user.setClubid(null);
            user.setMerchantid(null);
        }
        if (user.getIsroot().equals(4)) {
            user.setMerchantid(user.getRootid());
            user.setClubid(null);
            user.setBranchid(null);
        }
        sysUserService.updateById(user);
        return R.ok("修改成功！");
    }


    /**
     * 修改用户密码
     *
     * @param
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParam(name = "params", value = "表单条件查询对象(oldpwd-旧密码,newpwd-新密码1,newpwds-新密码2 必传)", required = true, dataType = "Map<String, String>", paramType = "body")
    @PostMapping("/editPassword")
    public R editPassword(@RequestBody Map<String, String> params) {
        String oldpwd = params.get("oldpwd");
        String newpwd = params.get("newpwd");
        String newpwds = params.get("newpwds");

        if (oldpwd == null || StringUtils.isBlank(oldpwd)) {
            return R.error("请输入原密码!");
        }
        if (newpwd == null || StringUtils.isBlank(newpwd)) {
            return R.error("请输入新密码!");
        }
        if (newpwds == null || StringUtils.isBlank(newpwds)) {
            return R.error("请再次输入新密码!");
        }

        SysUser user = sysUserService.getById(UserContext.get());
        if (!user.getPassword().equals(DigestUtils.md5Hex(oldpwd))) {
            return R.error("原密码错误，修改失败！");
        }
        if (!newpwd.equals(newpwds)) {
            return R.error("两次输入的新密码不一致，修改失败！");
        }
        if (user.getPassword().equals(DigestUtils.md5Hex(newpwd))) {
            return R.error("新密码不能和原密码相同，修改失败！");
        }

        user.setPassword(DigestUtils.md5Hex(newpwd));
        sysUserService.updateById(user);

        return R.ok("修改成功！");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据id批量删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "params", value = "用户ids ", required = true)
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
        for (String id : ids) {
            if(id.equals(UserContext.get())){
                return R.error(400,"您删除的用户是当前登录用户，无法对其操作");
            }
        }

        sysUserService.removeByIds(Arrays.asList(ids));
        return R.ok("删除成功！");
    }

    /**
     * 修改用户状态
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改状态", notes = "修改状态")
    @ApiImplicitParam(name = "params", value = "需传入(id-用户ID,state-状态 必传)", required = true)
    @PostMapping("/updataUserState")
    public R updataUserState(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String state = params.get("state");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        if (state == null || StringUtils.isBlank(state)) {
            return R.error(400, "状态为空！");
        }
        if(id.equals(UserContext.get())){
            return R.error(400,"您操作的用户是当前登录用户，无法对其操作");
        }
        SysUser sysUser = new SysUser();
        sysUser.setLogincount(0);
        sysUser.setId(id);
        if (state.equals("1")) {
            sysUser.setState(0);
        }
        if (state.equals("0")) {
            sysUser.setState(1);
        }
        sysUserService.updateById(sysUser);
        return R.ok("修改状态成功！");
    }

    /**
     * 详情
     */
    @ApiOperation(value = "根据ID获取用户", notes = "获取单个网点用户")
    @ApiImplicitParam(name = "params", value = "用户ID", required = true, dataType = "Map<String, String>")
    @PostMapping("/info")
    public R info(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        //参数校验
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        SysUser sysUser = sysUserService.myGetById(id);
        if (sysUser == null) {
            return R.error(400, "未查询到该结果！");
        }
        return R.ok().put("user", sysUser);
    }


    /**
     * 获取下拉框列表
     */
    @ApiOperation(value = "根据选择的用户等级获取对应列表", notes = "获取下拉框二级列表")
    @ApiImplicitParam(name = "params", value = "用户等级", required = true, dataType = "Map<String, String>")
    @PostMapping("/selectRole")
    public R selectRole(@RequestBody Map<String, String> params) {
        //获取用户选择的等级以及用户自己的等级返回对应的所属机构列表
        String isroot = params.get("isroot");
        SysUser user = sysUserService.myGetById(UserContext.get());
        List resultList;
        //参数校验


        if (isroot == null || StringUtils.isBlank(isroot)) {
            return R.error(400, "角色为空！");
        }
        if (isroot.equals("2")) {
            if (user.getIsroot() == 2) {
                QueryWrapper<PayClub> wrapper = new QueryWrapper();
                wrapper.eq("id", user.getClubid());
                List<PayClub> clubList2 = payClubService.list(wrapper);
                return R.ok().put("clubList", clubList2);
            }
            if (user.getIsroot() == 1) {
                List<PayClub> clubList1 = payClubService.list();
                return R.ok().put("clubList", clubList1);
            }

        }
        if (isroot.equals("3")) {

            if (user.getIsroot() == 2) {
                QueryWrapper<PayBranch> wrapper = new QueryWrapper();
                wrapper.eq("clubid", user.getClubid());
                List<PayBranch> branchList2 = payBranchService.list(wrapper);
                return R.ok().put("branchList", branchList2);
            }
            if (user.getIsroot() == 1) {
                List<PayBranch> branchList1 = payBranchService.list();
                return R.ok().put("branchList", branchList1);
            }
            if (user.getIsroot() == 3) {
                QueryWrapper<PayBranch> wrapper = new QueryWrapper();
                wrapper.eq("id", user.getBranchid());
                List<PayBranch> branchList3 = payBranchService.list(wrapper);
                return R.ok().put("branchList", branchList3);
            }
        }
        if (isroot.equals("4")) {

            if (user.getIsroot() == 2) {
                List<PayMerchant> merchList2 = sysUserService.roleMerch(user.getClubid());
                return R.ok().put("merchList", merchList2);
            }
            if (user.getIsroot() == 1) {
                List<PayMerchant> merchList1 = payMerchantService.list();
                return R.ok().put("merchList", merchList1);
            }
            if (user.getIsroot() == 3) {
                QueryWrapper<PayMerchant> wrapper = new QueryWrapper();
                wrapper.eq("branchid", user.getBranchid());
                List<PayMerchant> merchList3 = payMerchantService.list(wrapper);
                return R.ok().put("merchList", merchList3);
            }
/*            if (user.getIsroot() == 4) {
                QueryWrapper<PayMerchant> wrapper = new QueryWrapper();
                wrapper.eq("id", user.getMerchantid());
                List<PayMerchant> merchList4 = payMerchantService.list(wrapper);
                return R.ok().put("merchList", merchList4);
            }*/
        }
        return R.ok();
    }

    /**
     * 重置用户密码
     */
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @ApiImplicitParam(name = "params", value = "需传入(id-用户ID)", required = true, dataType = "Map<String, String>")
    @PostMapping("/resetPassword")
    public R resetPassword(@RequestBody Map<String, String> params) {
        String id = params.get("ids");
        if (id == null || StringUtils.isBlank(id)) {
            return R.error(400, "id为空！");
        }
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            return R.error(400, "未查询到该用户！");
        }
        sysUser.setPassword(DigestUtils.md5Hex(INITUSERPASSWORD));
        sysUserService.updateById(sysUser);
        return R.ok("密码重置成功！");
    }

}
