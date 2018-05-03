package com.todorex.workflow.service;

import com.todorex.workflow.entity.ReAttend;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by rex on 2018/4/30.
 */
public interface ReAttendService {
    void startReAttendFlow(ReAttend reAttend);

    List<ReAttend> listTasks(String username);

    void approve(ReAttend reAttend);
    List<ReAttend> listReAttend(String username);
}
