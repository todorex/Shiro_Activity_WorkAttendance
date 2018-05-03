package com.todorex.login.controller;

/**
 * Created by rex on 2018/4/29.
 */

import com.todorex.user.entity.User;
import com.todorex.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午1:39
     * @Description 返回登录页面
     */
    @RequestMapping
    public String login() {
        return "login";
    }


    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午1:39
     * @Description 校验登录
     */
    @RequestMapping("/check")
    // 返回json
    @ResponseBody
    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");
        UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
//        如果前台传递RememberMe参数则可设置为true
//        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            // 设置session过期时间
            SecurityUtils.getSubject().getSession().setTimeout(1800000);
        } catch (Exception e) {
            return "login_fail";
        }
        return "login_succ";
    }

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午12:58
     *@Description 注册用户接口
     */
    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userService.createUser(user);
        return "succ";
    }


}

