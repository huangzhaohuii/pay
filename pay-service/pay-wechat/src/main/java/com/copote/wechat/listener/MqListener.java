package com.copote.wechat.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.wechat.service.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 * @create 2020/5/25
 * @Description: 业务通知MQ实现
 * @since 1.0.0
 */
@Component
@EnableBinding(value = {Sink.class})
@Slf4j
public class MqListener {

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 处理MQ中消息发送
     * @param message
     */
//    @StreamListener(Sink.INPUT)
    public void receive(Message message){
        log.info("do notify task, msg={}", message);
        JSONObject msgObj = (JSONObject) message.getPayload();
        //通知地址
        String respUrl = msgObj.getString("url");
        //订单号
        String orderId = msgObj.getString("orderId");
        //通知次数
        int count = msgObj.getInteger("count");
        if(StringUtils.isEmpty(respUrl)) {
            log.error("notify url is empty. respUrl={}", respUrl);
            return;
        }
        try {
            StringBuffer sb = new StringBuffer();
            URL console = new URL(respUrl);
            log.info("==>MQ通知业务系统开始[orderId：{}][count：{}][time：{}]", orderId, count, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if("https".equals(console.getProtocol())) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[] {
//                        new Mq4PayNotify.TrustAnyTrustManager()
                        },
                        new java.security.SecureRandom());
                HttpsURLConnection con = (HttpsURLConnection) console.openConnection();
                con.setSSLSocketFactory(sc.getSocketFactory());
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setConnectTimeout(10 * 1000);
                con.setReadTimeout(5 * 1000);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()), 1024*1024);
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                in.close();
            }else if("http".equals(console.getProtocol())) {
                HttpURLConnection con = (HttpURLConnection) console.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setConnectTimeout(10 * 1000);
                con.setReadTimeout(5 * 1000);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()), 1024*1024);
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                in.close();
            }else {
                log.error("not do protocol. protocol=%s", console.getProtocol());
                return;
            }
            log.info("<==MQ通知业务系统结束[orderId：{}][count：{}][time：{}]", orderId, count, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // 验证结果
            log.info("notify response , OrderID={}", orderId);
            if(sb.toString().trim().equalsIgnoreCase("success")){
                //log.info("{} notify success, url:{}", _notifyInfo.getBusiId(), respUrl);
                //修改订单表
                try {
                    int result = payOrderService.updateStatus4Complete(orderId);
                    log.info("修改payOrderId={},订单状态为处理完成->{}", orderId, result == 1 ? "成功" : "失败");
                } catch (Exception e) {
                    log.error("修改订单状态为处理完成异常");
                }
                // 修改通知次数
                try {
                    int result = payOrderService.updateNotify(orderId, (byte) 1);
                    log.info("修改payOrderId={},通知业务系统次数->{}", orderId, result == 1 ? "成功" : "失败");
                }catch (Exception e) {
                    log.error("修改通知次数异常");
                }
                return ; // 通知成功结束
            }else {
                // 通知失败，延时再通知
                int cnt = count+1;
                log.info("notify count={}", cnt);
                // 修改通知次数
                try {
                    int result = payOrderService.updateNotify(orderId, (byte) cnt);
                    log.info("修改payOrderId={},通知业务系统次数->{}", orderId, result == 1 ? "成功" : "失败");
                }catch (Exception e) {
                    log.error("修改通知次数异常");
                }

                if (cnt > 5) {
                    log.info("notify count>5 stop. url={}", respUrl);
                    return ;
                }
                msgObj.put("count", cnt);
//                发送延迟消息机制
//                this.send(msgObj.toJSONString(), cnt * 60 * 1000);
            }
            log.warn("notify failed. url:{}, response body:{}", respUrl, sb.toString());
        } catch(Exception e) {
            log.info("<==MQ通知业务系统结束[orderId：{}][count：{}][time：{}]", orderId, count, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            log.error( "notify exception. url:%s", respUrl);
        }

    }

    @StreamListener(Sink.INPUT)
    public void test(Message message){
        System.out.println(message);
    }
}
