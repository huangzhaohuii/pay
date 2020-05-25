package com.copote.wechat.service;

/**
 * @author Administrator
 * @create 2020/5/25
 * @Description: 消息通知
 * @since 1.0.0
 */
public interface NotifyService {

    /**
     * 消息发送
     * @param msg
     * @return
     */
    void send(String msg);
}
