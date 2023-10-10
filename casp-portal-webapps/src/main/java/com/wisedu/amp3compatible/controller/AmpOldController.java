package com.wisedu.amp3compatible.controller;

import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.api.model.DubboUserInfoResp;
import com.wisedu.minos.api.model.DubboUserSearchByAccountReq;
import com.wisedu.minos.api.model.amp.AmpAppConditionRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.annotation.OpenGatewayApi;
import com.wisedu.minos.gateway.client.util.InsideApiClassifyEmums;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AmpOldController {

    @DubboReference
    private UserService userService;
    /**
     * 上科大兼容需求，兼容原公共服务接口，获取用户使用的语言信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/OpenService/api/language.json"}, produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @OpenGatewayApi(name = "获取用户使用的语言", realPath = "/OpenService/api/language.json", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public Map<String, Object> queryAppsByCondition(HttpServletRequest request) {
        String userId = request.getParameter("xgh");
        Map<String, Object> result = new HashMap<>();
        if(StringUtils.isEmpty(userId)){
            result.put("SFCG", "FAILED");
            result.put("XGH", "");
            result.put("YYLX", "");
            return result;
        }
        DubboUserSearchByAccountReq req = new DubboUserSearchByAccountReq();
        req.setAccount(userId);
        DubboUserInfoResp dubboUserInfoResp = userService.searchByAccount(req);
        List<DubboUserInfo> data = dubboUserInfoResp.getData();
        if(CollectionUtils.isEmpty(data)){
            result.put("SFCG", "FAILED");
            result.put("XGH", "userId");
            result.put("YYLX", "");
            return result;
        }

        DubboUserInfo currentUser = data.get(0);

        result.put("SFCG", "SUCCESS");
        result.put("XGH", userId);
        String lang = currentUser.getPreferredLanguage();
        if (StringUtils.isEmpty(lang)) {
            lang = "ZH_CN";
        }
        if ("cn".equalsIgnoreCase(lang) || "zh_cn".equalsIgnoreCase(lang)) {
            lang = "ZH_CN";
        }
        if ("en".equalsIgnoreCase(lang) || "en_us".equalsIgnoreCase(lang)) {
            lang = "EN_US";
        }
        result.put("YYLX", lang);

        return result;
    }
}
