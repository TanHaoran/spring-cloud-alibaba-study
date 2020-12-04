package com.jerry.springbootdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/4
 * Time: 22:02
 * Description:
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
