package com.jerry.contentcenter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2021/2/16
 * Time: 18:42
 * Description:
 */
@Slf4j
@Service
public class TestService {

    @SentinelResource("common")
    public String common() {
        log.info("common...");
        return "common";
    }

}
