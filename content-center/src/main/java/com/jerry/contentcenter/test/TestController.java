package com.jerry.contentcenter.test;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.common.collect.Maps;
import com.jerry.contentcenter.dao.content.ShareMapper;
import com.jerry.contentcenter.domain.dto.user.UserDTO;
import com.jerry.contentcenter.domain.entity.content.Share;
import com.jerry.contentcenter.feignclient.TestBaiduFeignClient;
import com.jerry.contentcenter.feignclient.TestUserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/7
 * Time: 22:31
 * Description:
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestController {

    private final ShareMapper shareMapper;

    private final DiscoveryClient discoveryClient;

    private final TestUserCenterFeignClient testUserCenterFeignClient;

    private final TestBaiduFeignClient testBaiduFeignClient;

    private final TestService testService;

    @GetMapping("/test")
    public List<Share> testInsert() {
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("xxx");
        share.setCover("xxx");
        share.setAuthor("jerry");
        share.setBuyCount(1);
        shareMapper.insertSelective(share);
        return shareMapper.selectAll();
    }

    /**
     * 测试服务发现，证明内容中心总能找到用户中心
     *
     * @return
     */
    @GetMapping("/testDiscovery")
    public List<ServiceInstance> getInstances() {
        // 查询指定服务的所有实例信息
        return discoveryClient.getInstances("user-center");
    }

    @GetMapping("/testServices")
    public List<String> testServices() {
        // 查询注册的所有微服务
        return discoveryClient.getServices();
    }

    @GetMapping("/test/query")
    public UserDTO query(UserDTO userDTO) {
        return testUserCenterFeignClient.query(userDTO);
    }

    @GetMapping("/test/query2")
    public UserDTO query2() {
        return testUserCenterFeignClient.query2(1L, "test2");
    }

    @GetMapping("/test/query3")
    public UserDTO query3() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", "1");
        map.put("wxId", "test3");
        return testUserCenterFeignClient.query3(map);
    }

    @PostMapping("/add")
    public UserDTO save() {
        return testUserCenterFeignClient.save(new UserDTO());
    }

    @GetMapping("/baidu")
    public String baiduIndex() {
        return testBaiduFeignClient.index();
    }

    @GetMapping("/testA")
    public String testA() {
        testService.common();
        return "testA";
    }

    @GetMapping("/testB")
    public String testB() {
        testService.common();
        return "testB";
    }

    @SentinelResource("testHot")
    @GetMapping("/testHot")
    public String testHot(@RequestParam(required = false) String a, @RequestParam(required = false) String b) {
        return a + " " + b;
    }

    @GetMapping("/testAddFlowRule")
    public String testAddFlowRule() {
        initFlowQpsRule();
        return "success";
    }

    private void initFlowQpsRule() {
        List<FlowRule> ruleList = new ArrayList<>();
        FlowRule rule = new FlowRule("/shares/1");
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        ruleList.add(rule);
        FlowRuleManager.loadRules(ruleList);
    }

    @GetMapping("/testSentinelAPI")
    public String testSentinelAPI(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName, "test-service");
        // 定义一个 sentinel 保护的资源，名称是 test-sentinel-api
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a 不能为空");
            }
            return a;
        }
        // 如果被保护的资源被限流或者降级了，就会抛 BlockException 异常
        catch (BlockException e) {
            log.warn("限流或者降级了", e);
            e.printStackTrace();
            return "限流或者降级了";
        } catch (IllegalArgumentException e2) {
            // 统计 IllegalArgumentException（发生次数、占比等）
            Tracer.trace(e2);
            return "非法参数";
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

}
