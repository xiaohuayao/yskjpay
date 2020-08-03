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
 * 商户
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Pay_Merchant")
public class PayMerchant implements Serializable {

	/**
	 * $column.comments
	 */
	@TableId
	private String id;
	/**
	 * 应用密钥
	 */
	private String appkey;
	/**
	 * 商户名称
	 */
	private String merchname;
	/**
	 * 威富通商户号
	 */
	private String swiffpassmerchno;
	/**
	 * 商户密钥
	 */
	private String swiffpasspaysecert;
	/**
	 * 开启通道，1=威富通，2=统一收单
	 */
	private Integer openchannel;
	/**
	 * 统一收单账号
	 */
	private String merid;
	/**
	 * 商户标识（统一收单特定参数）
	 */
	private String merinst;
	/**
	 * 证书存储位置
	 */
	private String certpath;
	/**
	 * 证书密钥
	 */
	private String certpwd;
	/**
	 * 创建人
	 */
	private String createuser;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createtime;

	/**
	 * 商户状态
	 */
	private Integer status;

	/**
	 * 所属网点
	 */
	private String branchid;

	/**
	 * 网点名称
	 */
	@TableField(exist = false)
	private String branchname;

	/**
	 * 行社名称
	 */
	@TableField(exist = false)
	private String clubname;

	/**
	 * 扣点信息
	 */
	private float bucklepoint;

	/**
	 * 商户最后一次的交易时间
	 */
	@TableField(exist = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastpaytime;

}
