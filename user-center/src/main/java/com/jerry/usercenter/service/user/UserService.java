package com.jerry.usercenter.service.user;

import com.jerry.usercenter.dao.user.UserMapper;
import com.jerry.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/8
 * Time: 21:41
 * Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
