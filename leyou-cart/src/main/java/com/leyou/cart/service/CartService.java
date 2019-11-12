package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author 文攀 2019/11/12 10:45
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "leyou:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart) {
        //获取用户登录信息
        UserInfo user = LoginInterceptor.getLoginUser();
        //存入redis的key
        String key = KEY_PREFIX + user.getId();
        //获取hash操作对象,这一步就是通过key从redis中取出该用户下的所有商品了
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //查询要添加到购物车的商品是否存在于redis
        Long skuId = cart.getSkuId();
        //要加入购物车的商品数量
        Integer num = cart.getNum();
        Boolean boo = hashOps.hasKey(skuId.toString());
        if(boo){
            // 存在，获取购物车数据
            String json = hashOps.get(skuId.toString()).toString();
            //将获取的json字符串格式的商品转化为商品
            cart = JsonUtils.parse(json,Cart.class);
            // 修改购物车数量
            cart.setNum(cart.getNum() + num);
        }else {
            //redis中不存在该购物车信息，则新增购物车
            cart.setUserId(user.getId());
            //其他商品信息，需要查询商品微服务
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        //将购物车数据写入redis
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车列表
     * @return
     */
    public List<Cart> queryCartList() {

        //获取登录用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        //判断购物车是否存在
        if(!this.redisTemplate.hasKey(key)){
            //不存在，则直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        //取得所有购物车
        List<Object> carts = hashOps.values();
        //判断是否有数据
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }
        //查询购物车数据，将从redis中查询到的String类型的购物车商品数据转化为对象集合类型
        return carts.stream().map(o -> JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 更新购物车数量
     * @param cart
     */
    public void updateNum(Cart cart) {

        //获取用户登录信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();

        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        //获取购物车信息
        String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
        Cart cart1 = JsonUtils.parse(cartJson, Cart.class);
        //更新数量
        cart1.setNum(cart.getNum());

        //写入购物车
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart1));
    }

    /**
     * 根据id删除一条购物车数据
     * @param skuId
     */
    public void deleteCart(String skuId) {

        //获取登录用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        //从redis中获取该用户的购物车信息
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
