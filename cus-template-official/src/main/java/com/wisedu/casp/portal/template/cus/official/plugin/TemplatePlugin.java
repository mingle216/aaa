package com.wisedu.casp.portal.template.cus.official.plugin;

import com.wisedu.casp.portal.template.cus.official.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractTemplate;
import com.wisedu.minos.casp.portal.spi.model.TemplateContext;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import java.util.*;

/**
 * @author kaisir
 */
@MinosSPI
public class TemplatePlugin extends AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(TemplatePlugin.class);
    @Override
    public String getTemplateId() {
        return TemplateVariable.TEMPLATE_ID;
    }

    @Override
    public String getTemplateConfig(String platformType) {
        return StringUtil.isNotEmpty(platformType)&&platformType.equals(String.valueOf(Global.PlatformType.MOBILE.getType()))?TemplateVariable.MOBILE_DEFAULT_CONFIG:TemplateVariable.PC_DEFAULT_CONFIG;
    }
    @Override
    public Object doExecDispatcher(TemplateAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getMyTaskCount":
                result = this.getMyTaskCount();
                break;
        }
        return result;
    }


    @Override
    public void destroy() {
        logger.info("destroy..");
    }

    private String getKeywords(Map<String, String> param, String keywords) {
        String keywords1 = param.get(keywords);
        return StringUtil.xssEncode(StringUtil.defaultIfEmpty(keywords1, ""));
    }




    //TODO 迁移到卡片
    private MyTaskCountResponse getMyTaskCount() {
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class)
                .getSystemConfigValue("taskcenter_url");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MyTaskCountResponse response = new MyTaskCountResponse();

        if (user == null) {
            return response;
        }
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(user.getUserAccount());
        HttpEntity entity = new HttpEntity(userIdRequest);

        TaskTodoAndDoneResponse taskTodoAndDoneResponse = RestTemplateUtils
                .post(taskCenterUrl + "/task/getTaskTodoAndDoneCount.do", entity, TaskTodoAndDoneResponse.class).getBody();
        MyProcessCountResponse myProcessCountResponse = RestTemplateUtils
                .post(taskCenterUrl + "/task/getMyProcessCount.do", entity, MyProcessCountResponse.class).getBody();

        if (null != taskTodoAndDoneResponse && null != taskTodoAndDoneResponse.getData()) {
            response.setTodoCount(taskTodoAndDoneResponse.getData().getTodoCount());
            response.setDoneCount(taskTodoAndDoneResponse.getData().getDoneCount());
        }
        if (null != myProcessCountResponse && null != myProcessCountResponse.getData()) {
            response.setRunningCount(myProcessCountResponse.getData().getRunningCount());
            response.setCompleteCount(myProcessCountResponse.getData().getCompleteCount());
        }

        return response;
    }

}
