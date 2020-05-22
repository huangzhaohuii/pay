package com.copote.wechat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author: HUANG
 * @date:  2020-05-22 15:40
 * @Description: 渠道信息
 * @version V1.0
 */
@Data
@TableName("t_pay_channel")
public class PayChannel implements Serializable {
    /**
     * 渠道主键ID
     *
     * @mbggenerated
     */
    @TableId
    private Integer id;

    /**
     * 渠道名称,如:alipay,wechat
     *
     * @mbggenerated
     */
    private String channelName;

    /**
     * 渠道状态,0-停止使用,1-使用中
     *
     * @mbggenerated
     */
    private Byte state;


    /**
     * 备注
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */

}