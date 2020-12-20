package com.jerry.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/20
 * Time: 16:37
 * Description: 自定义权重规则
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 读取配置文件，并初始化 NacosWeightedRule
     *
     * @param iClientConfig
     */
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try {
            // ILoadBalancer 是 Ribbon 的入口
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) getLoadBalancer();

            // 请求的微服务名称
            String name = loadBalancer.getName();

            // 负载均衡算法
            // 服务发现相关的 API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // nacos client 自动通过基于权重的负载均衡算法选择一个实例，只拿健康的实例
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("选择的实例是：port: {}, instance: {}", instance.getPort(), instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            return null;
        }
    }

}
