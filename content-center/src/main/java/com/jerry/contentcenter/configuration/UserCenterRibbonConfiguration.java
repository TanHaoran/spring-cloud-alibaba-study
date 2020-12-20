package com.jerry.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
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
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
}
