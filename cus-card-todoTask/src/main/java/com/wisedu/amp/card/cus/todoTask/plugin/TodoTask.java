package com.wisedu.amp.card.cus.todoTask.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cus.todoTask.model.ConfigInfo;
import com.wisedu.amp.card.cus.todoTask.model.ResultUtil;
import com.wisedu.amp.card.cus.todoTask.model.TaskInfo;
import com.wisedu.amp.card.cus.todoTask.model.TaskInfoRequest;
import com.wisedu.amp.card.cus.todoTask.model.TaskInfoResponse;
import com.wisedu.amp.card.cus.todoTask.model.TaskJsonInfoResponse;
import com.wisedu.amp.card.cus.todoTask.model.TaskObjResponse;
import com.wisedu.casp.tdc.api.DubboTdcService;
import com.wisedu.casp.tdc.model.DubboModel;
import com.wisedu.casp.tdc.model.DubboReadRequest;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;

/**
 * @author kaisir
 */
@MinosSPI
public class TodoTask extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(TodoTask.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat(ResultUtil.YYYYMMDDHHMMSS);
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long FIFTEEN_IN_MILLIS = 15 * 60 * 1000;
    /***
     * 移动端任务中心待办地址url
     * @author jszhang
     */
    private static final String TASK_CENTER_MOBILE_TODO_TASK_URL_SUFFIX="/taskCenter/*default/mobile.do#/TodoTask";

    @Override
    public String getCardId() {
        return "CUS_CARD_TODOTASK";
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
            case "getMarkNumber":
                result = this.getMarkNumber();
                break;
            case "updateTaskReadStatus":
                result= this.updateTaskReadStatus(request);
                break;
        }
        return result;
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        String taskSource = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("taskSource");
        if ("1".equals(taskSource)) {
            ComponentContainer container = cardConfigReq.getComponentContainer();
            List<AbstractComponent> components = container.getComponents();
            for (AbstractComponent component : components) {
                if ("itemConfigure".equals(component.getKey())) {
                    JSONArray array =  JSONArray.parseArray(JSON.toJSONString(component.getDatas()));
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        if ("scene".equals(object.getString("value"))) {
                            array.remove(i);
                            break;
                        }
                    }
                    container.setData("itemConfigure", Global.ComponentParam.DATAS, array);
                }
            }
        }
        return super.getConfig(cardConfigReq);
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
//        UserInfo user = new UserInfo();
//        user.setUserAccount("ampadmin");
        //获取amp接口url
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.TASKCENTER_URL);

        Map<String, Object> t = new HashMap<>();
        if (null == user) {
            logger.error("待办任务列表出错：用户获取失败！");
            t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
            t.put(ResultUtil.TASKINFO, new ArrayList<TaskInfo>());

            t.put(ResultUtil.CONFIG, config);
            t.put(ResultUtil.CARDWID, request.getCardWid());
            t.put(ResultUtil.CARDID, request.getCardId());
            return t;
        }
        List<TaskInfo> taskList = new ArrayList<>();
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        try {
            //查询任务来源和数量列表
            if (!isMobileDevice &&  null != config.getSourceList() && ResultUtil.RadioFlag.YES.getIndex().equals(config.getSourceList().toString())) {
                List<TaskInfo> sourceList = getSourceList(taskCenterUrl, user);
                t.put(ResultUtil.SOURCELIST, sourceList);
                if (CollectionUtils.isNotEmpty(sourceList) && StringUtils.isEmpty(sourceWid)) {
                    sourceWid = sourceList.get(0).getWID();
                }
            } else {
                t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
            }
            //如果参数为空则展示默认的第一条数据的参数
            taskList = getTaskList(taskCenterUrl, user, config, sourceWid);
        } catch (Exception e) {
            logger.error("待办任务列表出错：接口查询异常！", e);
            t.put(ResultUtil.SOURCELIST, new ArrayList<TaskInfo>());
        }
        t.put(ResultUtil.TASKINFO, taskList);

        t.put(ResultUtil.CONFIG, config);
        t.put(ResultUtil.CARDWID, request.getCardWid());
        t.put(ResultUtil.CARDID, request.getCardId());
        if (isMobileDevice) {
            String url = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("taskcenter_url")
                + TASK_CENTER_MOBILE_TODO_TASK_URL_SUFFIX;
            t.put(ResultUtil.TASKCENTER_URL, url);
        }
        t.put("taskSource",ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("taskSource"));
        return t;
    }

    private List<TaskInfo> getSourceList(String taskCenterUrl, UserInfo user) {
        String path = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.SOURCE_LIST_URL.getIndex());
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(user.getUserAccount());
        taskInfoRequest.setFlag(ResultUtil.TaskType.TODO.getIndex());
        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        HttpEntity entity = new HttpEntity(taskInfoRequest);
        List<TaskInfo> taskList = new ArrayList<>();
        try {
            TaskObjResponse response = RestTemplateUtils.post(taskCenterUrl + path, entity, TaskObjResponse.class).getBody();
            //处理数据返回最後的list传给前端
            taskList = handleSourceListData(response);
        } catch (Exception e) {
            //异常返回空列表
            logger.error("待办任务查询任务来源列表出错：", e);
        }
        return taskList;
    }

    private List<TaskInfo> getTaskList(String taskCenterUrl, UserInfo user, ConfigInfo config, String sourceWid) {
        String path = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.TODO_TASK_LIST_URL.getIndex());
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(user.getUserAccount());
        taskInfoRequest.setFlag(ResultUtil.TaskType.TODO.getIndex());
        taskInfoRequest.setPageSize(config.getRows());
        taskInfoRequest.setSourceWid(sourceWid);
        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        HttpEntity entity = new HttpEntity(taskInfoRequest);

        List<TaskInfo> taskList = new ArrayList<>();
        try {
            TaskJsonInfoResponse response = RestTemplateUtils.post(taskCenterUrl + path, entity, TaskJsonInfoResponse.class).getBody();
            //取最後的list传给前端
            taskList = handleTaskListData(response);
        } catch (Exception e) {
            //异常返回空列表
            logger.error("待办任务查询任务列表出错：", e);
        }
        //时间格式处理 及 taskId
        taskList = handleDateStr(taskList, config);

        return taskList;
    }

    private List<TaskInfo> handleDateStr(List<TaskInfo> taskList, ConfigInfo config) {
        Date date = CommonUtil.getSystemDate();
        for (int i = 0; i < taskList.size(); i++) {
            //判断部门是否有数据
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

                    createDate=getCreateDate(c,createDate,valueStr,cTwo,cThree);

                    taskList.get(i).setIsNew(handerNewTag(valueStr, config));

                    taskList.get(i).setCreateTime(createDate);
                } catch (Exception e) {
                    logger.error("已办任务任务列表处理时间格式出错：", e);
                }

            }
            taskList.get(i).setCreateTime(createDate);
            String wid = taskList.get(i).getWID();
            if ( StringUtil.isNotBlank(wid) ) {
                taskList.get(i).setTaskId(wid);
            }
        }
        return taskList;
    }

    private int handerNewTag(long value, ConfigInfo configInfo){
        int tag = 0;
        if(null != configInfo.getNewsRules()){
            tag = (value <= configInfo.getNewsRules().intValue() * 60 * 60 * 1000) ? 1 : 0;
        }
        return tag;
    }

    private String getCreateDate(Calendar c,String createDate,long valueStr,Calendar cTwo,Calendar cThree){
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


    private Integer getMarkNumber() {
        String path = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.TODO_TASK_COUNT_URL.getIndex());
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.TASKCENTER_URL);
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (null == user) {
            logger.error("待办任务数量查询出错：用户获取失败！");
            return 0;
        }
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(user.getUserAccount());
        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        taskInfoRequest.setFlag(ResultUtil.TaskType.TODO.getIndex());
        HttpEntity entity = new HttpEntity(taskInfoRequest);
        int count = ResultUtil.ZERO;
        try {
            TaskInfoResponse response = RestTemplateUtils.post(taskCenterUrl + path, entity, TaskInfoResponse.class).getBody();
            if (ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
                count = response.getData().getTodoCount();
            }
        } catch (Exception e) {
            logger.error("待办任务查询待办数量出错：", e);
        }
        return count;
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
            Map<String, Object> queryTodoList = response.getData().get(ResultUtil.QUERY_TODO_TASK);
            Object jsonList = queryTodoList.get(ResultUtil.TASKDATA);
            taskList = JSON.parseArray(JSON.toJSONString(jsonList), TaskInfo.class);
        }
        return taskList;
    }

    /*
     *处理任务类型列表返回数据
     */
    private List<TaskInfo> handleSourceListData(TaskObjResponse response) {
        List<TaskInfo> taskList = new ArrayList<>();
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

    /**
     * 更新任务已读状态
     * @param request
     * @return
     */
    private Object updateTaskReadStatus(CardAjaxRequest request){
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
//        currentUser = new UserInfo();
//        currentUser.setUserAccount("admin");
        if(null==currentUser){
            throw new NoLoginException();
        }
        Map<String, String> param = request.getParam();
        //任务id
        String  taskId=null!=param.get("taskId")?param.get("taskId"):null;
        //忽略 0取消忽略 1忽略
        Integer isRead=null!=param.get("isRead")?Integer.valueOf(param.get("isRead")):null;
        if(StringUtil.isEmpty(taskId)||null==isRead){
            return ResultData.error("待办任务列表已读/取消已读接口传参错误！");
        }
        CardDubboUtil cardDubboUtil = ApplicationContextUtil.get(CardDubboUtil.class);
        DubboTdcService dubboTdcService = cardDubboUtil.getDubboTdcService();
        DubboReadRequest dubboUpdateReq = new DubboReadRequest();
        dubboUpdateReq.setUserId(currentUser.getUserAccount());
        dubboUpdateReq.setIsRead(isRead);
        dubboUpdateReq.setTaskWid(taskId);
        DubboModel dubboModel=null;
        try {
            dubboModel = dubboTdcService.updateTaskReadStatus(dubboUpdateReq);
        } catch (Exception e) {
            logger.info("待办任务列表已读/取消已读接口请求发生异常",e);
        }
        if(null!=dubboModel&&"0".equals(dubboModel.getErrcode())){
            return "success";
        }
        throw new BusinessException("处理异常");
    }
}
