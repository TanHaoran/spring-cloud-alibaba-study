package com.jerry.contentcenter.configuration;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/20
 * Time: 17:19
 * Description:
 */
public class ExtendBalancer extends Balancer {

    public static Instance getByRandomWeight(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
