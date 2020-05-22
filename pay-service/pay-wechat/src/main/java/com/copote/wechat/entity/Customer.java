package com.copote.wechat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @create 2020/5/22
 * @Description: 客户表
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_customer")
public class Customer {

    /**
     * 客户类型
     */
    @TableId
    private String ccXzsxlx;

    /**
     * 通知地址
     */
    private String ccNotifyUrl;

    /**
     * 通知次数
     */
    private int nnNotifyCount;
}
