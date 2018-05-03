package com.todorex.user.controller;

import com.todorex.user.entity.User;
import com.todorex.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by rex on 2018/4/29.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:19
     *@Description 返回主页
     */
    @RequestMapping("/home")
    public String user() {

        return "home";
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午4:21
     * @Description 获取用户信息
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public User getUser(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午4:28
     * @Description 退出登录
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

}
