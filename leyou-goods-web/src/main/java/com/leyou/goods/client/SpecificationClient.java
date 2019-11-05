package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/11/04 10:36
 */
@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
