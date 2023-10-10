package com.wisedu.minos.casp.portal.utils;

import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.DubboFeedbackService;
import com.wisedu.casp.tdc.api.DubboApplicationService;
import com.wisedu.casp.tdc.api.DubboTdcService;
import com.wisedu.minos.api.data.AppServiceService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class CardDubboUtilImpl implements CardDubboUtil {


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
    public UserCalDubboService getUserCalDubboService () {
        return userCalDubboService;
    }

    @Override
    public AppServiceService getAppServiceService() {
        return appServiceService;
    }
}
