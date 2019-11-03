package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/10/31 13:47
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
