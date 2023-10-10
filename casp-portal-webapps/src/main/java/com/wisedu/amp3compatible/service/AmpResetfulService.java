package com.wisedu.amp3compatible.service;

import com.wisedu.amp3compatible.model.*;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.api.model.amp.AppQueryItem;
import com.wisedu.minos.api.model.amp.DubboAmpSearchRequest;
import com.wisedu.minos.api.model.amp.DubboAmpSearchResponse;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.model.*;

import com.wisedu.minos.casp.portal.service.impl.AppAppraiseService;
import com.wisedu.minos.casp.portal.service.impl.HomeService;
import com.wisedu.minos.casp.portal.utils.ChinesePinyinUtil;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AmpResetfulService {

    private final static Logger logger = LogManager.getLogger(AmpResetfulService.class);

    public List<String> readStationFronRunPlatform(List<RunPlatform> runPlatformList){
        List<String> stations = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(runPlatformList)) {
            for (RunPlatform runPlatform : runPlatformList) {
                if(runPlatform.equals(RunPlatform.PC_APP)){
                    if(!stations.contains("0")) {
                        stations.add("0");
                    }
                }
                if(runPlatform.equals(RunPlatform.MOBILE_APP)){
                    if(!stations.contains("1")) {
                        stations.add("1");
                    }
                }
                if(runPlatform.equals(RunPlatform.ALL)){
                    if(!stations.contains("0")) {
                        stations.add("0");
                    }
                    if(!stations.contains("1")) {
                        stations.add("1");
                    }
                }
            }
        }else{
            //不传默认查pc应用
            stations.add("0");
        }
        return stations;
    }

    @DubboReference
    private UserService userService;

    private DubboUserInfo getUserByAccount(String userId){
        DubboUserInfo userInfo = null;
        if(StringUtils.isNotEmpty(userId)) {
            DubboUserSearchByAccountReq req = new DubboUserSearchByAccountReq();
            req.setAccount(userId);
            DubboUserInfoResp dubboUserInfoResp = userService.searchByAccount(req);
            List<DubboUserInfo> data = dubboUserInfoResp.getData();
            if(CollectionUtils.isNotEmpty(data)){
                userInfo = data.get(0);
            }
        }
        return userInfo;
    }

    @Autowired
    private HomeService homeService;
    @DubboReference
    AppServiceService appServiceService;

    private DubboServiceInfo getServiceByWid(String serviceWid){
        DubboServiceInfoApiResp resp = homeService.queryServiceByWid(serviceWid, "");
        return CollectionUtils.isEmpty(resp.getData()) ? null : resp.getData().get(0);
    }

    public List<AppInfoForOA> queryServiceByCondition(AppListConditionRequest request){
        if(null == request.getAppType()){
            throw new MinosException("请传入应用类型");
        }
        if(0 != request.getAppType() && 1 != request.getAppType() && 2 != request.getAppType()){
            throw new MinosException("应用类型的值必须是0,1,2");
        }
        DubboAmpSearchRequest dubboRequest = buildSearchDubboRequest(request);
        DubboAmpSearchResponse dubboResponse = appServiceService.queryAmpServiceList(dubboRequest);
        List<AppInfoForOA> result = new ArrayList<>();
        List<AppQueryItem> items = dubboResponse.getDatas();
        if(CollectionUtils.isNotEmpty(items)){
            for(AppQueryItem item : items){
                AppInfoForOA app = new AppInfoForOA();
                app.setAccessUrl(item.getAccessUrl());
                app.setAppIcon(item.getAppIcon());
                app.setAppId(item.getAppId());
                app.setAppName(item.getAppName());
                app.setAppShortName("");
                app.setIsMobileApp(item.getIsMobileApp());
                app.setIsPcApp(item.getIsPcApp());
                List<LabelEntity> entityList = buildLables(item.getServiceTypes());
                app.setServiceTypes(entityList.toArray(new LabelEntity[0]));
                result.add(app);
            }
        }
        return result;
    }

    private List<LabelEntity> buildLables(List<com.wisedu.minos.api.model.amp.Category> categoryList){
        List<LabelEntity> entityList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(categoryList)){
            for(com.wisedu.minos.api.model.amp.Category category:categoryList){
                LabelEntity entity = new LabelEntity();
                entity.setLabelId(category.getCategoryId());
                entity.setLabelName(category.getCategoryName());
                entityList.add(entity);
            }
        }
        return entityList;
    }
    private DubboAmpSearchRequest buildSearchDubboRequest(AppListConditionRequest request){
        DubboAmpSearchRequest request1 = new DubboAmpSearchRequest();
        if(request.getAppType() == 0){
            request1.setStation(2);
        }
        if(request.getAppType() == 1){
            request1.setStation(0);
        }
        if(request.getAppType() == 2){
            request1.setStation(1);
        }
        if(null != request.getAppIds() && request.getAppIds().length > 0){
            request1.setServiceIds(Arrays.asList(request.getAppIds()));
        }
        if(StringUtils.isNotEmpty(request.getServiceTypeName())){
            request1.setServiceTypeName(request.getServiceTypeName());
        }
        return request1;
    }
    public List<UserGroup> getGroupByAppAndUser(GetGroupByUserAndAppRequest request){
        if(StringUtils.isEmpty(request.getUserId())){
            throw new MinosException("传入的userId不可为空");
        }
        DubboUserInfo userInfo = getUserByAccount(request.getUserId());
        if(userInfo == null){
            throw new MinosException("根据传入的用户id找不到对应用户");
        }
        if(StringUtils.isEmpty(request.getAppId())){
            throw new MinosException("传入的appId不可为空");
        }
        DubboServiceInfo serviceInfo = getServiceByWid(request.getAppId());
        if(serviceInfo == null){
            throw new MinosException("根据传入的服务wid找不到对应服务");
        }
        DubboUserServiceGrantRequest dubboRequest = new DubboUserServiceGrantRequest();
        dubboRequest.setServiceWid(request.getAppId());
        dubboRequest.setUserId(request.getUserId());
        DubboModelInfoResponse dubboResponse = appServiceService.queryUserServiceGrantedGroup(dubboRequest);
        List<UserGroup> groupList = new ArrayList<>();
        if(null != dubboResponse && CollectionUtils.isNotEmpty(dubboResponse.getData())){
            for(DubboModelInfo model : dubboResponse.getData()){
                UserGroup group = new UserGroup();
                group.setGroupId(model.getModelWid());
                group.setGroupName(model.getModelName());
                group.setDomainId(model.getDomainId());
                group.setDomainName(model.getDomainName());
                groupList.add(group);
            }
        }
        return groupList;
    }

    private AppInfoRequest getAppInfoRequest(String userWid, String station) {
        AppInfoRequest appInfoRequest = new AppInfoRequest();
        appInfoRequest.setUserId(userWid);
        appInfoRequest.setServiceStation(station);
        return appInfoRequest;
    }

    /**
     * 查我收藏的服务列表
     * @param appTypeRequest
     * @return
     */
    public List<FaveriteServiceInfo> getFavoriteService(AppTypeRequest appTypeRequest){
        DubboUserInfo userInfo = getUserByAccount(appTypeRequest.getUserId());
        if(null == userInfo){
            throw new MinosException("找不到对应的用户");
        }
        List<FaveriteServiceInfo> appList = new ArrayList<>();

        List<String> stations = readStationFronRunPlatform(appTypeRequest.getRunPlatformList());
        if (CollectionUtils.isNotEmpty(stations)) {
            String station = stations.get(0);
            if(stations.contains("1") && stations.contains("0")){
                station = "";
            }

            AppInfoRequest appInfoRequest = getAppInfoRequest(userInfo.getWid(), station);
            HttpEntity entity = new HttpEntity(appInfoRequest);
            ServiceFaveriteResponse response = null;
            try {
                response = RestTemplateUtils.post("/serviceFavorite/userServiceFavoriteList", entity
                        , ServiceFaveriteResponse.class).getBody();
            } catch ( Exception e ) {
                logger.error("调用收藏应用查询接口失败，原因：", e);
            }
            if ( null != response && CollectionUtils.isNotEmpty(response.getData())) {
                appList = response.getData();
            }

        }

        return appList;
    }
    @Autowired
    AppAppraiseService appAppraiseService;

    public Map<String, AppAppraiseSummaryVo> buildServiceScoreMap(List<FaveriteServiceInfo> serviceInfoList){
        List<String> serviceWids = serviceInfoList.stream().map(e -> e.getServiceWid()).collect(Collectors.toList());
        List<AppAppraiseSummaryVo> scoreList = appAppraiseService.queryAppAppraiseSummary(serviceWids);
        Map<String, AppAppraiseSummaryVo> scoreMap = CollectionUtils.isEmpty(scoreList) ? new HashMap<>()
                :scoreList.stream().collect(Collectors.toMap(e -> e.getServiceWid(), e -> e));
        return scoreMap;
    }
    public List<Category> buildCategoryList(List<ServiceClassifyInfo> classifyInfoList){
        List<Category> categoryList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(classifyInfoList)){
            for(ServiceClassifyInfo classifyInfo: classifyInfoList) {
                Category category = new Category();
                category.setCategoryId(classifyInfo.getWid());
                category.setCategoryName(classifyInfo.getClassifyName());
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    public List<AppEntity> buildAppEntity(List<FaveriteServiceInfo> appList){
        List<AppEntity> entityList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(appList)){
            Map<String, AppAppraiseSummaryVo> scoreMap = buildServiceScoreMap(appList);
            for(FaveriteServiceInfo appInfo : appList){
                AppEntity entity = new AppEntity();
                entity.setAppId(appInfo.getServiceId());
                entity.setAppWid(appInfo.getServiceWid());
                entity.setAppName(appInfo.getServiceName());
                entity.setAppIcon(appInfo.getIconLink());
//                entity.setVersion();
                entity.setCategoryList(buildCategoryList(appInfo.getClassifyInfos()));
                AppAppraiseSummaryVo vo = scoreMap.get(appInfo.getServiceWid());
                entity.setAppScore(vo == null ? 0 : vo.getAvgScore());
                entity.setCollectCount(null == appInfo.getFavoriteCount() ? 0 : appInfo.getFavoriteCount());
                entity.setIsCanUse(appInfo.getPermission() ? 1:0);
//                entity.setDeployPrefix();
                if(appInfo.getServiceStation() == 0 || appInfo.getServiceStation() == 2) {
                    entity.setEntranceUrl(appInfo.getPcAccessUrl());
                }else if(appInfo.getServiceStation() == 1){
                    entity.setEntranceUrl(appInfo.getMobileAccessUrl());
                }
                entity.setFirstChar(ChinesePinyinUtil.headChar(appInfo.getServiceName()));
                entity.setStatus(1);
                if(appInfo.getServiceStation() == 0){
                    entity.setIsPcApp(1);
                    entity.setIsMobileApp(0);
                }
                if(appInfo.getServiceStation() == 1){
                    entity.setIsPcApp(0);
                    entity.setIsMobileApp(1);
                }
                if(appInfo.getServiceStation() == 2){
                    entity.setIsPcApp(1);
                    entity.setIsMobileApp(1);
                }
                entity.setIsPcCard(0);
                entity.setIsMobileCard(0);
                entityList.add(entity);
            }
        }
        return entityList;
    }
}
