package com.wisedu.minos.casp.portal.utils;

import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.expansion.dubbo.apps.feedback.service.DubboFeedbackService;
import com.wisedu.casp.tdc.api.DubboApplicationService;
import com.wisedu.casp.tdc.api.DubboTdcService;
import com.wisedu.minos.api.data.AppServiceService;

public interface CardDubboUtil {


    /**
     * getDubboFeedbackService
     * <p/>
     * 获取意见反馈卡片中使用的dubbo服务
     *
     * @return T
     * @throws
     * @date 2021-03-30 18:21
     */
    DubboFeedbackService getDubboFeedbackService();

    /**
     * 获取待办中心我的申请使用的dubbo服务
     * @return
     */
    DubboApplicationService getDubboApplicationService();
    /**
     * 获取待办中心待办已办使用的dubbo服务
     * @return
     */
    DubboTdcService getDubboTdcService();

    /**
     * 获取日历管理使用的dubbo服务
     * @return
     */
    UserCalDubboService getUserCalDubboService();

    /**
     * 获取服务查询的相关dubbo服务
     * @return
     */
    AppServiceService getAppServiceService();

}
