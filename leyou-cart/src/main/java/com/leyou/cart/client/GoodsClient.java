package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/11/12 10:48
 */@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
