package com.wisedu.amp3compatible.controller;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/newMobile/item")
public class NewMobileServiceItemController {

    private static final Logger logger = LoggerFactory.getLogger(NewMobileServiceItemController.class);

    @ResponseBody
    @RequestMapping(value = "/queryMobileServiceCountGroupByType", method = RequestMethod.GET)
    public Map<String, Object> queryMobileServiceCountGroupByType(@RequestParam(value = "userType", required = true) String userType,
                                                                  @RequestParam(value = "maxItems", required = false, defaultValue = "0") Integer maxItems,
                                                                  @RequestParam(value = "queryType", required = true) String queryType) {
        try {


            JSONObject param = new JSONObject();
            param.put("userType", userType);
            param.put("maxItems", maxItems == 0 ? Integer.MAX_VALUE : maxItems);
            param.put("queryType", queryType);
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);


            return RestTemplateUtils.post("/casp-sim/newMobile/item/queryMobileServiceCountGroupByType", httpEntity, Map.class).getBody();
        } catch (Exception e) {
            logger.error("移动端分类统计服务事项失败", e);
            return responseError("移动端分类统计服务事项失败，原因：" + e.getMessage());
        }
    }

    /**
     * 按部门或主题分页查询服务事项
     *
     * @param request
     * @param typeCode
     * @param searchKey
     * @param isOnline
     * @param queryType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryMobileServiceGroupByType", method = RequestMethod.GET)
    public Object queryMobileServiceGroupByType(HttpServletRequest request, @RequestParam(value = "typeCode", required = false) String typeCode,
                                                @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey,
                                                @RequestParam(value = "isOnline", required = false, defaultValue = "false") String isOnline,
                                                @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "userType", required = true) String userType,
                                                @RequestParam(value = "queryType", required = false) String queryType) {
        try {
            //调试代码，后面去掉
//            for (Cookie cookie : request.getCookies()) {
//                logger.info("cpdaily cookie:{}",cookie.getValue());
//            }
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
//            UserInfo userInfo = (UserInfo) request.getSession().getAttribute("user");
//            if (userInfo != null) {
//                logger.info("cpdaily userInfo:{}",userInfo.getUserAccount());
//            }
            JSONObject param = new JSONObject();
            param.put("typeCode", typeCode);
            param.put("isOnline", isOnline);
            param.put("searchKey", searchKey);
            param.put("pageNumber", pageNumber);
            param.put("pageSize", pageSize);
            param.put("userType", userType);
            param.put("queryType", queryType);
            param.put("userAccount", user.getUserAccount());
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
            return RestTemplateUtils.post("/casp-sim/newMobile/item/queryMobileServiceGroupByType", httpEntity, Map.class).getBody();
        } catch (Exception e) {
            logger.error("移动端按部门或主题分页查询服务事项出错", e);
            return responseError("移动端按部门或主题分页查询服务事项出错" + e.getMessage());
        }
    }


    /**
     * 根据工作间联想查询，模糊匹配事项名称
     *
     * @param searchKey
     * @param userType
     * @param maxItems
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/serviceMobileAssociate", method = {RequestMethod.GET, RequestMethod.POST})
    public Object serviceMobileAssociate(@RequestParam(value = "searchKey", required = true) String searchKey, @RequestParam(value = "userType", required = true) String userType,
                                         @RequestParam(value = "maxItems", required = false, defaultValue = "5") Integer maxItems) {
        try {
            JSONObject param = new JSONObject();
            param.put("searchKey", searchKey);
            param.put("userType", userType);
            param.put("maxItems", maxItems);
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
            return RestTemplateUtils.post("/casp-sim/newMobile/item/serviceMobileAssociate", httpEntity, Map.class).getBody();
        } catch (Exception e) {
            logger.error("移动端联想搜索服务事项失败", e);
            return responseError("移动端联想搜索服务事项失败，原因：" + e.getMessage());
        }
    }


    /**
     * 根据id获取服务事项
     *
     * @param serviceItemIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryServiceItemByIds", method = RequestMethod.GET)
    public Object queryServiceItemById(@RequestParam(value = "serviceItemIds") String serviceItemIds) {
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            JSONObject param = new JSONObject();
            param.put("serviceItemIds", serviceItemIds);
            param.put("userAccount", user.getUserAccount());
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
            return RestTemplateUtils.post("/casp-sim/newMobile/item/queryServiceItemByIds", httpEntity, Map.class).getBody();

        } catch (Exception e) {
            logger.error("移动端根据id获取服务事项失败", e);
            return responseError("移动端根据id获取服务事项失败，原因：" + e.getMessage());
        }
    }

    private Map<String, Object> dataTableData(List<?> records, int total) {
        return responseData("0", "", records, Collections.singletonMap("total", (Object) total));
    }


    private Map<String, Object> responseData(Object data) {
        return responseData("0", "", data, null);
    }

    private Map<String, Object> responseError(String msg) {
        return responseData("1", msg, null, null);
    }

    private Map<String, Object> responseError(String msg, Object data) {
        return responseData("1", msg, data, null);
    }


    private Map<String, Object> responseData(String code, String msg, Object data, Map<String, Object> extendData) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", code);
        resultMap.put("message", msg);
        resultMap.put("datas", data);
        if (MapUtils.isNotEmpty(extendData)) {
            resultMap.putAll(extendData);
        }
        return resultMap;
    }

}
