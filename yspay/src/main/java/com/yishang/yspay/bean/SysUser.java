package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ${comments}
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Sys_Users")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private String id;
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * 角色ID
	 */
	private String roleid;

	/**
	 * 角色
	 */
	@TableField(exist = false)
	private String rolename;

	/**
	 * 用户状态，1正常，0禁用
	 */
	private Integer state;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createtime;
	/**
	 * 创建人ID
	 */
	private String createuser;

	/**
	 * 所属行社ID
	 */
	private String clubid;

	/**
	 * 所属行社
	 */
	@TableField(exist = false)
	private String clubname;

	/**
	 * 所属网点ID
	 */
	private String branchid;

	/**
	 * 所属网点
	 */
	@TableField(exist = false)
	private String branchname;

	/**
	 * 所属商户ID
	 */
	private String merchantid;

	/**
	 * 所属商户
	 */
	@TableField(exist = false)
	private String merchantname;

	/**
	 * isROOT(1管理员，2行社，3网点，4商户)
	 */
	private Integer isroot;

	/**
	 * 所属机构
	 */
	@TableField(exist = false)
	private String rootid;

	/**
	 * LoginCount(登录失败次数)
	 */
	private Integer logincount;

	/**
	 * 最后一次失败时间
	 */
	private Date defeatedtime;

}
