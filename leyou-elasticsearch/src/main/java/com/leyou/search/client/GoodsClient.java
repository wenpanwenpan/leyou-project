package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *商品的FeignClient：仅需要继承通用interface中的自定义API类就可以响应对应的请求
 * @author 文攀 2019/10/30 13:46
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {


}
