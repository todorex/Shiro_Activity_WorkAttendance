package com.todorex.user.service;

import com.todorex.common.utils.MD5Utils;
import com.todorex.user.dao.UserMapper;
import com.todorex.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rex on 2018/4/29.
 */
@Service("userServiceImp1")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午12:59
     * @Description 创建用户接口
     */
    // 模拟创建用户，主要执行对用户密码的加密
    @Transactional
    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 在业务代码中，需要try-catch捕获异常，并记录到日志,但是如果是事务方法需要在记录日志之后将异常抛出以供回滚
        user.setPassword(MD5Utils.encryptPassword(user.getPassword()));
        // 利用可以缺参数的插入语句
        userMapper.insertSelective(user);
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午1:40
     * @Description 根据用户名查询用户
     */
    @Override
    public User findUserByUserName(String username) {
        User user = null;
        try {
            user = userMapper.selectByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


}
