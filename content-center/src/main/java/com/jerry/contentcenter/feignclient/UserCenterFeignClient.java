package com.jerry.contentcenter.feignclient;

import com.jerry.contentcenter.configuration.UserCenterFeignConfiguration;
import com.jerry.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/24
 * Time: 20:31
 * Description:
 */
@FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
public interface UserCenterFeignClient {

    /**
     * 实际请求地址：http://user-center/users/{id}
     *
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);

}
