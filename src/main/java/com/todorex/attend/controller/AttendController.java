package com.todorex.attend.controller;

import com.todorex.attend.entity.Attend;
import com.todorex.attend.service.AttendService;
import com.todorex.attend.vo.QueryCondition;
import com.todorex.common.page.PageQueryBean;
import com.todorex.user.entity.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by rex on 2018/4/29.
 */
@Controller
@RequestMapping("attend")
public class AttendController {

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toAttend() {
        return "attend";
    }


    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/29 下午5:48
     * @Description 签到
     */
    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend(@RequestBody Attend attend, HttpSession session) throws Exception {
        attendService.signAttend(attend);
        return "succ";
    }


    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午12:58
     *@Description 显示用户出勤列表
     */
//    设置权限
    @RequiresPermissions("attend:attendList")
    @RequestMapping("/attendList")
    @ResponseBody
    // QueryCondition会自动注入(JavaBean的特性)
    public PageQueryBean listAttend(QueryCondition condition,HttpSession session){

        User user = (User) session.getAttribute("userInfo");

        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;
    }
}
