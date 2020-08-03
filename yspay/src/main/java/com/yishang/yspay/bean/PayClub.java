package com.yishang.yspay.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行社
 *
 * @author zjf
 * @email 962700753@qq.com
 * @date 2020-06-22 09:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Pay_Club")
public class PayClub {

    @TableId
    private String id;

    /**
     * 行社名称n
     */
    private String clubname;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 负责人联系电话
     */
    private String phone;

    /**
     * 备注
     */
    private String message;

}
