package com.wisedu.minos.casp.portal.service.impl;

import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.model.DubboResponse;
import com.wisedu.casp.cal.model.DubboShareScheduleResponse;
import com.wisedu.casp.cal.model.ShareScheduleInfo;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.ScheduleApiAdapter;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class ScheduleApiAdapterImpl implements ScheduleApiAdapter {

    @DubboReference
    UserCalDubboService userCalDubboService;

    @Override
    public ScheduleInfoResponse getShareEvent(String wid) {
        if (StringUtils.isBlank(wid)) {
            throw new MinosException("381001", "eventWid不能为空");
        }
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        ShareScheduleInfo shareScheduleInfo = new ShareScheduleInfo();
        shareScheduleInfo.setEventWid(wid);
        shareScheduleInfo.setSharedUserWid(user.getUserAccount());
        DubboResponse<DubboShareScheduleResponse> userShareEvent = userCalDubboService.getUserShareEvent(shareScheduleInfo);
        ScheduleInfoResponse scheduleInfoResponse = new ScheduleInfoResponse();
        if ("0".equals(userShareEvent.getErrcode())) {
            DubboShareScheduleResponse data = userShareEvent.getData();
            ScheduleInfo scheduleInfo = new ScheduleInfo();
            scheduleInfo.setContent(data.getContent());
            scheduleInfo.setShareState(data.getShareState());
            scheduleInfo.setTitle(data.getTitle());
            scheduleInfo.setStartTime(data.getStartTime());
            scheduleInfo.setEndTime(data.getEndTime());
            scheduleInfo.setLanguageKey(data.getLanguageKey());
            scheduleInfoResponse.setData(scheduleInfo);
            return scheduleInfoResponse;
        }
        scheduleInfoResponse.setErrcode("-1");
        scheduleInfoResponse.setErrmsg(userShareEvent.getErrmsg());
        return scheduleInfoResponse;
    }

    @Override
    public ModelApiResponse scheduleHandlerShareEventPost(HandleScheduleRequest body) {
        String eventWid = body.getEventWid();
        if (StringUtils.isBlank(eventWid)) {
            throw new MinosException("381001", "eventWid不能为空");
        }
        Integer shareState = body.getShareState();
        if (shareState == null) {
            throw new MinosException("381001", "shareState不能为空");
        }
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        ShareScheduleInfo shareScheduleInfo = new ShareScheduleInfo();
        shareScheduleInfo.setEventWid(eventWid);
        shareScheduleInfo.setSharedUserWid(user.getUserAccount());
        shareScheduleInfo.setShareState(shareState);
        DubboResponse<DubboShareScheduleResponse> responseDubboResponse = userCalDubboService.handlerShareEvent(shareScheduleInfo);
        ModelApiResponse modelApiResponse = new ModelApiResponse();
        if (!"0".equals(responseDubboResponse.getErrcode())) {
            modelApiResponse.setErrcode("-1");
            modelApiResponse.setErrmsg(responseDubboResponse.getErrmsg());
        }
        return modelApiResponse;
    }
}
