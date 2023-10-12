package com.wisedu.minos.casp.portal.utils;

import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.api.UserScheduleDubboService;
import com.wisedu.casp.cal.model.DubboScheduleInfo;
import com.wisedu.casp.cal.model.DubboScheduleRequest;
import com.wisedu.casp.cal.model.DubboUserInfoRequest;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.DubboFeedbackService;
import com.wisedu.casp.tdc.api.DubboApplicationService;
import com.wisedu.casp.tdc.api.DubboTdcService;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    AppServiceService appServiceService;
    @DubboReference
    UserScheduleDubboService userScheduleDubboService;

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
    public AppServiceService getAppServiceService() {
        return appServiceService;
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
        return userScheduleDubboService.searchUser(dubboUserInfoRequest);
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
}
