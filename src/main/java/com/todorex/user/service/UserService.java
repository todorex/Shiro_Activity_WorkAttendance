package com.todorex.user.service;

import com.todorex.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rex on 2018/4/29.
 */
public interface UserService {

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午12:59
     *@Description 根据用户名查找用户接口
     */
    User findUserByUserName(String username);

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午12:59
     *@Description 创建用户接口
     */
    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

}
