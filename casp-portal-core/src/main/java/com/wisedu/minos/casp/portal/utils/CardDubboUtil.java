package com.wisedu.minos.casp.portal.utils;

import com.wisedu.casp.cal.api.CalGroupDubboService;
import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.api.UserCalDubboServiceV2;
import com.wisedu.casp.cal.api.UserEventModifyDubboService;
import com.wisedu.casp.cal.model.DubboScheduleInfo;
import com.wisedu.casp.cal.model.DubboScheduleRequest;
import com.wisedu.casp.cal.model.DubboUserInfoRequest;
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
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.stata.api.DubboStataService;

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
     * 获取日历管理使用的dubbo服务
     * @return
     */
    UserCalDubboServiceV2 getUserCalDubboServiceV2();
    /**
     * 获取群组日历管理使用的dubbo服务
     * @return
     */
    CalGroupDubboService getCalGroupDubboService();

    /**
     * 获取服务查询的相关dubbo服务
     * @return
     */
    AppServiceService getAppServiceService();

    /**
     * 获取消息相关dubbo服务
     * @return
     */
    MessageSendService getMessageSendService();

    Object createSchedule(DubboScheduleInfo dubboScheduleInfo);

    Object modifySchedule(DubboScheduleInfo dubboScheduleInfo);

    Object removeSchedule(String wid);

    Object searchSchedule(String wid);

    Object shareSchedule(DubboScheduleRequest dubboScheduleRequest);

    Object searchUser(DubboUserInfoRequest dubboUserInfoRequest);

    String getDomain();

    void removeRedisData(CardAjaxRequest request);

    /**
     * 获取事项相关的dubbo服务
     */
    DubboServiceItemService getDubboServiceItemService();

    DubboOneThingService getDubboOneThingService();

    /**
     * 获取使用分析的dubbo服务
     * @return
     */
    DubboStataService getDubboStataService();

    /**
     * 获取日程的dubbo服务
     * @return
     */
    UserEventModifyDubboService getUserEventModifyDubboService();
    
    /**
    * 获取所有的内网IP段
    */
    SystemConfigService getSystemConfigService();

    DubboFavouriteOneThingService getDubboFavouriteOneThingService();

    /**
     * 根据deptWid查部门，包含子集
     * @param request 请求参数
     * @return
     */
    DubboPaginationResponse<DubboDeptInfo> getDeptById(DubboQueryDeptRequest request);

    DubboNewsService getDubboNewsService();
}
