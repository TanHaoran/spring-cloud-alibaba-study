package com.jerry.usercenter;

import com.jerry.usercenter.dao.user.UserMapper;
import com.jerry.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    private final UserMapper userMapper;

    @GetMapping("/test")
    public User testInsert() {
        User user = new User();
        user.setAvatarUrl("xxx");
        user.setBonus(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertSelective(user);
        return user;
    }

    @GetMapping("/query")
    public User query(User user) {
        return user;
    }

    @PostMapping("/add")
    public User save(User user) {
        return user;
    }

}
