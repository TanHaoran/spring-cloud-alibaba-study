package com.jerry.contentcenter;

import com.google.common.collect.Maps;
import com.jerry.contentcenter.dao.content.ShareMapper;
import com.jerry.contentcenter.domain.dto.user.UserDTO;
import com.jerry.contentcenter.domain.entity.content.Share;
import com.jerry.contentcenter.feignclient.TestBaiduFeignClient;
import com.jerry.contentcenter.feignclient.TestUserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class TestController {

    private final ShareMapper shareMapper;

    private final DiscoveryClient discoveryClient;

    private final TestUserCenterFeignClient testUserCenterFeignClient;

    private final TestBaiduFeignClient testBaiduFeignClient;

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
        List<Share> shareList = shareMapper.selectAll();
        return shareList;
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

}
