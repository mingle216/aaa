package com.wisedu.minos.casp.portal.utils;

import com.wisedu.casp.cal.api.*;
import com.wisedu.casp.cal.model.*;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.DubboFeedbackService;
import com.wisedu.casp.mc.api.message.MessageSendService;
import com.wisedu.casp.sim.api.DubboOneThingService;
import com.wisedu.casp.sim.api.DubboServiceItemService;
import com.wisedu.casp.sim.api.favouriteonething.DubboFavouriteOneThingService;
import com.wisedu.casp.sim.model.DubboDeptInfo;
import com.wisedu.casp.sim.model.DubboPaginationResponse;
import com.wisedu.casp.sim.model.DubboQueryDeptRequest;
import com.wisedu.casp.tdc.api.DubboApplicationService;
import com.wisedu.casp.tdc.api.DubboTdcService;
import com.wisedu.minos.api.config.SystemConfigService;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.news.DubboNewsService;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.stata.api.DubboStataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardDubboUtilImpl implements CardDubboUtil {

    @Value("${module.domain}")
    private String moudleDomain;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserUtil userUtil;

    @DubboReference
    DubboFeedbackService dubboFeedbackService;
    @DubboReference
    DubboApplicationService dubboApplicationService;
    @DubboReference
    DubboTdcService dubboTdcService;
    @DubboReference
    UserCalDubboService userCalDubboService;
    @DubboReference
    UserCalDubboServiceV2 userCalDubboServiceV2;
    @DubboReference
    AppServiceService appServiceService;
    @DubboReference
    UserScheduleDubboService userScheduleDubboService;
    @DubboReference
    DubboServiceItemService dubboServiceItemService;
    @DubboReference
    DubboStataService dubboStataService;
    @DubboReference
    UserEventModifyDubboService userEventModifyDubboService;
    @DubboReference
    private DubboOneThingService dubboOneThingService;
    @DubboReference
    private CalGroupDubboService calGroupDubboService;
    @DubboReference
    private SystemConfigService systemConfigService;
    @DubboReference
    private DubboFavouriteOneThingService dubboFavouriteOneThingService;
    @DubboReference
    private MessageSendService messageSendService;

    @DubboReference
    private DubboNewsService dubboNewsService;

    @Override
    public DubboFeedbackService getDubboFeedbackService() {
        return dubboFeedbackService;
    }

    @Override
    public DubboApplicationService getDubboApplicationService() {
        return dubboApplicationService;
    }

    @Override
    public DubboTdcService getDubboTdcService() {
        return dubboTdcService;
    }

    @Override
    public UserCalDubboService getUserCalDubboService() {
        return userCalDubboService;
    }
    @Override
    public UserCalDubboServiceV2 getUserCalDubboServiceV2() {
        return userCalDubboServiceV2;
    }

    @Override
    public CalGroupDubboService getCalGroupDubboService() {
        return calGroupDubboService;
    }

    @Override
    public AppServiceService getAppServiceService() {
        return appServiceService;
    }

    @Override
    public MessageSendService getMessageSendService () {
        return messageSendService;
    }

    @Override
    public UserEventModifyDubboService getUserEventModifyDubboService(){
        return userEventModifyDubboService;
    }

    @Override
    public SystemConfigService getSystemConfigService() {
        return systemConfigService;
    }

    @Override
    public Object createSchedule(DubboScheduleInfo dubboScheduleInfo) {
        return userScheduleDubboService.create(dubboScheduleInfo);
    }

    @Override
    public Object modifySchedule(DubboScheduleInfo dubboScheduleInfo) {
        return userScheduleDubboService.modify(dubboScheduleInfo);
    }

    @Override
    public Object removeSchedule(String wid) {
        return userScheduleDubboService.remove(wid);
    }

    @Override
    public Object searchSchedule(String wid) {
        return userScheduleDubboService.searchSchedule(wid);
    }

    @Override
    public Object shareSchedule(DubboScheduleRequest dubboScheduleRequest) {
        return userScheduleDubboService.shareSchedule(dubboScheduleRequest);
    }

    @Override
    public Object searchUser(DubboUserInfoRequest dubboUserInfoRequest) {
        DubboResponse<DubboUserInfoResponse> dubboUserInfoResponseDubboResponse = userScheduleDubboService.searchUser(dubboUserInfoRequest);
        UserInfo currentUser = userUtil.getCurrentUser();
        if(currentUser!=null) {
            List<DubboScheduleUserInfo> userInfos = dubboUserInfoResponseDubboResponse.getData().getData().stream().filter(e -> !e.getWid().equals(currentUser.getWid())).collect(Collectors.toList());
            dubboUserInfoResponseDubboResponse.getData().setData(userInfos);
        }
        return dubboUserInfoResponseDubboResponse;

    }

    @Override
    public String getDomain() {
        return moudleDomain;
    }

    @Override
    public void removeRedisData(CardAjaxRequest request) {
        String cardWid = request.getParam().get("cardWid");
        if (StringUtils.isBlank(cardWid)) {
            cardWid = request.getCardWid();
        }
        String key = "userId:" + userUtil.getUserAccount() + ":cardId:" + request.getCardId() + ":cardWid:" + cardWid;
        redisUtil.del(key);
    }

    @Override
    public DubboServiceItemService getDubboServiceItemService () {
        return dubboServiceItemService;
    }

    @Override
    public DubboOneThingService getDubboOneThingService() {
        return dubboOneThingService;
    }

    @Override
    public DubboStataService getDubboStataService () {
        return dubboStataService;
    }

    @Override
    public DubboFavouriteOneThingService getDubboFavouriteOneThingService() {
        return dubboFavouriteOneThingService;
    }

    @Override
    public DubboPaginationResponse<DubboDeptInfo> getDeptById(DubboQueryDeptRequest request) {
        return dubboServiceItemService.getDeptById(request);
    }

    @Override
    public DubboNewsService getDubboNewsService() {
        return dubboNewsService;
    }
}
