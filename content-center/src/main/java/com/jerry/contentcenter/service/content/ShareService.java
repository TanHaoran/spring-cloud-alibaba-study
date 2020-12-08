package com.jerry.contentcenter.service.content;

import com.jerry.contentcenter.dao.content.ShareMapper;
import com.jerry.contentcenter.domain.content.ShareDTO;
import com.jerry.contentcenter.domain.dto.user.UserDTO;
import com.jerry.contentcenter.domain.entity.content.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/8
 * Time: 21:59
 * Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final ShareMapper shareMapper;
    private final RestTemplate restTemplate;

    public ShareDTO findById(Integer id) {
        Share share = shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();

        // 远程调用
        UserDTO userDTO = restTemplate.getForObject("http://localhost:8080/users/{id}",
                UserDTO.class, userId);
        // 消息装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());

        return shareDTO;
    }

}


