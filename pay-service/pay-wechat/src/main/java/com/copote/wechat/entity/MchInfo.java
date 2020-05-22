package com.copote.wechat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author: HUANG
 * @date:  2020-05-22 15:39
 * @Description: 商户信息
 * @version V1.0
 */
@Data
@TableName("t_mch_info")
public class MchInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @TableId
    private String mchId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 请求私钥
     */
    private String reqKey;

    /**
     * 响应私钥
     */
    private String resKey;

    /**
     * 商户状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}