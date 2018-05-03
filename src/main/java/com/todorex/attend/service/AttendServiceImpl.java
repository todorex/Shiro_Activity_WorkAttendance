package com.todorex.attend.service;

import com.todorex.attend.dao.AttendMapper;
import com.todorex.attend.entity.Attend;
import com.todorex.attend.vo.QueryCondition;
import com.todorex.common.page.PageQueryBean;
import com.todorex.common.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rex on 2018/4/29.
 */
@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService {
    // 对于方法的参数不用能数字（魔鬼数字）
    // 要定义成常量
    /**
     * 中午十二点 判定上下午
     */
    private static final int NOON_HOUR = 12;
    private static final int NOON_MINUTE = 00;

    /**
     * 早晚上班时间判定
     */
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    /**
     * 缺勤一整天
     */
    private static final Integer ABSENCE_DAY = 480;
    /**
     * 考勤异常状态
     */
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午1:03
     * @Description 打卡接口
     */
    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            // 12点之前算上午打卡，12点以后都是下午打卡，不足8小时（时间差）为异常

            // 获得打卡时间
            Date today = new Date();

            attend.setAttendDate(new Date());
            attend.setAttendWeek((byte) DateUtils.getTodayWeek());

            Date noon = DateUtils.getDate(NOON_HOUR, NOON_MINUTE);

            Attend todayRecord = attendMapper.selectTodaySignRecord(attend.getId());
            // 查询当天，此人是否已经打卡
            if (todayRecord == null) {
                //打卡记录还不存在
                if (today.compareTo(noon) <= 0) {
                    //打卡时间 早于12点 上午打卡
                    attend.setAttendMorning(today);
                } else {
                    //晚上打卡
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            } else {
                if (today.compareTo(noon) <= 0) {
                    //打卡时间 早于12点 上午打卡
                    return;
                } else {
                    //晚上打卡
                    todayRecord.setAttendEvening(today);
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }
        } catch (Exception e) {
            log.error("用户签到异常", e);
            throw e;
        }

    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午1:02
     * @Description 显示用户出勤列表，返回分页对象
     */
    @Override
    public PageQueryBean listAttend(QueryCondition condition) {
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if (count > 0) {
            pageResult.setTotalRows(count);
            // 设置当前页
            pageResult.setCurrentPage(condition.getCurrentPage());
            // 设置一页大小
            pageResult.setPageSize(condition.getPageSize());
            // 按条件查询
            List<Attend> attendList = attendMapper.selectAttendPage(condition);
            // 将查询结果放入pageResult
            pageResult.setItems(attendList);
        }
        return pageResult;
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 下午1:02
     * @Description 定时任务调用的接口，检查一天没有任何打卡的用户并新增记录
     */
    @Override
    @Transactional
    public void checkAttend() {

        //查询缺勤用户ID 插入打卡记录  并且设置为异常 缺勤480分钟
        List<Long> userIdList = attendMapper.selectTodayAbsence();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<Attend> attendList = new ArrayList<Attend>();
            for (Long userId : userIdList) {
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtils.getTodayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }
        // 检查晚打卡 将下班未打卡记录设置为异常
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if (CollectionUtils.isNotEmpty(absenceList)) {
            for (Attend attend : absenceList) {
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }

    }


}
