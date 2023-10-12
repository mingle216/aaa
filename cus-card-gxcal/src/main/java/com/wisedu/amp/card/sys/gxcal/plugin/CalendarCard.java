package com.wisedu.amp.card.sys.gxcal.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.sys.gxcal.dto.CalendarDtoV2;
import com.wisedu.amp.card.sys.gxcal.enums.CalendarConsoleDto;
import com.wisedu.amp.card.sys.gxcal.enums.QueryModelEnum;
import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.api.UserCalDubboServiceV2;
import com.wisedu.casp.cal.model.*;
import com.wisedu.minos.api.config.SystemToolsService;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.dao.entity.UserCalCardRelEntity;
import com.wisedu.minos.casp.portal.dao.mapper.UserCalCardRelMapper;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.SchoolCalendarModel;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.AddCalendarComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.model.configcomponent.WeekRemindCalendarComponent;
import com.wisedu.minos.casp.portal.model.errcode.CalDubboExceptionEnum;
import com.wisedu.minos.casp.portal.model.errcode.DubboExceptionEnum;
import com.wisedu.minos.casp.portal.service.SchoolCalendarAdapter;
import com.wisedu.minos.casp.portal.service.impl.HomeService;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.service.impl.TemplateService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日程卡片
 *
 * @author jszhang@wisedu
 * @version 1.0
 * @date 2021/7/27 14:24
 **/
@MinosSPI
public class CalendarCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(CalendarCard.class);

    @Override
    public String getCardId() {
        return "CUS_CARD_GXCAL";
    }

    @Override
    public void destroy() {
        logger.debug("card[SYS_CARD_CAROUSEL] destroyed ...");

    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        if(!checkLogin() && !"renderData".equals(request.getMethod())
                && !"getPermissionCal".equals(request.getMethod()) && !"getWeek".equals(request.getMethod())){
            return result;
        }
        switch (request.getMethod()) {
            case "renderData":
                result = this.renderData(request);
                break;
            case "subscribe":
                result = this.subscribe(request);
                break;
            case "getPermissionCal":
                result = this.getPermissionCal(request);
                break;
            case "share":
                result = this.share(request);
                break;
            case "getWeek":
                result = this.getWeek(request);
                break;
            case "setWeeklyReminder":
                result = this.setWeekReminder(request);
                break;
            case "getWeeklyReminder":
                result = this.getWeeklyReminder(request);
                break;
            case "pullDingdingData":
                result = this.pullDingdingData(request);
                break;
            default:
                break;
        }
        return result;
    }

    private Object pullDingdingData(CardAjaxRequest request) {
        Map<String, String> param = request.getParam();
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        String isPublished = param.get("isPublished");
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        String userId = currentUser.getWid();

        UserCalDubboServiceV2 userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboServiceV2();
        UserCalSynchronizeReq userCalSynchronizeReq = new UserCalSynchronizeReq();
        userCalSynchronizeReq.setStartTime(startTime);
        userCalSynchronizeReq.setEndTime(endTime);
        userCalSynchronizeReq.setIsPublished(Integer.parseInt(isPublished));
        userCalSynchronizeReq.setUserWid(userId);
        DubboResponse response = userCalDubboService.synchronizeCal(userCalSynchronizeReq);
        if ("0".equals(response.getErrcode())) {
            return null;
        } else {
            String errcode;
            if (CalDubboExceptionEnum.CANT_PULL_DINGDING_AGAIN.getCode().equals(response.getErrcode())) {
                errcode = DubboExceptionEnum.CANT_PULL_DINGDING_AGAIN.getCode();
            } else if (CalDubboExceptionEnum.GLOBAL_DINGDING_NOT_ENABLE.getCode().equals(response.getErrcode())) {
                errcode = DubboExceptionEnum.GLOBAL_DINGDING_NOT_ENABLE.getCode();
            } else if (CalDubboExceptionEnum.SYNCHRONIZE_JOB_ALREADY_EXIST.getCode().equals(response.getErrcode())) {
                errcode = DubboExceptionEnum.SYNCHRONIZE_JOB_ALREADY_EXIST.getCode();
            } else {
                errcode = "-1";
            }
            throw new BusinessException(errcode, response.getErrmsg());
        }
    }

    private Object getWeek(CardAjaxRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = request.getParam();
        String selectDateString = param.get("selectDate");
        LocalDate selectDate;
        if (StringUtils.isBlank(selectDateString)) {
            selectDate = LocalDate.now();

        } else {
            selectDate = LocalDate.parse(selectDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        SchoolCalendarModel underInfo = ApplicationContextUtil.get(SchoolCalendarAdapter.class)
                .getProductUnderInfo(0, selectDate);
        //获取研究生校历信息
        SchoolCalendarModel postInfo = ApplicationContextUtil.get(SchoolCalendarAdapter.class)
                .getProductPostInfo(0, selectDate);
        result.put("underInfo", underInfo);
        result.put("postInfo", postInfo);
        return result;
    }

    private Object share(CardAjaxRequest request) {
        Map<String, String> param = request.getParam();
        String calId = param.get("calId");
        UserCalDubboService userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboService();
        ShareCalReq shareCalReq = new ShareCalReq();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String userId = currentUser == null ? "guest" : currentUser.getWid();
        shareCalReq.setCalWid(calId);
        shareCalReq.setUserWid(userId);
        DubboResponse response = userCalDubboService.getShareCalId(shareCalReq);
        if (response != null && response.getData() != null && StringUtils.isNotBlank((String) response.getData())) {
            return response.getData().toString();
        }
        return null;
    }

    /***
     * //todo 添加描述
     * @param request
     * @return java.lang.Object
     * @author jszhang
     * @date 2021/8/5 13:15
     */
    private Object getPermissionCal(CardAjaxRequest request) {
        Map<String, String> param = request.getParam();
        String paramLang = StringUtil.isNotEmpty(param.get("lang")) ? param.get("lang") : Global.DEFAULT_LANGUAGE;
        UserCalDubboService userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboService();
//        userCalDubboService.getCalList()
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        String userId = currentUser == null ? null : currentUser.getWid();
        String lang = currentUser == null ? paramLang : currentUser.getPreferredLanguage();

        Map<String, Object> sysConfig = getSysConfig(request);
        Object value = sysConfig.get("addCalendar");
        List<String> selectList = new ArrayList<>();
        if (value instanceof List && !((List) value).isEmpty()) {
            ((JSONArray) value).forEach(e -> {
                String id = ((JSONObject) e).getString("id");
                selectList.add(id);
            });
        }
        DubboResponse<List<CalendarInfo>> response;
        if (StringUtils.isBlank(userId)) {
            response = userCalDubboService.getCalList(QueryModelEnum.USER_NOT_LOGIN.getCode(), null, lang);
        } else {
            response = userCalDubboService.getCalList(QueryModelEnum.USER_PERMISSION.getCode(), userId, lang);
        }
        if ("0".equals(response.getErrcode())) {
            List<CalendarInfo> data = response.getData();
            List<CalendarInfo> res = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(data)) {

                selectList.forEach(e -> {
                    List<CalendarInfo> collect = data.stream().filter(f -> f.getWid().equals(e)).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        res.add(collect.get(0));
                    }
                });
                // 3.5.1.Beta1 注释 采用管控台配置的日历顺序
//                List<CalendarInfo> collect = res.stream().sorted(Comparator.comparing(CalendarInfo::getWid)).collect(Collectors.toList());
                Optional<CalendarInfo> first = data.stream().filter(f -> "1".equals(f.getWid())).findFirst();
                Optional<CalendarInfo> groupCal = data.stream().filter(f -> "2".equals(f.getWid())).findFirst();
                groupCal.ifPresent(calendarInfo -> res.add(0, calendarInfo));
                first.ifPresent(calendarInfo -> res.add(0, calendarInfo));

                return res;
            }
        }
        return Collections.emptyList();

    }

    private Object subscribe(CardAjaxRequest request) {
        UserCalDubboService userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboService();
        Map<String, String> param = request.getParam();
        String subscribe = param.get("subscribe");
        String unSubscribe = param.get("unSubscribe");
        List<SubscribeInfo> subscribeInfos = new ArrayList<>();
        if (StringUtils.isNotBlank(subscribe)) {
            List<SubscribeInfo> collect = Splitter.on(",")
                    .omitEmptyStrings().trimResults().splitToList(subscribe)
                    .stream().map(e -> {
                        SubscribeInfo subscribeInfo = new SubscribeInfo();
                        subscribeInfo.setCalWid(e);
                        subscribeInfo.setStatus("0");
                        return subscribeInfo;
                    }).collect(Collectors.toList());
            subscribeInfos.addAll(collect);

        }

        if (StringUtils.isNotBlank(unSubscribe)) {
            List<SubscribeInfo> collect = Splitter.on(",")
                    .omitEmptyStrings().trimResults().splitToList(unSubscribe)
                    .stream().map(e -> {
                        SubscribeInfo subscribeInfo = new SubscribeInfo();
                        subscribeInfo.setCalWid(e);
                        subscribeInfo.setStatus("1");
                        return subscribeInfo;
                    }).collect(Collectors.toList());
            subscribeInfos.addAll(collect);
        }
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        String userId = currentUser == null ? null : currentUser.getWid();
        SubscribeReq subscribeReq = new SubscribeReq();
        subscribeReq.setUserWid(userId);
        subscribeReq.setSubscribeInfos(subscribeInfos);
        userCalDubboService.subscribe(subscribeReq);
        return "success";
    }

    private Object renderData(CardAjaxRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = request.getParam();
        String paramLang = StringUtil.isNotEmpty(param.get("lang")) ? param.get("lang") : Global.DEFAULT_LANGUAGE;
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        UserCalDubboServiceV2 userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboServiceV2();
        UserCalReq userCalReq = new UserCalReq();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String userId = currentUser == null ? "guest" : currentUser.getWid();
        String lang = null != currentUser ? currentUser.getPreferredLanguage() : paramLang;
        userCalReq.setUserWid(userId);
        userCalReq.setStartTime(startTime);
        userCalReq.setEndTime(endTime);
        userCalReq.setLang(lang);
        Map<String, ArrayList<CalendarDtoV2>> datas = new HashMap<>();

        Map<String, Object> sysConfig = getSysConfig(request);
        Object value = sysConfig.get("addCalendar");
        Map<String, String> selectMap = new LinkedHashMap<>();
        if (value instanceof List && !((List) value).isEmpty()) {
            ((JSONArray) value).forEach(e -> {
                String id = ((JSONObject) e).getString("id");
                String primaryColor = ((JSONObject) e).getString("color");
                selectMap.put(id, primaryColor);
            });
        }
        DubboResponse<List<CalendarDataInfoV2>> response = userCalDubboService.getUserCalV2(userCalReq);
        List<CalendarDataInfoV2> data = new ArrayList<>();
        if ("0".equals(response.getErrcode())) {
            data = response.getData();
            datas = data.stream().filter(e -> "1".equals(e.getCalWid())||  "2".equals(e.getCalWid()) || StringUtil.isNotBlank(selectMap.get(e.getCalWid()))).map(e -> {
                CalendarDtoV2 calendarDto = new CalendarDtoV2();
                BeanUtils.copyProperties(e, calendarDto);
                calendarDto.setPrimaryColor(selectMap.get(e.getCalWid()));
                return calendarDto;
            }).collect(Collectors.toMap(e -> e.getStartDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), e -> Lists.newArrayList(e), (e1, e2) -> {
                e1.addAll(e2);
                List<CalendarDtoV2> collect1 = e1.stream().sorted(Comparator.comparing(l -> l.getStartTime(), Comparator.nullsLast(LocalDateTime::compareTo))).collect(Collectors.toList());
                return new ArrayList<>(collect1);
            }));
        } else {
            datas = new HashMap<>();
        }
        result.put("datas", datas);
        result.put("config", sysConfig);
        if(currentUser!=null) {
            DubboResponse<UserSubscribeResult> userSubscribeResult = userCalDubboService.getUserSubscribeResult(currentUser.getUserAccount());
            if( userSubscribeResult!=null && "0".equals(userSubscribeResult.getErrcode())) {
                result.put("userSubscribe", userSubscribeResult.getData());
            }
        }
        return result;
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();
        List<AbstractComponent> components = componentContainer.getComponents();
        Object value = components.stream().filter(e -> Global.Components.ADD_CALENDAR.getKey().equals(e.getKey())).findFirst().orElse(new AddCalendarComponent()).getValue();
        String primaryColor = getPrimaryColor(cardConfigReq.getTemplateWid());

        List<CalendarConsoleDto> datas = getAllCalendars(primaryColor, Global.DEFAULT_LANGUAGE);
        Map<String, CalendarConsoleDto> nameMap = datas.stream().collect(Collectors.toMap(CalendarConsoleDto::getId, e -> e, (v1, v2) -> v1));
        List<CalendarConsoleDto> realValues = new ArrayList<>();
        if (value instanceof List && !((List) value).isEmpty()) {
            ((JSONArray) value).forEach(e -> {
                String id = ((JSONObject) e).getString("id");
                CalendarConsoleDto dto = nameMap.get(id);
                if (dto != null) {
                    realValues.add(new CalendarConsoleDto().setId(id).setName(dto.getName()).setPrimaryColor(dto.getPrimaryColor()));
                }
            });
        }
        componentContainer.setData(Global.Components.ADD_CALENDAR.getKey(), Global.ComponentParam.DATAS, datas);
        componentContainer.setData(Global.Components.ADD_CALENDAR.getKey(), Global.ComponentParam.VALUE, realValues);

        // 日程提醒方式动态添加
        List<JSONObject> list = getChannel();
        Object val = components.stream().filter(e -> Global.Components.WEEK_REMIND_CALENDAR.getKey().equals(e.getKey())).findFirst().orElse(new WeekRemindCalendarComponent()).getValue();
        if ( CollectionUtils.isNotEmpty(list) ) {
            if ( "INBOX".equals(list.get(0).get("value")) ) {
                if ( val instanceof Map && ObjectUtils.isNotEmpty(val) ) {
                    List<String> checkbox = ( List<String> ) (( Map ) val).get("checkbox");
                    if ( CollectionUtils.isEmpty(checkbox) || !checkbox.contains("INBOX") ) {
                        checkbox.add("INBOX");
                    }
                    (( Map ) val).put("checkbox", checkbox);
                }
            }
        }

        componentContainer.setData(Global.Components.WEEK_REMIND_CALENDAR.getKey(), Global.ComponentParam.DATAS, list);
        componentContainer.setData(Global.Components.WEEK_REMIND_CALENDAR.getKey(), Global.ComponentParam.VALUE, val);
        return componentContainer;
    }

    private List<JSONObject> getChannel() {
        List<JSONObject> list = new ArrayList<>();
        Map<String, String> sendChannel = ApplicationContextUtil.get(SystemToolsService.class).getAllEnableChannelSendType();
        sendChannel.forEach((key, val) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", val);
            jsonObject.put("value", key);
            if ("INBOX".equals(key)) {
                jsonObject.put("disabled", true);
            }
            // 提醒通道去掉短信
            if ( "SMS".equals(key) ) {
                return;
            }
            list.add(jsonObject);
        });
        list.sort(Comparator.comparingInt(obj -> ((String) obj.get("value")).length()));
        list.sort((o1, o2) -> {
            if ("INBOX".equals(o1.get("value"))) {
                return -1;
            } else if ("INBOX".equals(o2.get("value"))) {
                return 1;
            }
            return 0;
        });
        return list;
    }

    private Map<String, Object> getSysConfig(CardAjaxRequest request) {
        String config = ApplicationContextUtil.get(CardUtil.class)
                .getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String lang = null != currentUser ? currentUser.getPreferredLanguage() : "";
        // 是否周提醒及提醒方式
        Map<String, String> sendChannel = ApplicationContextUtil.get(SystemToolsService.class).getSendChannel(lang);
        Map<String, Object> weekValueMap = ( Map<String, Object> ) configMap.get(Global.Components.WEEK_REMIND_CALENDAR.getKey());
        List<String> checkboxList = ( List<String> ) weekValueMap.get("checkbox");

        if ( CollectionUtils.isEmpty(checkboxList) || !checkboxList.contains("INBOX") ) {
            if ( sendChannel.containsKey("INBOX") ) {
                checkboxList.add(0, "INBOX");
            }
        }

        List<JSONObject> list = new ArrayList<>();
        checkboxList.forEach(channel -> {
            String value = sendChannel.get(channel);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", value);
            jsonObject.put("value", channel);
            list.add(jsonObject);
        });
        weekValueMap.put("checkbox", list);
        configMap.put(Global.Components.WEEK_REMIND_CALENDAR.getKey(), weekValueMap);
        return configMap;
    }

    public Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !"[]".equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }

    List<CalendarConsoleDto> getAllCalendars(String primaryColor, String lang) {
        List<CalendarInfo> datas;
        UserCalDubboService userCalDubboService = ApplicationContextUtil.get(CardDubboUtil.class).getUserCalDubboService();
        DubboResponse<List<CalendarInfo>> response = userCalDubboService.getCalList(QueryModelEnum.CONSOLE.getCode(), null, lang);
        if ("0".equals(response.getErrcode())) {
            datas = (List<CalendarInfo>) response.getData();
            if (CollectionUtils.isNotEmpty(datas)) {
                return datas.stream().map(e -> new CalendarConsoleDto().setId(e.getWid()).setName(e.getCalName()).setPrimaryColor(primaryColor)).collect(Collectors.toList());
            }
        }
        return Collections.EMPTY_LIST;
    }

    //获取主题色
    private String getPrimaryColor(String templateWid) {
        ShowProgrammeEntity showProgramme = ApplicationContextUtil.get(ShowProgrammeService.class).getById(templateWid);
        HomeService homeService = ApplicationContextUtil.get(HomeService.class);
        TemplateService templateService = ApplicationContextUtil.get(TemplateService.class);
        TemplateEntity templateEntity = templateService.getById(showProgramme.getTemplateId());
        String localTemplateId = templateEntity.getTemplateId();


        String valueTemplateConfig = showProgramme.getTemplateConfig();

        if (StringUtils.isBlank(valueTemplateConfig)) {
            TemplateUtil templateUtil = ApplicationContextUtil.get(TemplateUtil.class);
            ITemplate template = templateUtil.getTemplate(localTemplateId);
            valueTemplateConfig = template.getTemplateConfig(String.valueOf(showProgramme.getPlatformType()));
        }
        String configJson = homeService.getConfigJson(localTemplateId, showProgramme.getPlatformType());
        ComponentContainer componentContainer = new ComponentContainer(configJson, valueTemplateConfig);
        List<AbstractComponent> components = componentContainer.getComponents();
        AbstractComponent primaryColor = components.stream().filter(e -> "themeColorSetting".equals(e.getKey())).findFirst().orElseThrow(() -> new BusinessException("无主题色配置"));
        if (primaryColor.getValue() == null) {
            Object defaultValue = primaryColor.getDefaultValue();
            return (String) ((LinkedHashMap) defaultValue).get("portal-primary-color-lv1");
        } else {
            Object value = primaryColor.getValue();
            return (String) ((JSONObject) value).get("portal-primary-color-lv1");
        }
    }

    /**
     * 获取用户周提醒数据
     */
    private Object getWeeklyReminder(CardAjaxRequest request) {
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        UserCalCardRelMapper userCalCardRelMapper = ApplicationContextUtil.get(UserCalCardRelMapper.class);
        LambdaQueryWrapper<UserCalCardRelEntity> wrapper = Wrappers.<UserCalCardRelEntity>lambdaQuery()
                .eq(UserCalCardRelEntity::getCardWid, request.getCardWid())
                .eq(UserCalCardRelEntity::getUserId, currentUser.getWid())
                .eq(UserCalCardRelEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(UserCalCardRelEntity::getIsEnabled, Global.Status.ENABLE.getId());
        return userCalCardRelMapper.selectOne(wrapper);
    }

    /**
     * 用户设置周提醒
     */
    private Object setWeekReminder(CardAjaxRequest request) {
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        String cardWid = request.getCardWid();
        Map<String, String> param = request.getParam();
        String openRemind = param.get("openRemind");
        UserCalCardRelMapper userCalCardRelMapper = ApplicationContextUtil.get(UserCalCardRelMapper.class);
        LambdaQueryWrapper<UserCalCardRelEntity> wrapper = Wrappers.<UserCalCardRelEntity>lambdaQuery()
                .eq(UserCalCardRelEntity::getCardWid, cardWid)
                .eq(UserCalCardRelEntity::getUserId, currentUser.getWid())
                .eq(UserCalCardRelEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(UserCalCardRelEntity::getIsEnabled, Global.Status.ENABLE.getId());
        Date date = CommonUtil.getSystemDate();
        if ( Global.CARD_UPLOAD_TYPE.equals(openRemind) ) {
            // 打开周提醒
            String remindTime = param.get("remindTime");
            String remindWay = param.get("remindWay");
            String remindTimeHour = param.get("remindTimeHour");
            remindTimeHour = StringUtil.isNotEmpty(remindTimeHour) ? remindTimeHour : "12";
            UserCalCardRelEntity entity = userCalCardRelMapper.selectOne(wrapper);
            if ( ObjectUtils.isNotEmpty(entity) ) {
                entity.setRemindTime(remindTime).setRemindWay(remindWay).setRemindTimeHour(remindTimeHour).setUpdateTime(date);
                userCalCardRelMapper.updateById(entity);
            } else {
                entity = new UserCalCardRelEntity();
                entity.setUserId(currentUser.getWid()).setRemindWay(remindWay)
                        .setCardWid(cardWid)
                        .setRemindTime(remindTime)
                        .setIsDeleted(Global.DeleteStatus.NO.getId())
                        .setIsEnabled(Global.Status.ENABLE.getId())
                        .setRemindTimeHour(remindTimeHour)
                        .setWid(StringUtil.uuid());
                entity.setUpdateTime(date);
                entity.setCreateTime(date);
                userCalCardRelMapper.insert(entity);
            }
        } else {
            // 关闭, 删除配置表中数据
            userCalCardRelMapper.delete(wrapper);
        }
        return "success";
    }
}
