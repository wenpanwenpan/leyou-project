package com.leyou.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * description
 * 监听消息队列，消息队列中一有消息就开始发送短信。
 * @author 文攀 2019/11/06 15:00
 */
@Component
@EnableConfigurationProperties
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties prop;     //注入短信配置类

    /**
     * 通过队列名称和交换器名称以及路由键来将交换器和队列进行绑定
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue",durable = "true"),
            exchange = @Exchange(value = "leyou.sms.exchange",
            ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}
    ))
    public void listenSms(Map<String,String> msg) throws Exception{

        if(CollectionUtils.isEmpty(msg) || msg.size() <= 0){
            //放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
            //放弃处理
            return;
        }
        //发送短信消息
        SendSmsResponse resp = this.smsUtils.sendSms(phone, code, prop.getSignName(), prop.getVerifyCodeTemplate());
    }
}
