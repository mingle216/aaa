package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.HandleScheduleRequest;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.ScheduleInfoResponse;


public interface ScheduleApiAdapter {


    /**
     * 获取分享日程详情
     **/
    public ScheduleInfoResponse getShareEvent(String wid);


    /**
     * 日程分享处理接口
     **/
    public ModelApiResponse scheduleHandlerShareEventPost(HandleScheduleRequest body);

}
