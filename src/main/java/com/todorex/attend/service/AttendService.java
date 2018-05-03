package com.todorex.attend.service;

import com.todorex.attend.entity.Attend;
import com.todorex.attend.vo.QueryCondition;
import com.todorex.common.page.PageQueryBean;
import org.springframework.stereotype.Service;

/**
 * Created by rex on 2018/4/29.
 */
public interface AttendService {
    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:03
     *@Description 打卡接口
     */
    void signAttend(Attend attend) throws Exception;

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:02
     *@Description 显示用户出勤列表，返回分页对象
     */
    PageQueryBean listAttend(QueryCondition condition);

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:02
     *@Description 定时任务调用的接口，检查一天没有任何打卡的用户并新增记录
     */
    void checkAttend();
}
