package com.wisedu.amp.card.cus.cucmyTask.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cus.cucmyTask.model.*;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@MinosSPI
public class MyTask extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(MyTask.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat(ResultUtil.YYYYMMDDHHMMSS);
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long FIFTEEN_IN_MILLIS = 15 * 60 * 1000;
    /***
     * 任务中心待办地址url
     * @author jszhang
     */
    private static final String TASK_CENTER_MOBILE_MY_TASK_URL_SUFFIX="/taskCenter/*default/mobile.do#/ProcessTrack";
    @Override
    public String getCardId() {
        return "CUS_CARD_CUSMYTASK";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "render":
                result = this.view(request);
                break;
            case "config":
                result = this.config(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }

        return result;
    }


    private Map<String, Object> view(CardAjaxRequest request) {
        logger.debug("doAjax..");
        //获取传参
        String sourceWid = "";
        Map<String, String> param = request.getParam();
        if (null != param && null != param.get(ResultUtil.SOURCEWID)) {
            sourceWid = param.get(ResultUtil.SOURCEWID);
        }
        //获取后台配置配置
        ConfigInfo config = getTodoTaskConfig(request);
        //获取用户信息
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        boolean mobile = param != null && param.containsKey("mobile") && "1".equals(param.get("mobile"));

//        UserInfo user = new UserInfo();
//        user.setUserAccount("ampadmin");
        //获取amp接口url
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.TASKCENTER_URL);

        Map<String, Object> t = new HashMap<>();
        if (null == user) {
            logger.error("我发起的任务列表出错：用户获取失败！");
            t.put(ResultUtil.TASKINFO, new ArrayList<TaskInfo>());
            t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
            t.put(ResultUtil.CONFIG, config);
            t.put(ResultUtil.CARDWID, request.getCardWid());
            t.put(ResultUtil.CARDID, request.getCardId());
            return t;
        }
        List<TaskInfo> taskList = new ArrayList<>();
        try {
            //查询任务来源和数量列表
            if (null != config.getSourceList() && !ResultUtil.RadioFlag.NO.getIndex().equals(config.getSourceList().toString())) {
                List<TaskInfo> sourceList = getSourceList(taskCenterUrl, user, String.valueOf(config.getSourceList()));
                t.put(ResultUtil.SOURCELIST, sourceList);
                if (CollectionUtils.isNotEmpty(sourceList) && StringUtils.isEmpty(sourceWid)) {
                    sourceWid = sourceList.get(0).getWID();
                }
            } else {
                t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
            }
            //如果参数为空则展示默认的第一条数据的参数
            taskList = getTaskList(taskCenterUrl, user, config, sourceWid, mobile);
        } catch (Exception e) {
            logger.error("我发起的任务列表出错：接口查询异常！", e);
            t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
        }
        if (mobile) {
            String url = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("taskcenter_url")
                    + TASK_CENTER_MOBILE_MY_TASK_URL_SUFFIX;
            t.put(ResultUtil.TASKCENTER_URL, url);
        }
        t.put(ResultUtil.TASKINFO, taskList);
        t.put("taskSource",ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("taskSource"));
        t.put(ResultUtil.CONFIG, config);
        t.put(ResultUtil.CARDWID, request.getCardWid());
        t.put(ResultUtil.CARDID, request.getCardId());

        return t;
    }

    private List<TaskInfo> getSourceList(String taskCenterUrl, UserInfo user, String sourceType) {
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(user.getUserAccount());
        taskInfoRequest.setFlag(ResultUtil.TaskType.MYTASK.getIndex());
        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        taskInfoRequest.setProcessClassifyType(sourceType);
        HttpEntity entity = new HttpEntity(taskInfoRequest);

        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        String sourceListUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.SOURCE_LIST_URL.getIndex());
        try {
            logger.debug("开始调用我发起的任务分类列表数据接口,url=" + taskCenterUrl + sourceListUrl, "参数=" + JSON.toJSONString(entity));
            TaskObjResponse response = RestTemplateUtils.post(taskCenterUrl + sourceListUrl, entity, TaskObjResponse.class).getBody();
            logger.debug("我发起的任务分类列表接口返回结果..." + JSON.toJSONString(response));
            //处理数据返回最後的list传给前端
            taskList = handleSourceListData(response);
        } catch (Exception e) {
            //异常返回空列表
            logger.error("调用我发起的任务列表数据接口失败,返回信息..." + e);
        }
        return taskList;
    }

    private List<TaskInfo> getTaskList(String taskCenterUrl, UserInfo user, ConfigInfo config, String sourceWid, boolean mobile) {
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(user.getUserAccount());
        taskInfoRequest.setFlag(ResultUtil.TaskType.MYTASK.getIndex());
        if(mobile) {
            taskInfoRequest.setPageSize(99);
        } else {
            taskInfoRequest.setPageSize(config.getRows());
            taskInfoRequest.setSourceWid(sourceWid);
            if ( null != config.getSourceList() ) {
                taskInfoRequest.setProcessClassifyType(String.valueOf(config.getSourceList()));
            }
        }

        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        HttpEntity entity = new HttpEntity(taskInfoRequest);

        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        String taskListUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.MY_TASK_LIST_URL.getIndex());
        try {
            logger.debug("开始调用我发起的任务数据接口...,url=" + taskCenterUrl + taskListUrl + "参数=" + JSON.toJSONString(entity));
            TaskJsonInfoResponse response = RestTemplateUtils.post(taskCenterUrl + taskListUrl, entity, TaskJsonInfoResponse.class).getBody();
            logger.debug("我发起的任务数据接口返回结果==>" + JSON.toJSONString(response));
            //取最後的list传给前端
            taskList = handleTaskListData(response);
        } catch (Exception e) {
            //异常返回空列表
            logger.error("调用我发起的任务数据接口失败...,返回信息" + e);
        }
        //时间格式处理
        taskList = handleDateStr(taskList);

        return taskList;
    }

    private List<TaskInfo> handleDateStr(List<TaskInfo> taskList) {
        Date date = CommonUtil.getSystemDate();
        for (int i = 0; i < taskList.size(); i++) {
            //判断部门是否有数据
            if (StringUtils.isEmpty(taskList.get(i).getCreatedByDepts())) {
                taskList.get(i).setCreatedByDepts("-");
            }
            String createDate = taskList.get(i).getCreateTime();
            if (StringUtil.isNotEmpty(createDate)) {
                try {
                    //计算相差多少时间
                    Date newCreateDate = sdf.parse(createDate);
                    long nowDateStr = date.getTime();
                    long newCreateDateStr = newCreateDate.getTime();
                    long valueStr = nowDateStr - newCreateDateStr;
                    //获取日期和前一天
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    //当前数据的时间
                    Calendar cTwo = Calendar.getInstance();
                    cTwo.setTime(newCreateDate);
                    //昨天
                    Calendar cThree = Calendar.getInstance();
                    cThree.setTime(date);
                    int day = cThree.get(Calendar.DATE);
                    cThree.set(Calendar.DATE, day - 1);
                    createDate = getCreateDate(createDate, valueStr, c, cTwo, cThree);
                } catch (Exception e) {
                    logger.error("已办任务任务列表处理时间格式出错：", e);
                }

            }
            taskList.get(i).setCreateTime(createDate);
        }
        return taskList;
    }

    private String getCreateDate(String createDate, long valueStr, Calendar c, Calendar cTwo, Calendar cThree) {
        if (c.get(Calendar.YEAR) == cTwo.get(Calendar.YEAR) && c.get(Calendar.MONTH) == cTwo.get(Calendar.MONTH)) {
            if (valueStr < FIFTEEN_IN_MILLIS) {
                //小于15分钟
                createDate = ResultUtil.DateShowType.EXACTLY.getName();
            } else if (valueStr < HOUR_IN_MILLIS) {
                //多少分钟前
                createDate = String.valueOf(valueStr / (1000 * 60)) + ResultUtil.DateShowType.MINUTES_AGO.getName();
            } else if (c.get(Calendar.DATE) == cTwo.get(Calendar.DATE)) {
                //几小时前
                createDate = String.valueOf(valueStr / (1000 * 60 * 60)) + ResultUtil.DateShowType.HOURS_AGO.getName();
            } else if (cTwo.get(Calendar.DATE) == cThree.get(Calendar.DATE)) {
                //昨天
                createDate = ResultUtil.DateShowType.YESTERDAY.getName();
            } else {
                createDate = createDate.replaceAll("-", "/").substring(0, 16);
            }
        } else {
            createDate = createDate.replaceAll("-", "/").substring(0, 16);
        }
        return createDate;
    }

    private ConfigInfo getTodoTaskConfig(CardAjaxRequest request) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        ConfigInfo config = JSONObject.parseObject(configInfo, ConfigInfo.class);
        if (null == config) {
            config = new ConfigInfo();
        }
        return config;
    }

    private String config(CardAjaxRequest request) {
        logger.debug("doConfig..");

        Template t;
        if (StringUtils.isEmpty(request.getTarget())) {
            t = getGroupTemplate().getTemplate("/" + this.getCardId() + "/config.html");
        } else {
            t = getGroupTemplate().getAjaxTemplate("/" + this.getCardId() + "/config.html", request.getTarget());
        }
        //查询配置信息
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo = "";
        if (null != request.getParam() && null != request.getParam().get("config")) {
            configInfo = request.getParam().get("config");
        } else {
            configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
                configInfo = JSON.parse(configInfo).toString();
            }
        }
        ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
        if (null == config) {
            config = new ConfigInfo();
        }
        //设置多少条
        t.binding("config", config);
        //设置卡片高度
        //设置复选框

        t.binding("cardId", getCardId());
        t.binding("cardWid", request.getCardWid());
        String str = t.render();
        return str;
    }

    /*
     *处理config初始化的复选框值
     */
    private void handleCheckBoxData(Template t, ConfigInfo config) {

    }

    /*
     *处理任务列表返回数据
     */
    private List<TaskInfo> handleTaskListData(TaskJsonInfoResponse response) {
        List<TaskInfo> taskList = new ArrayList<>();
        if (ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
            Map<String, Object> queryMyList = response.getData().get("queryProcessTrack");
            Object jsonList = queryMyList.get("taskData");
            taskList = JSON.parseArray(JSON.toJSONString(jsonList), TaskInfo.class);
            //处理当前处理人
            for (TaskInfo taskInfo : taskList) {
                String assignNames = taskInfo.getAssignNames();
                // 3.2.1.SP2 增加当前处理人部门
                String assignDepts = taskInfo.getAssignDepts();
                if (StringUtil.isNotEmpty(assignNames)) {
                    assignNames = assignNames.replaceAll(",", "、");
                }
                if(StringUtil.isNotEmpty(assignDepts)){
                    assignDepts = assignDepts.replaceAll(",", "、");
                }
                taskInfo.setAssignNames(assignNames);
                taskInfo.setAssignDepts(assignDepts);
            }
        }
        return taskList;
    }

    /*
     *处理任务类型列表返回数据
     */
    private List<TaskInfo> handleSourceListData(TaskObjResponse response) {
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        if (ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
            Object jsonList = response.getData();
            taskList = JSON.parseArray(JSON.toJSONString(jsonList), TaskInfo.class);
            if (CollectionUtils.isNotEmpty(taskList)) {
                taskList.remove(ResultUtil.ZERO);
                //循环处理count
                for (TaskInfo taskInfo : taskList) {
                    if (Integer.parseInt(taskInfo.getCOUNT()) > 99) {
                        taskInfo.setCOUNT(ResultUtil.NINETY_NINE);
                    }
                }
            }
        }
        return taskList;
    }

    /*
     *处理config初始化的单选框值
     */
    private void handleRadioData(Template t, ConfigInfo configInfo, String cardWid) {

    }
}
