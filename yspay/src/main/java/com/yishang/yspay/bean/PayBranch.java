package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网点
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Pay_Branch")
public class PayBranch {

    @TableId
    private String id;

    /**
     * 网点名称
     */
    private String branchname;

    /**
     * 网点负责人
     */
    private String principal;

    /**
     * 网点负责人联系电话
     */
    private String phone;

    /**
     * 备注
     */
    private String message;

    /**
     * 所属行社
     */
    private String clubid;

    /**
     *
     * 所属行社名称
     */
    @TableField(exist = false)
    private String clubname;


}
