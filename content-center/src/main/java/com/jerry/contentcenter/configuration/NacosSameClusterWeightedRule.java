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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/20
 * Time: 16:57
 * Description: 同一集群优先调用规则
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try {
            // 获取配置文件中的集群名称 BJ
            String clusterName = nacosDiscoveryProperties.getClusterName();

            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) getLoadBalancer();
            String name = loadBalancer.getName();
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // 1、找到指定服务的所有实例 A，只拿健康的实例
            List<Instance> instanceList = namingService.selectInstances(name, true);

            // 2、过滤出相同集群下的所有实例 B
            List<Instance> sameClusterInstanceList = instanceList.stream().filter(
                    instance -> Objects.equals(instance.getClusterName(), clusterName)
            ).collect(Collectors.toList());

            // 3、如果没有 B，就用 A
            List<Instance> chooseInstanceList;
            if (CollectionUtils.isEmpty(sameClusterInstanceList)) {
                chooseInstanceList = instanceList;
                log.warn("发生跨级群调用，name：{}, clusterName：{}，instanceList：{}", name, clusterName, instanceList);
            } else {
                chooseInstanceList = sameClusterInstanceList;
            }

            // 4、基于权重的负载均衡算法，返回 1 个实例
            Instance instance = ExtendBalancer.getByRandomWeight(chooseInstanceList);
            log.info("选择的实例是：port：{}, instance：{}", instance.getPort(), instance);

            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("发生异常了", e);
            return null;
        }
    }

}

