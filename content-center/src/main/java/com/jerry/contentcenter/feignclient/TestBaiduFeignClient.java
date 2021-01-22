package com.jerry.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2021/1/22
 * Time: 19:53
 * Description:
 */
@FeignClient(name = "baidu", url = "https://www.baidu.com")
public interface TestBaiduFeignClient {

    @GetMapping("")
    String index();

}
