package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description
 *
 * @author 文攀 2019/11/07 17:27
 */
//@RequestMapping("user")
public interface UserApi {

    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username,
                          @RequestParam("password") String password);
}
