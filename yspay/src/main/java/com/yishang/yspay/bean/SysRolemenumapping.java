package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

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
@TableName("Sys_RoleMenuMapping")
public class SysRolemenumapping implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private String id;
	/**
	 * 角色ID
	 */
	private String roleid;
	/**
	 * 菜单ID
	 */
	private String menuid;

}
