package com.jerry.contentcenter.feignclient;

import com.jerry.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2021/1/13
 * Time: 20:54
 * Description:
 */
@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {

    @GetMapping("/query")
    UserDTO query(@SpringQueryMap UserDTO userDTO);

    @GetMapping(value = "/query")
    UserDTO query2(@RequestParam("id") Long id, @RequestParam("wxId") String wxId);

    @GetMapping(value = "/query")
    UserDTO query3(@RequestParam Map<String, Object> map);

    @PostMapping(value = "/add")
    UserDTO save(@RequestBody UserDTO user);

}
