package com.todorex.workflow.service;

import com.todorex.attend.dao.AttendMapper;
import com.todorex.attend.entity.Attend;
import com.todorex.attend.service.AttendService;
import com.todorex.workflow.dao.ReAttendMapper;
import com.todorex.workflow.entity.ReAttend;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rex on 2018/4/30.
 */
@Service("reAttendServiceImpl")
public class ReAttendServiceImpl implements ReAttendService {

    private static final java.lang.String RE_ATTEND_FLOW_ID = "re_attend";

    /**
     * 补签流程状态
     */
    // 进行中
    private static final Byte RE_ATTEND_STATUS_ONGOING = 1;
    // 通过
    private static final Byte RE_ATTEND_STATUS_PSSS = 2;
    // 被拒绝
    private static final Byte RE_ATTEND_STATUS_REFUSE = 3;
    /**
     *
     */
    private static final Byte ATTEND_STATUS_NORMAL = 1;
    /**
     * 流程下一步处理人
     */
    private static final String NEXT_HANDLER = "next_handler";
    /**
     * 任务关联补签数据键（bpmn的id）
     */
    private static final String RE_ATTEND_SIGN = "re_attend";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReAttendMapper reAttendMapper;

    @Autowired
    private AttendMapper attendMapper;

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:08
     *@Description 开始补签流程
     */
    @Override
    @Transactional
    public void startReAttendFlow(ReAttend reAttend) {
        // 从公司组织架构中，查询到此人上级领导人用户名
        // 这里手动设置
        reAttend.setCurrentHandler("rex666");
        reAttend.setStatus(RE_ATTEND_STATUS_ONGOING);
        // 插入数据库补签表(ID自增)
        reAttendMapper.insertSelective(reAttend);
        // 将一些需要的参数放入流程中传递，即Variables
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RE_ATTEND_SIGN, reAttend);
        // 后面需要根据流程中的变量名来获取
        map.put(NEXT_HANDLER, reAttend.getCurrentHandler());
        // 获得流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(RE_ATTEND_FLOW_ID, map);
        // 获得任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        // 提交用户补签任务
        taskService.complete(task.getId(), map);
    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 上午11:31
     * @Description 审批工作流
     */
    @Override
    @Transactional
    public void approve(ReAttend reAttend) {
        Task task = taskService.createTaskQuery().taskId(reAttend.getTaskId()).singleResult();
        // 如果同意
        if (("" + RE_ATTEND_STATUS_PSSS).equals(reAttend.getApproveFlag())) {
            Attend attend = new Attend();
            attend.setId(reAttend.getAttendId());
            attend.setAttendStatus(ATTEND_STATUS_NORMAL);
            // 将出勤数据的状态从异常变为正常
            attendMapper.updateByPrimaryKeySelective(attend);
            // 审批通过，修改补签数据状态
            reAttend.setStatus(RE_ATTEND_STATUS_PSSS);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);

        } else if (("" + RE_ATTEND_STATUS_REFUSE.toString()).equals(reAttend.getApproveFlag())) {
            reAttend.setStatus(RE_ATTEND_STATUS_REFUSE);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }
        taskService.complete(task.getId());

    }

    /**
     * @Author RexLi [www.todorex.com]
     * @Date 2018/5/3 上午11:31
     * @Description 根据流程任务变量 查询某人需要处理的任务
     */
    @Override
    public List<ReAttend> listTasks(String userName) {
        List<ReAttend> reAttendList = new ArrayList<ReAttend>();
        List<Task> taskList = taskService.createTaskQuery().processVariableValueEquals(userName).list();
        //转换成页面实体
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                Map<String, Object> variable = taskService.getVariables(task.getId());
                ReAttend reAttend = (ReAttend) variable.get(RE_ATTEND_SIGN);
                reAttend.setTaskId(task.getId());
                reAttendList.add(reAttend);
            }
        }
        return reAttendList;
    }


    /**
     * @param username
     * @Author JackWang [www.coder520.com]
     * @Date 2017/7/1 20:42
     * @Description 查询补签申请状态
     */
    @Override
    public List<ReAttend> listReAttend(String username) {
        List<ReAttend> list = reAttendMapper.selectReAttendRecord(username);
        return list;
    }
}
