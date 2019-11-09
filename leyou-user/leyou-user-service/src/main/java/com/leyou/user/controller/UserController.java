package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping(value = "check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data,@PathVariable("type") Integer type){

        Boolean flag = this.userService.checkUserData(data,type);
        return ResponseEntity.ok(flag);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping(value = "code")
    public ResponseEntity<Void> sendVerifyCode(String phone){

        Boolean flag = this.userService.sendVerifyCode(phone);
        if(!flag || flag == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 用户注册
     * @Valid 使用hibernate-validator做后台数据格式校验
     * @param user
     * @param code
     * @return
     */
    @PostMapping(value = "register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){

        Boolean flag = this.userService.register(user,code);
        if(flag == null || !flag){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new  ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password){

        User user = this.userService.queryUser(username,password);
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

}
