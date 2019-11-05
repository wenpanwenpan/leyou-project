package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/11/04 10:34
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
