package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/11/04 10:35
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
