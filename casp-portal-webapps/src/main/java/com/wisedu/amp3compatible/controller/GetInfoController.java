package com.wisedu.amp3compatible.controller;

import com.wisedu.amp3compatible.model.*;
import com.wisedu.amp3compatible.service.GetInfoService;
import com.wisedu.minos.gateway.client.annotation.OpenGatewayApi;
import com.wisedu.minos.gateway.client.util.InsideApiClassifyEmums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/8/1
 */

/**
 * 从amp 3.0.3_TR13 AppRestfulController迁移过来
 */
@RestController
public class GetInfoController {
    @Autowired
    GetInfoService getInfoApi;

    /**
     * 根据个人数据的wid获取具体的详情信息的接口
     *
     * @param request
     * @return
     */
    @OpenGatewayApi(name = "查询个人数据详情", realPath = "/restful/getPersonalRemindDetail.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = "/restful/getPersonalRemindDetail.do",
            produces = { "application/json;charset=UTF-8" },
            method = RequestMethod.POST)
    public Object getPersonalRemindDetail (@RequestBody PersonalRemindDetailRequest request){
        return getInfoApi.getPersonalRemindDetail(request);
    }

    @OpenGatewayApi(name = "查询用户可见个人数据的列表", realPath = "/restful/getPersonalRemindList.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = { "/restful/getPersonalRemindList.do" },
            produces = { "application/json;charset=UTF-8" },
            method = RequestMethod.POST)
    public PersonalRemindResponse getPersonalRemindList (@RequestBody UserIdEntity userIdEntity) {
        return getInfoApi.getPersonalRemindList(userIdEntity);
    }

    @OpenGatewayApi(name = "获取用户已绑定的邮箱账号列表接口", realPath = "/restful/getMailAccountInfo.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = "/restful/getMailAccountInfo.do",
            produces = { "application/json;charset=UTF-8" },
            method = RequestMethod.POST)
    public Object getMailAccountInfo (@RequestBody UserIdEntity userIdEntity) {
        return getInfoApi.getMailAccountInfo(userIdEntity);
    }

    @OpenGatewayApi(name = "事项主数据接口", realPath = "/restful/getServiceItemInfo.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = { "/restful/getServiceItemInfo.do" },
            produces = { "application/json;charset=UTF-8" },
            method = RequestMethod.POST)
    public Object getServiceItemInfo (@RequestBody YanBianServiceReq yanBianServiceReq) {
        return getInfoApi.getServiceItemInfo(yanBianServiceReq);
    }

    @OpenGatewayApi(name = "获取邮箱点击跳转地址", realPath = "/restful/getLinkUrl.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = "/restful/getLinkUrl.do",
            produces = { "application/json;charset=UTF-8" },
            method = RequestMethod.POST)
    public Object getLinkUrl (@RequestBody GetLinkUrlRequest request){
        return getInfoApi.getLinkUrl(request);
    }
}
