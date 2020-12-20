package com.jerry.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/20
 * Time: 14:47
 * Description:
 */
@Configuration
@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
}
