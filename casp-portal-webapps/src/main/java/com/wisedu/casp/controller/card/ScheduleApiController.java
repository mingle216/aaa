package com.wisedu.casp.controller.card;

import com.wisedu.minos.annotation.OperationLog;
import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.model.HandleScheduleRequest;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.ScheduleInfoResponse;
import com.wisedu.minos.casp.portal.service.ScheduleApi;
import com.wisedu.minos.casp.portal.service.ScheduleApiAdapter;
import com.wisedu.minos.gateway.client.annotation.IgnoreGatewaySecurity;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ScheduleApiController implements ScheduleApi {

    @Autowired
    private ScheduleApiAdapter adapter;


    /**
     * 获取分享日程详情
     **/
    @MustLogin
    @Override
    public ScheduleInfoResponse getShareEvent(String wid) {
        return adapter.getShareEvent(wid);
    }


    /**
     * 日程分享处理接口
     **/
    @MustLogin
    @Override
    public ModelApiResponse scheduleHandlerShareEventPost(HandleScheduleRequest body) {
        return adapter.scheduleHandlerShareEventPost(body);
    }

}
