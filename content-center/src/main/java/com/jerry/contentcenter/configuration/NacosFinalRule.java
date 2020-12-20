package com.jerry.contentcenter.configuration;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * Time: 18:59
 * Description:
 */
@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        // 负载均衡规则：优先选择同集群下，符合metadata的实例
        // 如果没有，就选择所有集群下，符合metadata的实例

        // 1. 查询所有实例 A
        // 2. 筛选元数据匹配的实例 B
        // 3. 筛选出同cluster下元数据匹配的实例 C
        // 4. 如果C为空，就用B
        // 5. 随机选择实例
        try {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            String targetVersion = nacosDiscoveryProperties.getMetadata().get("target-version");

            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) getLoadBalancer();
            String name = loadBalancer.getName();

            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // 所有实例
            List<Instance> instanceList = namingService.selectInstances(name, true);

            List<Instance> metadataMatchInstanceList = instanceList;
            // 如果配置了版本映射，那么只调用元数据匹配的实例
            if (StringUtils.isNotBlank(targetVersion)) {
                metadataMatchInstanceList = instanceList.stream()
                        .filter(instance -> Objects.equals(targetVersion, instance.getMetadata().get("version")))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(metadataMatchInstanceList)) {
                    log.warn("未找到元数据匹配的目标实例！请检查配置。targetVersion：{}，instance：{}",
                            targetVersion, instanceList);
                    return null;
                }
            }

            List<Instance> clusterMetadataMatchInstanceList = metadataMatchInstanceList;
            // 如果配置了集群名称，需筛选同集群下元数据匹配的实例
            if (StringUtils.isNotBlank(clusterName)) {
                clusterMetadataMatchInstanceList = metadataMatchInstanceList.stream()
                        .filter(instance -> Objects.equals(clusterName, instance.getClusterName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(clusterMetadataMatchInstanceList)) {
                    clusterMetadataMatchInstanceList = metadataMatchInstanceList;
                    log.warn("发生跨集群调用。clusterName：{}，targetVersion：{}，clusterMetadataMatchInstanceList：{}",
                            clusterName, targetVersion, clusterMetadataMatchInstanceList);
                }
            }

            Instance instance = ExtendBalancer.getByRandomWeight(clusterMetadataMatchInstanceList);
            log.info("选择的实例是：port：{}, instance：{}", instance.getPort(), instance);
            return new NacosServer(instance);
        } catch (Exception e) {
            log.warn("发生异常", e);
            return null;
        }
    }

}
