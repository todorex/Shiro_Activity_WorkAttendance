package com.todorex.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rex on 2018/4/29.
 */
public class DateUtils {

    private static Calendar calendar = Calendar.getInstance();

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/4/29 下午6:12
     *@Description 获得星期几
     */
    public static int getTodayWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 国外星期天等于0
        int week = calendar.get(Calendar.DAY_OF_WEEK-1);
        if (week < 0) {
            week = 7;
        }
        return week;

    }

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/4/29 下午6:11
     *@Description 获得分钟数
     */
    public static int getMunite(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();
        int munite = (int) ((end-start)/60*1000);
        return munite;
    }

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/4/29 下午6:16
     *@Description 获取当前的某个时间
     */
    public static Date getDate(int hour, int munite) {
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,munite);
        return calendar.getTime();
    }


    public static void main(String[] args) {

        calendar.setTime(new Date());
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println(week);
    }
}
