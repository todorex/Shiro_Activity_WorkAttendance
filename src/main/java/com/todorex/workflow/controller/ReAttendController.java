package com.todorex.workflow.controller;

import com.todorex.user.entity.User;
import com.todorex.workflow.entity.ReAttend;
import com.todorex.workflow.service.ReAttendService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by rex on 2018/4/30.
 */
@Controller
@RequestMapping("reAttend")
public class ReAttendController {

    @Autowired
    private ReAttendService reAttendService;

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午1:05
     * @Description 获得补签记录
     */
    // Velocity用Model传值
    //不能设置（reAttend这样的权限，不然会允许所有权限）
    @RequestMapping
    public String toReAttend(Model model, HttpSession session) {

        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        List<ReAttend> reAttendList = reAttendService.listReAttend(user.getUsername());
        model.addAttribute("reAttendList", reAttendList);
        return "reAttend";
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午1:06
     * @Description 发起补签流程
     */
    @RequestMapping("/start")
    public void startReAttendFlow(@RequestBody ReAttend reAttend, HttpSession session, HttpServletResponse response) {
        User user = (User) session.getAttribute("userInfo");
        reAttend.setReAttendStarter(user.getRealName());
        reAttendService.startReAttendFlow(reAttend);
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/30 下午1:07
     * @Description 查看自己的任务
     */
    // 根据角色
    // @RequiresRoles("leader")
    // 根据权限
    @RequiresPermissions("reAttend:list")
    @RequestMapping("/list")
    public String listReAttendFlow(Model model, HttpSession session) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        String userName = user.getUsername();
        List<ReAttend> tasks = reAttendService.listTasks(userName);
        model.addAttribute("tasks", tasks);
        return "reAttendApprove";
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/4/30 下午1:06
     * @Description 完成审批
     */
    // 如果返回值为空，则一定要加HttpServletResponse response参数，不然会报错
    // 详情可以看这个博客https://blog.csdn.net/yh_zeng2/article/details/75136614
    @RequestMapping("/approve")
    public void approveReAttendFlow(@RequestBody ReAttend reAttend, HttpServletResponse response) {
        reAttendService.approve(reAttend);
    }


}
