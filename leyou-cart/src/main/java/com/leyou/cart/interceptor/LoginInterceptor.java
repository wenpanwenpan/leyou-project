package com.leyou.cart.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description
 *  登录拦截器，拦截请求验证用户是否登录
 * @author 文攀 2019/11/12 10:21
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;

    //定义一个线程变量，存放登录用户。该变量为每一个访问的线程私有，可以将线程特有信息放入该变量中
    private static final ThreadLocal<UserInfo> TL = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //查询token
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        if(StringUtils.isBlank(token)){
            //返回未授权 401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            // 有token，查询用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //将用户信息放入到线程中，以便后面取用用户信息
            TL.set(userInfo);
            return true;
        }catch (Exception e){
            //出现异常，则返回未授权 401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    /**
     * 当请求完成之后（即返回给浏览器以后），需要从线程变量中移除掉用户信息。
     * 因为我们使用的是线程池技术，每当一个线程使用完毕之后都需要放回到线程池中
     * 以供下一个用户使用。所以需要将该线程的用户信息清空，然后放回到线程池
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TL.remove();
    }

    /**
     * 提供一个获取用户信息的方法供外界调用，然后获取用户信息
     * @return
     */
    public static UserInfo getLoginUser() {
        return TL.get();
    }
}
