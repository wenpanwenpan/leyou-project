package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * description
 *
 * @author 文攀 2019/11/07 17:33
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
