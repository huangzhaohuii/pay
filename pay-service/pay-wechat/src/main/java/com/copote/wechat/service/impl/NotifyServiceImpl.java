package com.copote.wechat.service.impl;

import com.copote.wechat.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @create 2020/5/25
 * @Description:
 * @since 1.0.0
 */
@Service("notifyService")
@EnableBinding(value = {Source.class})
public class NotifyServiceImpl implements NotifyService {

    /**
     * 消息发送管道
     */
    @Qualifier("output")
    @Autowired
    private MessageChannel messageChannel;

    @Override
    public void send(String msg) {
        this.messageChannel.send(MessageBuilder.withPayload(msg).build());
    }
}
