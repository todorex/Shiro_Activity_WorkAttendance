package com.todorex.common.interceptor;

import com.todorex.user.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用于判断用户是否登录，是否放行
 * Created by rex on 2018/4/29.
 */
public class SessionInterceptor implements HandlerInterceptor{
    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:11
     *@Description 用来过滤请求（前处理器）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String url = request.getRequestURI();
        if (url.indexOf("login")>=0) {
            return true;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        if (user!=null) {
            return true;
        }
        request.getRequestDispatcher("/login").forward(request,response);
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
