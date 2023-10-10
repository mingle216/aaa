package com.wisedu.amp3compatible.controller;

import com.wisedu.amp3compatible.model.*;
import com.wisedu.amp3compatible.service.AmpResetfulService;
import com.wisedu.amp3compatible.util.AmpUtils;
import com.wisedu.amp3compatible.util.FastJsonUtils;
import com.wisedu.amp3compatible.util.ParameterCheckUtil;
import com.wisedu.minos.api.constant.SensitiveWordStrategyEnum;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.data.ServiceAmpService;
import com.wisedu.minos.api.model.DubboAmpAppResponse;
import com.wisedu.minos.api.model.DubboModelApiResp;
import com.wisedu.minos.api.model.PageResponse;
import com.wisedu.minos.api.model.amp.*;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.AppAppraiseService;
import com.wisedu.minos.casp.portal.service.impl.SensitiveWordService;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.annotation.OpenGatewayApi;
import com.wisedu.minos.gateway.client.util.InsideApiClassifyEmums;
import com.wisedu.minos.gateway.client.util.XSSKeyUtil;
import com.wisedu.minos.util.MinosCommonUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wisedu.amp3compatible.util.ParameterCheckUtil.verifyInput;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TestController
 * @Author: 01319098
 * @Date: 2022/8/1
 */
@Controller
@RequestMapping("/restful")
public class AppRestfulController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppRestfulController.class);

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private SensitiveWordService sensitiveWordService;
    @Autowired
    private AppAppraiseService appAppraiseService;
    @DubboReference
    private ServiceAmpService serviceAmpService;
    @DubboReference
    private AppServiceService appServiceService;

    @ResponseBody
    @RequestMapping(value = { "/queryAppsByCondition.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "查询用户有权看到的已上线应用列表", realPath = "/restful/queryAppsByCondition.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public Map<String, Object> queryAppsByCondition (@RequestBody AmpAppConditionRequest appConditionRequest) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageResponse<AmpServiceEntity> dubboResp = serviceAmpService.getAppList(appConditionRequest, CommonUtil.isMobileDevice());
            // 设置评分
            List<AmpServiceEntity> datas = dubboResp.getDatas();
            if ( CollectionUtils.isNotEmpty(datas) ) {
                Map<String, AppAppraiseSummaryVo> appAppraiseSummaryVoMap = appAppraiseService.queryAppAppraiseSummary(datas.stream().map(AmpServiceEntity::getWid).collect(Collectors.toList())).stream().collect(Collectors.toMap(AppAppraiseSummaryVo::getServiceWid, e -> e));
                datas.forEach(data -> {
                    AppAppraiseSummaryVo appAppraiseSummaryVo = appAppraiseSummaryVoMap.get(data.getWid());
                    if ( null != appAppraiseSummaryVo ) {
                        data.setAppScore(appAppraiseSummaryVo.getAvgScore());
                    }
                });
            }
            result.put("status", Status.OK_CODE);
            result.put("message", Status.OK);
            result.put("datas", datas.toArray(new AmpServiceEntity[0]));
            result.put("total", dubboResp.getTotal());
        } catch ( Exception e ) {
            result.put("status", Status.SYSTEM_ERROR_CODE);
            result.put("message", e.getMessage());
            LOGGER.error("调用接口queryAppsByCondition失败：" + e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/getNewApps.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "获取用户有权看到的最新服务", realPath = "/restful/getNewApps.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public DubboAmpServiceResponse getNewApps (@RequestBody AmpServiceTypeRequest request) {
        DubboAmpServiceResponse appResponse = new DubboAmpServiceResponse();
        try {
            int limitNum = 5;
            if ( request.getLimitNum() != 0 ) {
                limitNum = request.getLimitNum();
            }
            appResponse = serviceAmpService.getTypeApps(request, "new", limitNum, CommonUtil.isMobileDevice());
            appResponse.setStatus(Status.OK_CODE);
            appResponse.setMessage(Status.OK);


            AmpServiceEntity[] datas = appResponse.getDatas();
            List<AmpServiceEntity> dataList = Stream.of(datas).collect(Collectors.toList());
            if ( CollectionUtils.isEmpty(dataList) ) {
                return appResponse;
            }
            // 设置平均分
            List<AppAppraiseSummaryVo> appAppraiseSummaryVos = appAppraiseService.queryAppAppraiseSummary(dataList.stream().map(AmpServiceEntity::getWid).collect(Collectors.toList()));
            Map<String, Double> scoreMap = appAppraiseSummaryVos.stream().collect(Collectors.toMap(AppAppraiseSummaryVo::getServiceWid, AppAppraiseSummaryVo::getAvgScore));
            dataList.forEach(e -> {
                Double score = scoreMap.get(e.getWid());
                if ( null != score ) {
                    e.setAppScore(score);
                }
            });
            appResponse.setDatas(dataList.toArray(new AmpServiceEntity[0]));

        } catch ( Exception e ) {
            appResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            appResponse.setMessage(Status.SYSTEM_ERROR);
            LOGGER.error("调用接口getNewApps失败：" + e);
        }
        return appResponse;
    }

    @ResponseBody
    @RequestMapping(value = { "/getHotApps.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "获取用户有权看到的热门服务", realPath = "/restful/getHotApps.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public DubboAmpServiceResponse getHotApps (@RequestBody AmpServiceTypeRequest request) {
        DubboAmpServiceResponse appResponse = new DubboAmpServiceResponse();
        try {
            int limitNum = 10;
            if ( request.getLimitNum() != 0 ) {
                limitNum = request.getLimitNum();
            }
            String orderType = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("amp_hot_service_type");
            request.setOrderType(Integer.parseInt(orderType));
            appResponse = serviceAmpService.getTypeApps(request, "hot", limitNum, CommonUtil.isMobileDevice());

            appResponse.setStatus(Status.OK_CODE);
            appResponse.setMessage(Status.OK);

            AmpServiceEntity[] datas = appResponse.getDatas();
            List<AmpServiceEntity> dataList = Stream.of(datas).collect(Collectors.toList());
            if ( CollectionUtils.isEmpty(dataList) ) {
                return appResponse;
            }
            // 设置平均分
            List<AppAppraiseSummaryVo> appAppraiseSummaryVos = appAppraiseService.queryAppAppraiseSummary(dataList.stream().map(AmpServiceEntity::getWid).collect(Collectors.toList()));
            Map<String, Double> scoreMap = appAppraiseSummaryVos.stream().collect(Collectors.toMap(AppAppraiseSummaryVo::getServiceWid, AppAppraiseSummaryVo::getAvgScore));
            dataList.forEach(e -> {
                Double score = scoreMap.get(e.getWid());
                if ( null != score ) {
                    e.setAppScore(score);
                }
            });
            if ( "1".equals(orderType) ) {
                // 按照评分排序
                dataList.sort(Comparator.comparingDouble(AmpServiceEntity::getAppScore).reversed().thenComparingInt(AmpServiceEntity::getCollectCount));
            }

            // 截取
            dataList = dataList.subList(0, Math.min(dataList.size(), limitNum));
            appResponse.setDatas(dataList.toArray(new AmpServiceEntity[ 0 ]));
        } catch ( Exception e ) {
            appResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            appResponse.setMessage(Status.SYSTEM_ERROR);
            LOGGER.error("调用接口getNewApps失败：" + e);
        }
        return appResponse;
    }

    @ResponseBody
    @RequestMapping(value = { "/assessApp.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "评价应用", realPath = "/restful/assessApp.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public Map<String, Object> assessApp (@RequestBody AssessAppRequest request) {
        Map<String, Object> result = new HashMap<>();
        if ( null == request
                || StringUtil.isBlank(request.getAppId())
                || StringUtil.isBlank(request.getAssessment())
                || StringUtil.isBlank(request.getUserId())
                || StringUtil.isBlank(request.getScore())) {
            result.put("status", Status.PARAM_ERROR_CODE);
            result.put("message", Status.PARAM_ERROR);
            return result;
        }
        try {
            // 根据serviceId 获取 serviceWid
            String wid = MinosCommonUtil.getWid();
            UserInfo user = userUtil.getUserByAccount(request.getUserId());
            if ( null == user ) {
                throw new MinosException("-1", "用户不存在");
            }
            AppAppraiseEntity entity = new AppAppraiseEntity();
            entity.setWid(wid);
            entity.setUserId(request.getUserId());
            entity.setScore(Float.parseFloat(request.getScore()));
            entity.setContent(request.getAssessment());
            entity.setIsDeleted("0");
            entity.setUserName(user.getUserName());

            AmpServiceEntity serviceInfo = serviceAmpService.getServiceInfoById(request.getAppId(), false);
            if ( null == serviceInfo ) {
                throw new MinosException("-1", "服务不存在");
            }
            entity.setAppId(serviceInfo.getWid());

            // 对评论内容进行XSS校验
            XSSKeyUtil.isXssIllegalWord(entity.getContent());
            // 对评价内容进行敏感词处理
            // 获取敏感词策略配置
            String config = sensitiveWordService.getConfig(SensitiveWordStrategyEnum.ModelWid.getCode(), SensitiveWordStrategyEnum.SERVICE_STRATEGY.getCode());
            // "0" 不处理
            if ( SensitiveWordStrategyEnum.TWO.getCode().equals(config) ) {
                // 禁发
                if ( sensitiveWordService.isContainsSensitiveWord(entity.getContent()) ) {
                    throw new MinosException("-1", "填写内容包含敏感词");
                }
            }
            appAppraiseService.saveOrUpdate(entity);
            result.put("status", Status.OK_CODE);
            result.put("message", Status.OK);
            result.put("assessId", wid);

        } catch ( Exception e ) {
            result.put("status", Status.SYSTEM_ERROR_CODE);
            result.put("message", e.getMessage());
            LOGGER.error("保存评价失败：", e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/getAppDetail.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "查看应用详情", realPath = "/restful/getAppDetail.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public Map<String, Object> getAppDetail (@RequestBody AmpAppIdEntity appIdEntity) {
        Map<String, Object> result = new HashMap<>();
        if ( null == appIdEntity
                || StringUtil.isBlank(appIdEntity.getAppId())) {
            result.put("status", Status.PARAM_ERROR_CODE);
            result.put("message", Status.PARAM_ERROR);
            return result;
        }
        try {
            AmpServiceEntity entity = serviceAmpService.getServiceInfoById(appIdEntity.getAppId(), CommonUtil.isMobileDevice());
            if ( null != entity ) {
                // 设置评分
                AppAppraiseSummaryVo appAppraiseSummaryVo = appAppraiseService.queryAppAppraiseSummary(entity.getWid());
                if ( null != appAppraiseSummaryVo ) {
                    entity.setAppScore(appAppraiseSummaryVo.getAvgScore());
                }
            }

            result.put("status", Status.OK_CODE);
            result.put("message", Status.OK);
            result.put("data", entity);
        } catch ( Exception e ) {
            result.put("status", Status.SYSTEM_ERROR_CODE);
            result.put("message", Status.SYSTEM_ERROR);
            LOGGER.error("系统错误：", e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/collectApp.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "用户收藏或取消收藏应用", realPath = "/restful/collectApp.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public Map<String, Object> collectApp (@RequestBody DubboAmpCollectServiceRequest collectAppRequest) {
        Map<String, Object> result = new HashMap<>();
        if ( null == collectAppRequest
                || StringUtil.isBlank(collectAppRequest.getUserId()) || StringUtil.isBlank(collectAppRequest.getAppId()) || StringUtil.isBlank(collectAppRequest.getOperate())) {
            result.put("status", Status.PARAM_ERROR_CODE);
            result.put("message", Status.PARAM_ERROR);
            return result;
        }
        try {
            DubboModelApiResp resp = serviceAmpService.operateFavService(collectAppRequest);
            if ( "0".equals(resp.getErrcode()) ) {
                result.put("status", Status.OK_CODE);
                result.put("message", Status.OK);
            } else {
                result.put("status", Status.SYSTEM_ERROR_CODE);
                result.put("message", resp.getErrmsg());
                LOGGER.error("调用接口collectApp失败：：" + resp.getErrmsg());
            }
        } catch ( Exception e ) {
            result.put("status", Status.SYSTEM_ERROR_CODE);
            result.put("message", Status.SYSTEM_ERROR);
            LOGGER.error("调用接口collectApp失败：：" + e);
        }
        return result;
    }

    @ResponseBody
    @OpenGatewayApi(name = "根据用户id获取周期应用", realPath = "/restful/readyAndOpenApps.do",
            agentPath = "/restful/readyAndOpenApps.do",apiClassify= InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = { "/readyAndOpenApps.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    public AppTotalResponse readyAndOpenApps (@RequestBody UserIdEntity userIdEntity) {
        AppTotalResponse appTotalResponse = new AppTotalResponse();
        try {
            String userIdEntityJson = FastJsonUtils.toJson(userIdEntity);
            verifyJson(false, userIdEntityJson);
            verifyInput(userIdEntity.getUserId());
            DubboAmpAppResponse dubboAmpAppResponse = appServiceService.readyAndOpenApps(userIdEntity.getUserId());
            appTotalResponse.setStatus(Status.OK_CODE);
            appTotalResponse.setMessage(Status.OK);
            if("0".equals(dubboAmpAppResponse.getErrcode())&&CollectionUtils.isNotEmpty(dubboAmpAppResponse.getData())){
                List<AmpAppEntity> datas = dubboAmpAppResponse.getData();

                Map<String, AppAppraiseSummaryVo> appAppraiseSummaryVoMap = appAppraiseService.queryAppAppraiseSummary(datas.stream().map(AmpAppEntity::getAppId).collect(Collectors.toList())).stream().collect(Collectors.toMap(AppAppraiseSummaryVo::getServiceWid, e -> e));
                datas.forEach(data -> {
                    AppAppraiseSummaryVo appAppraiseSummaryVo = appAppraiseSummaryVoMap.get(data.getAppId());
                    if ( null != appAppraiseSummaryVo ) {
                        data.setAppScore(appAppraiseSummaryVo.getAvgScore());
                    }
                });
                appTotalResponse.setTotal(datas.size());
                appTotalResponse.setDatas(datas);
            }
        } catch ( Exception e ) {
            appTotalResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            appTotalResponse.setMessage(Status.SYSTEM_ERROR);
            LOGGER.error("调用接口readyAndOpenApps失败：", e);
        }
        return appTotalResponse;
    }

    @ResponseBody
    @OpenGatewayApi(name = "判断当前用户是否是当前应用的应用管理员", realPath = "/restful/checkUserAppManage",
            agentPath = "/restful/checkUserAppManage",apiClassify= InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = { "/checkUserAppManage" }, produces = { "application/json;charset=UTF-8" }, method = { RequestMethod.POST })
    public UserAppIdResponse checkUserAppManage (@RequestBody UserAppIdRequest userAppId) {
        UserAppIdResponse userAppIdResponse = new UserAppIdResponse();
        try {
            String userAppIdJson = FastJsonUtils.toJson(userAppId);
            verifyJson(false, userAppIdJson);
            verifyInput(true, userAppId.getAppId());
            verifyInput(userAppId.getUserIds());
            userAppIdResponse = appServiceService.checkUserAppManage(userAppId);
        } catch ( Exception e ) {
            userAppIdResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            userAppIdResponse.setMessage(e.getMessage());
            LOGGER.error("调用checkUserIdsIsAppManage接口异常", e);
        }
        return userAppIdResponse;
    }

    @ResponseBody
    @OpenGatewayApi(name = "解密打开应用时带出的当前用户身份信息", realPath = "/restful/decryptUserInfo",
            agentPath = "/restful/decryptUserInfo",apiClassify= InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    @RequestMapping(value = { "/decryptUserInfo" }, produces = { "application/json;charset=UTF-8" }, method = { RequestMethod.POST })
    public DecryptResponse decryptUserInfo (@RequestBody DecryptRequest decryptRequest) {
        DecryptResponse decryptResponse = new DecryptResponse();
        try {
            String decryptJson = FastJsonUtils.toJson(decryptRequest);
            verifyJson(false, decryptJson);
            verifyInput(decryptRequest.getCiphertext());
            String cipher = AmpUtils.decodeIdentityToken(decryptRequest.getCiphertext());
            String[] tokens = cipher.split(":");
            if ( tokens == null || tokens.length != 2 ) {
                throw new RuntimeException("非法的身份信息");
            }
            DecryptEntity entity = new DecryptEntity(tokens[ 0 ], tokens[ 1 ]);
            decryptResponse.setStatus(Status.OK_CODE);
            decryptResponse.setMessage(Status.OK);
            decryptResponse.setData(entity);
        } catch ( Exception e ) {
            decryptResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            decryptResponse.setMessage(e.getMessage());
            LOGGER.error("调用decryptUserInfo接口异常", e);
        }
        return decryptResponse;
    }

    /**
     * 方法名：检验Json
     * <p>功能说明：</p>
     * @param ignoreNullOrEmpty
     * @param args
     */
    public static void verifyJson(boolean ignoreNullOrEmpty, String... args)
    {
        ParameterCheckUtil.verifyJson(ignoreNullOrEmpty, args);
    }

    @Autowired
    private AmpResetfulService ampResetfulService;

    @ResponseBody
    @RequestMapping(value = { "/getFavoriteApps.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "查询用户已收藏的服务", realPath = "/restful/getFavoriteApps.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public AppResponse getFavoriteApps(@RequestBody AppTypeRequest appTypeRequest) {
        AppResponse appResponse = new AppResponse();
        try {
            verifyInput(appTypeRequest.getUserId());
            String appConditionRequestJson = FastJsonUtils.toJson(appTypeRequest);
            verifyJson(false, appConditionRequestJson);
            List<FaveriteServiceInfo> serviceList = ampResetfulService.getFavoriteService(appTypeRequest);
            List<AppEntity> entityList = ampResetfulService.buildAppEntity(serviceList);
            appResponse.setDatas(entityList.toArray(new AppEntity[0]));
            appResponse.setStatus(Status.OK_CODE);
            appResponse.setMessage(Status.OK);
        } catch (Exception e) {
            appResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            appResponse.setMessage(Status.SYSTEM_ERROR + e.getMessage());
            LOGGER.error("调用接口getFavoriteApps失败", e);
        }
        return appResponse;
    }

    @ResponseBody
    @RequestMapping(value = { "/queryGroupByUserAndApp.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "根据userId和appId获取已授权的用户组", realPath = "/restful/queryGroupByUserAndApp.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public GetGroupByUserAndAppResponse queryGroupByUserAndApp(@RequestBody GetGroupByUserAndAppRequest request) {
        GetGroupByUserAndAppResponse response = new GetGroupByUserAndAppResponse();
        try {
            String getGroupByUserAndAppRequestJson = FastJsonUtils.toJson(request);
            verifyJson(false, getGroupByUserAndAppRequestJson);
            verifyInput(request.getAppId(), request.getUserId());
            List<UserGroup> groupList = ampResetfulService.getGroupByAppAndUser(request);
            response.setDatas(groupList.toArray(new UserGroup[0]));
            response.setStatus(Status.OK_CODE);
            response.setMessage(Status.OK);
        } catch (Exception e) {
            response = new GetGroupByUserAndAppResponse();
            response.setStatus(Status.SYSTEM_ERROR_CODE);
            response.setMessage(Status.SYSTEM_ERROR + e.getMessage());
            LOGGER.error("调用接口queryGroupByUserAndApp失败", e);
        }
        return response;
    }
    @ResponseBody
    @RequestMapping(value = { "/queryAppListByCondition.do" }, produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @OpenGatewayApi(name = "获取所有已经接入到学校的应用", realPath = "/restful/queryAppListByCondition.do", apiClassify = InsideApiClassifyEmums.API_INSIDE_CLASSIFY_V1_AMP)
    public AppListResponse queryAppListByCondition(@RequestBody AppListConditionRequest appListConditionRequest) {
        AppListResponse appListResponse = new AppListResponse();
        try {
            String appListConditionRequestJson = FastJsonUtils.toJson(appListConditionRequest);
            verifyJson(false, appListConditionRequestJson);
            List<AppInfoForOA> list = ampResetfulService.queryServiceByCondition(appListConditionRequest);
            appListResponse.setDatas(list.toArray(new AppInfoForOA[0]));
            appListResponse.setStatus(Status.OK_CODE);
            appListResponse.setMessage(Status.OK);
        } catch (Exception e) {
            appListResponse = new AppListResponse();
            appListResponse.setStatus(Status.SYSTEM_ERROR_CODE);
            appListResponse.setMessage(Status.SYSTEM_ERROR + e.getMessage());
            LOGGER.error("调用接口queryAppListByCondition失败", e);
        }
        return appListResponse;
    }

}
