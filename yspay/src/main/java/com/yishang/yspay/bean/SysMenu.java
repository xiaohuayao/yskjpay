package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@TableName("Sys_Menu")
public class SysMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private String id;
	/**
	 * 菜单名称
	 */
	@TableField(value = "Name")
	private String menuName;
	/**
	 * 组件名称
	 */
	private String title;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 路由地址
	 */
	private String route;
	/**
	 * 组件地址
	 */
	private String component;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 父级菜单ID
	 */
	private String parentid;
	/**
	 * $column.comments
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createtime;
	/**
	 * $column.comments
	 */
	private String createuser;

	/**
	 *
	 */
	@TableField(exist = false)
	private List<SysMenu> children;

}
