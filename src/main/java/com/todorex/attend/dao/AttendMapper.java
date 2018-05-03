package com.todorex.attend.dao;

import com.todorex.attend.entity.Attend;
import com.todorex.attend.vo.QueryCondition;

import java.util.List;

public interface AttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

    Attend selectTodaySignRecord(Long id);

    int countByCondition(QueryCondition condition);

    List<Attend> selectAttendPage(QueryCondition condition);

    void batchInsert(List userIdList);

    List<Long> selectTodayAbsence();

    List<Attend> selectTodayEveningAbsence();
}