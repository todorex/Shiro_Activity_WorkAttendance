package com.todorex.attend.vo;

import com.todorex.common.page.PageQueryBean;

/**
 * 变成了有条件的分页Bean
 * Created by rex on 2018/4/29.
 */
public class QueryCondition extends PageQueryBean{
    private Long userId;

    private String startDate ;

    private String endDate ;

    private String rangeDate;

    private Byte attendStatus;

    public Byte getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(Byte attendStatus) {
        this.attendStatus = attendStatus;
    }

    public String getRangeDate() {
        return rangeDate;
    }

    public void setRangeDate(String rangeDate) {
        this.rangeDate = rangeDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
