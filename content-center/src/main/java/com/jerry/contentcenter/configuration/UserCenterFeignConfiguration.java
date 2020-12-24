package com.jerry.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/24
 * Time: 21:25
 * Description: feign 的配置类，这个类不要加 @Configuration 注解，否则必须放在启动类所在包之外
 */
public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level level() {
        // 让 feign 打印所有请求的细节
        return Logger.Level.FULL;
    }

}
