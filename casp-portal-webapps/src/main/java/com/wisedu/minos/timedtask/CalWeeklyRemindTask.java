package com.wisedu.minos.timedtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.model.DubboResponse;
import com.wisedu.casp.cal.model.*;
import com.wisedu.minos.api.config.SystemToolsService;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.DateUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PageCardConfigEntity;
import com.wisedu.minos.casp.portal.dao.entity.UserCalCardRelEntity;
import com.wisedu.minos.casp.portal.dao.mapper.UserCalCardRelMapper;
import com.wisedu.minos.casp.portal.service.impl.PageCardConfigService;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.scheduler.job.BaseJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述：日程周提醒定时任务
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CalWeeklyRemindTask
 * @Author: 01319098
 * @Date: 2022/3/10
 */
@Component
@DisallowConcurrentExecution
public class CalWeeklyRemindTask implements BaseJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalWeeklyRemindTask.class);
    @Autowired
    UserCalCardRelMapper userCalCardRelMapper;
    @Autowired
    CardUtil cardUtil;
    @Autowired
    PageCardConfigService pageCardConfigService;
    @DubboReference
    private UserCalDubboService userCalDubboService;
    @DubboReference
    private SystemToolsService systemToolsService;
    @DubboReference
    private UserService userService;
    public static final String CARD_ID = "SYS_CARD_CALENDAR";

    @Override
    public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.debug("=======>>>开始执行日程周提醒定时任务");
        try {
            // 1.删除当前已经不存在的卡片Wid数据
            Set<String> delCardIds = pageCardConfigService.list(Wrappers.<PageCardConfigEntity>lambdaQuery().eq(PageCardConfigEntity::getIsDeleted, Global.DeleteStatus.YES.getId()))
                    .stream().map(PageCardConfigEntity::getCardWid).collect(Collectors.toSet());
            if ( CollectionUtils.isNotEmpty(delCardIds) ) {
                userCalCardRelMapper.delete(Wrappers.<UserCalCardRelEntity>lambdaQuery().in(UserCalCardRelEntity::getCardWid, delCardIds));
            }
            // 2.查询用户日程关联表，查出当天哪些用户需要进行提醒
            LocalDateTime now = LocalDateTime.now();
            int week = now.getDayOfWeek().getValue();
            List<UserCalCardRelEntity> userCals = userCalCardRelMapper.queryUserCalList(String.valueOf(week), String.valueOf(now.getHour()));
            List<String> userIds = userCals.stream().map(UserCalCardRelEntity::getUserId).collect(Collectors.toList());
            Set<String> cardWids = userCals.stream().map(UserCalCardRelEntity::getCardWid).collect(Collectors.toSet());
            if ( CollectionUtils.isEmpty(userCals) ) {
                return;
            }

            // 查询卡片配置
            Map<String, List<String>> cardConfigMap = Maps.newHashMap();
            cardWids.forEach(card -> {
                String config = cardUtil.getCardConfigByCardWid(card, CARD_ID);
                if ( StringUtil.isNotEmpty(config) ) {
                    config = JSON.parse(config).toString();
                    JSONObject value = JSON.parseObject(config);
                    Object addCalendar = value.get("addCalendar");
                    List<String> selectList = new ArrayList<>();
                    if ( addCalendar instanceof List && ! (( List ) addCalendar).isEmpty() ) {
                        (( JSONArray ) addCalendar).forEach(e -> {
                            String id = (( JSONObject ) e).getString("id");
                            selectList.add(id);
                        });
                    }
                    cardConfigMap.put(card, selectList);
                }
            });

            // 获取用户信息
            DubboIds dubboIds = new DubboIds();
            dubboIds.setWid(userIds);
            Map<String, DubboUserInfo> userMap = Maps.newHashMap();
            try {
                DubboUserInfoResp resp = userService.getUserByIds(dubboIds);
                userMap = resp.getData().stream().collect(Collectors.toMap(DubboUserInfo::getWid, e -> e));
            } catch ( Exception e ) {
                LOGGER.info("调用getUserByIds获取用户详情失败，原因为：", e);
            }

            // 获取下周一的时间
            String startTime = DateUtil.getStrFromDate(DateUtil.getNextWeekStart(), DateUtil.DATE_FORMATE_STRING_A);
            String endTime = DateUtil.getStrFromDate(DateUtil.getNextWeekEnd(), DateUtil.DATE_FORMATE_STRING_A);

            // 3.查询用户下周需要提醒的日程信息
            UserCalReq dubboReq = new UserCalReq();
            dubboReq.setStartTime(startTime);
            dubboReq.setEndTime(endTime);
            dubboReq.setUserIdList(userIds);
            DubboResponse<List<DubboUserCalInfo>> dubboResponse = userCalDubboService.getScheduleWithUserList(dubboReq);
            if ( null != dubboResponse && "0".equals(dubboResponse.getErrcode()) ) {
                List<DubboUserCalInfo> data = dubboResponse.getData();
                if ( CollectionUtils.isEmpty(data) ) {
                    return;
                }
                Map<String, List<CalendarWithEventInfo>> userCalMap = data.stream().collect(Collectors.toMap(DubboUserCalInfo::getUserId, e -> e.getCalendarList()));
                // 4.发送消息日程
                Map<String, DubboUserInfo> finalUserMap = userMap;
                userCals.forEach(cal -> {
                    DubboUserInfo user = finalUserMap.get(cal.getUserId());
                    if ( ObjectUtils.isEmpty(user) ) {
                        return;
                    }
                    List<CalendarWithEventInfo> calInfoList = userCalMap.get(cal.getUserId());
                    if ( CollectionUtils.isEmpty(calInfoList) ) {
                        return;
                    }
                    // 获取当前卡片配置信息
                    List<String> calList = cardConfigMap.get(cal.getCardWid());

                    if ( null == calList ) {
                        return;
                    }
                    Optional<CalendarWithEventInfo> first = calInfoList.stream().filter(f -> "1".equals(f.getWid())).findFirst();
                    first.ifPresent(calendarInfo -> calList.add(0, calendarInfo.getWid()));
                    calInfoList = calInfoList.stream().filter(i -> calList.contains(i.getWid())).collect(Collectors.toList());
                    // 查询日程信息
                    List<DubboEventInfo> eventInfoList = Lists.newArrayList();
                    calInfoList.forEach(i -> {
                        eventInfoList.addAll(i.getEventInfoList());
                    });

                    // 拼装提醒内容
                    StringBuilder commonContent = new StringBuilder();
                    StringBuilder htmlContent = new StringBuilder();
                    String title = DateUtil.getStrFromDate(DateUtil.getNextWeekStart(), DateUtil.DATE_FORMATE_STRING_C) + " - " + DateUtil.getStrFromDate(DateUtil.getNextWeekEnd(), DateUtil.DATE_FORMATE_STRING_C);
                    if ( CollectionUtils.isNotEmpty(eventInfoList) ) {
                        // 按开始时间排序
                        List<DubboEventInfo> collect = eventInfoList.stream().sorted(Comparator.comparing(l -> DateUtil.getDateFromStr(l.getStartTime(), "yyyy-MM-dd HH:mm:ss"), Comparator.nullsLast(Date::compareTo))).collect(Collectors.toList());

                        for ( DubboEventInfo event : collect ) {
                            String start = DateUtil.getStrFromDate(DateUtil.getDateFromStr(event.getStartTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy/MM/dd HH:mm:ss");
                            String end = DateUtil.getStrFromDate(DateUtil.getDateFromStr(event.getEndTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy/MM/dd HH:mm:ss");
                            commonContent.append(start);
                            htmlContent.append(start);
                            if ( StringUtil.isNotEmpty(end) ) {
                                commonContent.append(" - ").append(end);
                                htmlContent.append(" - ").append(end);
                            }
                            if ( StringUtil.isNotEmpty(event.getEventTitle()) ) {
                                commonContent.append("\n").append(event.getEventTitle());
                                htmlContent.append("<br>").append(event.getEventTitle());
                            }
                            if ( StringUtil.isNotEmpty(event.getEventDesc()) ) {
                                commonContent.append("\n").append(event.getEventDesc());
                                htmlContent.append("<br>").append(event.getEventDesc());
                            }
                            commonContent.append("\n\n");
                            htmlContent.append("<br><br>");
                        }
                        // 发送邮件
                        sendRemindMsg(cal, title, commonContent.toString(), htmlContent.toString(), user);
                    }
                });

            }
        } catch ( Exception e ) {
            LOGGER.error("=======>>>执行获取日程周提醒定时任务发生异常:", e);
        }
    }

    private void sendRemindMsg (UserCalCardRelEntity entity, String title, String content, String htmlContent, DubboUserInfo user) {
        //发送消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setSubject(title);

        //设置提醒的通道
        String remindChannel = entity.getRemindWay();
        List<String> types = Arrays.asList(remindChannel.split(","));
        List<String> htmlTypes = Arrays.asList("INBOX", "EMAIL");
        List<String> commonTypes = types.stream().filter(i -> ! htmlTypes.contains(i)).collect(Collectors.toList());
        if ( types.contains("INBOX") ) {
            sendRemindMsgDetail(messageInfo, Arrays.asList("INBOX"), htmlContent, user);
        }
        if ( types.contains("EMAIL") ) {
            sendEmailMsg(user, htmlContent, title);
        }
        if ( CollectionUtils.isNotEmpty(commonTypes) ) {
            sendRemindMsgDetail(messageInfo, commonTypes, content, user);
        }
    }

    private void sendRemindMsgDetail (MessageInfo messageInfo, List<String> types, String content, DubboUserInfo user) {
        //发送消息
        messageInfo.setTypes(types);
        messageInfo.setContent(content);
        //设置提醒人信息
        Receiver receiver = new Receiver();
        receiver.setUserName(user.getUserName());
        receiver.setEmail(user.getEmail());
        receiver.setPhone(user.getPhone());
        receiver.setUserAccount(user.getUserAccount());
        messageInfo.setReceiver(receiver);
        Map<String, String> ext = new HashMap<>();
        ext.put("appId", "casp.portal");
        ext.put("appName", "门户");
        messageInfo.setExt(ext);
        //发送提醒
        boolean sendMessage = systemToolsService.sendMessage(messageInfo);
        if ( ! sendMessage ) {
            LOGGER.error("发送日程周提醒消息失败：{}", messageInfo.toString());
        }
    }

    /**
     * 发送邮件
     */
    private void sendEmailMsg (DubboUserInfo user, String content, String title) {
        SynMessageInfo message = new SynMessageInfo();
        String channelId = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("mailSendChannelId");
        String instanceId = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("mailSendInstanceWid");
        Receiver receiver = new Receiver();
        receiver.setEmail(user.getEmail());
        message.setContent(content);
        message.setReceiver(receiver);
        message.setChannelId(channelId);
        message.setInstanceWid(instanceId);
        message.setSubject(title);
        MessageSynResponse response = systemToolsService.synSendMessage(message);
        if ( ! Global.CONSTANT_NO.equals(response.getCode()) ) {
            // 消息中心发送消息出错,直接返回错误信息
            LOGGER.error("通过邮件发送日程周提醒消息失败：{}", response.getMsg());
        }
    }
}
