package com.jerry.contentcenter.service.content;

import com.jerry.contentcenter.dao.content.ShareMapper;
import com.jerry.contentcenter.domain.content.ShareDTO;
import com.jerry.contentcenter.domain.dto.user.UserDTO;
import com.jerry.contentcenter.domain.entity.content.Share;
import com.jerry.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/8
 * Time: 21:59
 * Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {

    private final ShareMapper shareMapper;

    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public ShareDTO findById(Integer id) {
        Share share = shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();

        // 远程调用
        List<ServiceInstance> instanceList = discoveryClient.getInstances("user-center");
        // 拿到服务地址
        String targetUrl = instanceList.stream()
                .map(instance -> instance.getUri() + "/users/{id}")
                // 这里先返回第1个
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前没有实例"));

        UserDTO userDTO = restTemplate.getForObject(targetUrl, UserDTO.class, userId);

        // 消息装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());

        return shareDTO;
    }

}


