package com.todorex.common.task;

import com.todorex.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rex on 2018/4/30.
 */
public class AttendCheckTask {

    @Autowired
    private AttendService attendService;

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:14
     *@Description 定时任务，用户当天检查为打卡用户
     */
    public void checkAttend() {
        attendService.checkAttend();
    }
}
