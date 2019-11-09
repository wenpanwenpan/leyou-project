package com.leyou.user.service;

import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean checkUserData(String data, Integer type) {

        User record = new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 发送验证码，并且将验证码存入到redis缓存中，以备下次校验
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone) {

        // 生成验证码
        String code = NumberUtils.generateCode(6);

        //发送短信
       try {
           HashMap<String, String> msg = new HashMap<>();
           msg.put("phone",phone);
           msg.put("code",code);

           //将手机号和验证码组成的集合放入到rabbitmq队列中
           this.amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);

           //将验证码code存入redis,设置5分钟过期
           this.redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);

           return true;
       }catch (Exception e){
           logger.error("发送短信失败。phone：{}， code：{}", phone, code);
           return false;
       }
    }

    public Boolean register(User user, String code) {

        //去redis中获取验证码，校验短信验证码
        String cacheCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        if(!StringUtils.equals(code,cacheCode)){
            return false;
        }

        //验证码校验通过则生成盐,并且设置到用户上
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //使用盐对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());

        //将用户信息添加到数据库
        Boolean flag = this.userMapper.insertSelective(user) == 1;
        if(flag){
            // 注册成功，删除redis中的记录
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }

        return flag;
    }

    public User queryUser(String username, String password) {

        User recode = new User();
        recode.setUsername(username);

        User user = this.userMapper.selectOne(recode);

        //校验用户名
        if(user == null){
            return null;
        }

        //校验密码
        if(!user.getPassword().equals(CodecUtils.md5Hex(password,user.getSalt()))){
            return null;
        }

        return user;
    }
}
