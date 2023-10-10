package com.wisedu.minos.casp.portal.service.cal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wisedu.casp.cal.api.UserEventModifyDubboService;
import com.wisedu.casp.cal.model.DubboEventModifyInfo;
import com.wisedu.casp.cal.model.DubboResponse;
import com.wisedu.casp.cal.model.calgroup.DubboGroupEventModifyInfo;
import com.wisedu.casp.cal.model.calgroup.DubboSimpleCalAuthObj;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 日程相关操作
 * @date 2022/8/24 13:29
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Component
public class CalEventService {

    public  Object create(CardAjaxRequest request) {
        Map<String, String> param = request.getParam();
        DubboResponse response = checkEventData(param);
        if (null != response) {
            return response;
        }

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        //默认日历是个人日历
        String calendarId = "1";
        if (StringUtils.isNotBlank(param.get("calendarId"))) {
            calendarId = param.get("calendarId");
        }
        DubboResponse res = new DubboResponse();
        if ("1".equals(calendarId)) {
            DubboEventModifyInfo eventInfo = new DubboEventModifyInfo();
            fillEventInfo(eventInfo, param, user);

            //删除redis缓存
            UserEventModifyDubboService userEventModifyDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserEventModifyDubboService();
            res = userEventModifyDubboService.addEvent(eventInfo);
        } else if ("2".equals(calendarId)) {
            //群组日历
            DubboGroupEventModifyInfo eventInfo = new DubboGroupEventModifyInfo();
            fillGroupEventInfo(eventInfo, param, user);

            //删除redis缓存
            UserEventModifyDubboService userEventModifyDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserEventModifyDubboService();
            res = userEventModifyDubboService.addGroupEvent(eventInfo);
        }
        ApplicationContextUtil.get(CardDubboUtil.class).removeRedisData(request);

        return res;
    }
    public Object modify(CardAjaxRequest request) {
        Map<String, String> param = request.getParam();
        DubboResponse response = checkEventEdit(param);
        if (null != response) {
            return response;
        }
        String calendarId =param.get("calendarId");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if ("1".equals(calendarId)) {

            DubboEventModifyInfo eventInfo = new DubboEventModifyInfo();
            eventInfo.setWid(param.get("wid"));
            fillEventInfo(eventInfo, param, user);
            eventInfo.setModifyDay(param.get("modifyDay"));
            eventInfo.setModifyType(Integer.parseInt(param.get("modifyType")));
            //删除redis缓存
            UserEventModifyDubboService userEventModifyDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserEventModifyDubboService();
            ApplicationContextUtil.get(CardDubboUtil.class).removeRedisData(request);

            return userEventModifyDubboService.editEvent(eventInfo);
        } else if ("2".equals(calendarId)) {
            //群组日历
            DubboGroupEventModifyInfo eventInfo = new DubboGroupEventModifyInfo();
            eventInfo.setWid(param.get("wid"));
            fillGroupEventInfo(eventInfo, param, user);
            eventInfo.setModifyDay(param.get("modifyDay"));
            eventInfo.setModifyType(Integer.parseInt(param.get("modifyType")));
            ApplicationContextUtil.get(CardDubboUtil.class).removeRedisData(request);

            //删除redis缓存
            UserEventModifyDubboService userEventModifyDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserEventModifyDubboService();
            return userEventModifyDubboService.editGroupEvent(eventInfo);
        }
        return null;

    }
    private DubboResponse checkEventData(Map<String, String> param) {
        DubboResponse response = new DubboResponse();
        if (param == null) {
            response.setErrcode("381001");
            response.setErrmsg("传入的param参数不能为空");
            return response;
        }
        if (StringUtils.isEmpty(param.get("eventTitle"))) {
            response.setErrcode("381001");
            response.setErrmsg("日程标题eventTitle不能为空");
            return response;
        }
        if (StringUtils.isEmpty(param.get("startTime"))) {
            response.setErrcode("381001");
            response.setErrmsg("开始时间startTime不能为空");
            return response;
        }
        if (StringUtils.isEmpty(param.get("endTime"))) {
            response.setErrcode("381001");
            response.setErrmsg("结束时间endTime不能为空");
            return response;
        }
        if (StringUtils.isEmpty(param.get("eventRepeatType")) ||
                !StringUtils.isNumeric(param.get("eventRepeatType"))) {
            response.setErrcode("381001");
            response.setErrmsg("重复类型eventRepeatType不能为空且必须是数字");
            return response;
        }
        if (!"0".equals(param.get("isAllDay")) &&
                !"1".equals(param.get("isAllDay"))) {
            response.setErrcode("381002");
            response.setErrmsg("是否全天日程isAllDay必须是0或1");
            return response;
        }
        if (StringUtils.isNotEmpty(param.get("dayOfMonth")) &&
                !StringUtils.isNumeric(param.get("dayOfMonth"))) {
            response.setErrcode("381002");
            response.setErrmsg("每月第几天dayOfMonth必须是数字");
            return response;
        }
        if (StringUtils.isNotEmpty(param.get("rangeNum")) &&
                !StringUtils.isNumeric(param.get("rangeNum"))) {
            response.setErrcode("381002");
            response.setErrmsg("重复次数rangeNum必须是数字");
            return response;
        }
        return null;
    }

    private void fillEventInfo(DubboEventModifyInfo eventInfo, Map<String, String> param, UserInfo user) {
        eventInfo.setEventTitle(param.get("eventTitle"));
        eventInfo.setStartTime(param.get("startTime"));
        eventInfo.setEndTime(param.get("endTime"));
        eventInfo.setEventAddr(param.get("eventAddr"));
        eventInfo.setEventDesc(param.get("eventDesc"));
        eventInfo.setLinkUrl(param.get("linkUrl"));
        eventInfo.setEventRepeatType(Integer.parseInt(param.get("eventRepeatType")));
        if (StringUtils.isNotEmpty(param.get("eventRepeatData"))) {
            eventInfo.setEventRepeatData(Integer.parseInt(param.get("eventRepeatData")));
        }
        if (StringUtils.isNotEmpty(param.get("remindTimes"))) {
            String[] remindTimes = param.get("remindTimes").split(",");
            List<Long> reminds = new ArrayList<>();
            for (String remind : remindTimes) {
                reminds.add(Long.parseLong(remind));
            }
            eventInfo.setRemindTimes(reminds);
        } else {
            eventInfo.setRemindTimes(null);
        }
        eventInfo.setUserWid(null == user ? "" : user.getUserAccount());
        eventInfo.setRemindChannel(param.get("remindChannel"));
        eventInfo.setDeadline(param.get("deadLine"));
        eventInfo.setIsAllDay(Integer.parseInt(param.get("isAllDay")));
        if (StringUtils.isNotEmpty(param.get("dayOfMonth"))) {
            eventInfo.setDayOfMonth(Integer.parseInt(param.get("dayOfMonth")));
        }
        eventInfo.setDayOfWeek(param.get("dayOfWeek"));
        eventInfo.setWeekIndex(param.get("weekIndex"));
        eventInfo.setRangeType(param.get("rangeType"));
        if (StringUtils.isNotEmpty(param.get("rangeNum"))) {
            eventInfo.setRangeNum(Integer.parseInt(param.get("rangeNum")));
        }
        eventInfo.setEventType(2);
        eventInfo.setCalendarId("1");
    }
    private void fillGroupEventInfo(DubboGroupEventModifyInfo eventInfo, Map<String, String> param, UserInfo user) {
        eventInfo.setEventTitle(param.get("eventTitle"));
        eventInfo.setStartTime(param.get("startTime"));
        eventInfo.setEndTime(param.get("endTime"));
        eventInfo.setEventAddr(param.get("eventAddr"));
        eventInfo.setEventDesc(param.get("eventDesc"));
        eventInfo.setLinkUrl(param.get("linkUrl"));
        eventInfo.setEventRepeatType(Integer.parseInt(param.get("eventRepeatType")));
        if (StringUtils.isNotEmpty(param.get("eventRepeatData"))) {
            eventInfo.setEventRepeatData(Integer.parseInt(param.get("eventRepeatData")));
        }
        if (StringUtils.isNotEmpty(param.get("remindTimes"))) {
            String[] remindTimes = param.get("remindTimes").split(",");
            List<Long> reminds = new ArrayList<>();
            for (String remind : remindTimes) {
                reminds.add(Long.parseLong(remind));
            }
            eventInfo.setRemindTimes(reminds);
        } else {
            eventInfo.setRemindTimes(null);
        }
        eventInfo.setUserWid(null == user ? "" : user.getUserAccount());
        eventInfo.setRemindChannel(param.get("remindChannel"));
        eventInfo.setDeadline(param.get("deadLine"));
        eventInfo.setIsAllDay(Integer.parseInt(param.get("isAllDay")));
        if (StringUtils.isNotEmpty(param.get("dayOfMonth"))) {
            eventInfo.setDayOfMonth(Integer.parseInt(param.get("dayOfMonth")));
        }
        eventInfo.setDayOfWeek(param.get("dayOfWeek"));
        eventInfo.setWeekIndex(param.get("weekIndex"));
        eventInfo.setRangeType(param.get("rangeType"));
        if (StringUtils.isNotEmpty(param.get("rangeNum"))) {
            eventInfo.setRangeNum(Integer.parseInt(param.get("rangeNum")));
        }
        eventInfo.setEventType(3);
        eventInfo.setCalendarId("2");
        List<DubboSimpleCalAuthObj> obj = new Gson().fromJson(param.get("authObjs"), new TypeToken<List<DubboSimpleCalAuthObj>>() {
        }.getType());
        eventInfo.setAuthObjs(obj);

    }

    private DubboResponse checkEventEdit(Map<String, String> param) {
        DubboResponse response = checkEventData(param);
        if (null != response) {
            return response;
        }
        if (StringUtils.isEmpty(param.get("wid"))) {
            response.setErrcode("381001");
            response.setErrmsg("日程主键wid不能为空");
            return response;
        }
        if (StringUtils.isEmpty(param.get("modifyType")) && !"0".equals(param.get("modifyType")) &&
                !"1".equals(param.get("modifyType"))) {
            response.setErrcode("381002");
            response.setErrmsg("修改类型modifyType必须是0或1");
            return response;
        }
        if (StringUtils.isEmpty(param.get("modifyDay"))) {
            response.setErrcode("381001");
            response.setErrmsg("编辑日期modifyDay不能为空");
            return response;
        }
        return null;
    }

}
