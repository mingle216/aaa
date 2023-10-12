package com.wisedu.amp.card.sys.cqdetail.plugin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.wisedu.amp.card.sys.cqdetail.model.*;
import com.wisedu.amp.card.sys.cqdetail.model.appraise.ItemAppraiseDetailSearchForPortalResponse;
import com.wisedu.amp.card.sys.cqdetail.model.appraise.ItemAppraiseDetailSearchRequest;
import com.wisedu.amp.card.sys.cqdetail.util.ResultUtil;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.model.AppraiseDetail;
import com.wisedu.minos.casp.portal.model.AppraiseRequest;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther:hlchen02
 * @Date:2020/8/17
 * @Description:com.wisedu.amp.card.sys.hotApp.plugin
 * @Version:1.0
 */
@MinosSPI
public class DetailsOfServiceItemsCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(DetailsOfServiceItemsCard.class);


    @Override
    public String getCardId () {
        return "CUS_CARD_CQDETAIL";
    }


    @Override
    public void destroy () {
        logger.debug("destroy..");
    }


    @Override
    public Object execDispatcher (CardAjaxRequest request) {
        Object result = null;
        switch ( request.getMethod() ) {
            case "renderData":
                result = this.renderData2(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "evaluation":
                result = this.evaluation(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "evaluationResponse":
                result = this.evaluationResponse(request);
                break;
                case "getAppraise":
                result = this.getAppraisement(request);
                break;
            default:
        }

        return result;
    }

    private AppInfoResponse evaluationResponse (CardAjaxRequest request) {
        DetailRequest detailRequest = new DetailRequest();
        if ( StringUtil.isNotEmpty(request.getParam().get("wid")) ) {
            String serviceItemWid = request.getParam().get("wid");
            detailRequest.setServiceItemWid(serviceItemWid);
        } else {
            detailRequest.setServiceItemWid("782240587926470656");
        }

        if ( StringUtil.isNotEmpty(request.getParam().get("lang")) ) {
            String langCountry = request.getParam().get("lang");
            detailRequest.setLangCountry(langCountry);
        } else {
            detailRequest.setLangCountry("String");
        }
        HttpEntity entity = new HttpEntity(detailRequest);
        AppInfoResponse response = null;
        //获取热门应用查询接口地址
        logger.debug("开始调用服务事项详情取评价接口...,url=/coosk/serviceItem/selectById,参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/coosk/serviceItem/selectById", entity, AppInfoResponse.class).getBody();
            logger.debug("调用服务事项详情取评价接口成功，返回结果：" + JSON.toJSONString(response));
        } catch ( Exception e ) {
            logger.error("调用服务事项详情取评价接口失败", e);
        }
        return response;
    }

    /**
     * 废弃，以后不再维护。使用ItemAppraiseController中的saveAppraiseWithPic替代
     * 不删除是为了定制卡片
     * @param request
     * @return
     */
    @Deprecated
    private AmpBaseResponse evaluation (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        if ( null == user || StringUtil.isEmpty(user.getWid()) ) {
            logger.error("评价出错：用户获取失败！");
            AmpBaseResponse ampBaseResponse = new AmpBaseResponse();
            ampBaseResponse.setErrcode("500");
            ampBaseResponse.setErrmsg("评价出错：用户获取失败！");
            return ampBaseResponse;
        }
        AppraiseRequest appraiseRequest = new AppraiseRequest();
        appraiseRequest.setUserId(user.getWid());
        appraiseRequest.setUserName(user.getUserName());
        appraiseRequest.setServiceWid(request.getParam().get("appId"));
        appraiseRequest.setSuggest(request.getParam().get("suggest"));
        AppraiseDetail appraiseDetail = JSON.parseObject(request.getParam().get("appraiseDetail"), AppraiseDetail.class);
        appraiseRequest.setAppraiseDetail(appraiseDetail);
        HttpEntity entity = new HttpEntity(appraiseRequest);

        AmpBaseResponse response = null;
        logger.debug("调用评价服务事项接口,usr = /itemAppraise/dimension/save" + "参数 = " + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/coosk/itemAppraise/dimension/save", entity, EvaluationResponse.class).getBody();
            logger.debug("调用评价服务事项接口返回结果..." + JSON.toJSONString(response));
        } catch ( Exception e ) {
            logger.error("调用评价服务事项接口失败...,返回错误信息", e);
        }
        return response;
    }

    private AmpBaseResponse collectApp (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        if ( null == user || StringUtil.isEmpty(user.getWid()) ) {
            logger.error("收藏出错：用户获取失败！");
            AmpBaseResponse ampBaseResponse = new AmpBaseResponse();
            ampBaseResponse.setErrcode("500");
            ampBaseResponse.setErrmsg("收藏出错：用户获取失败！");
            return ampBaseResponse;
        }
        CollectAppRequest collectAppRequest = new CollectAppRequest();
        collectAppRequest.setUserId(user.getWid());
        collectAppRequest.setItemId(request.getParam().get("appId"));
        collectAppRequest.setFlag(Integer.parseInt(request.getParam().get("flag")));
        HttpEntity entity = new HttpEntity(collectAppRequest);

        AmpBaseResponse response = null;
        logger.debug("调用应用收藏/取消收藏接口,/itemManager/favoriteItem" + "参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/coosk/itemManager/favoriteItem", entity, EvaluationResponse.class).getBody();
            logger.debug("调用应用收藏/取消收藏接口返回结果..." + JSON.toJSONString(response));
        } catch ( Exception e ) {
            logger.error("调用应用收藏/取消收藏接口失败...,返回错误信息", e);
        }
        return response;
    }

    /**
     * 后台配置重构时，新增或重写的方法
     */

    private Object renderData2 (CardAjaxRequest request) {
        logger.debug("Prepare to call the DetailsOfServiceItemsCard renderData method ...");

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        Map<String, Object> configMap = getConfig(request);
        logger.debug("DetailsOfServiceItemsCard的配置信息:" + configMap);

        String serviceItemWid = request.getParam().get("wid");
        String langCountry = request.getParam().get("lang");
        AppInfo serviceItemInfo = null;

        AppInfoResponse response = getServiceItemDetails(serviceItemWid, langCountry);

        Object itemNameObj = "";
        //评分
        double score = 0.0;
        List<LinkInfo> linkService = null;
        int linkSize = 0;
        String showOnlineButton = "0";
        //返回结果
        Map<String, Object> results = new HashMap<>();

        if ( null != response && null != response.getData() ) {
            if ( (response.getData().getIsEnabled() != null && response.getData().getIsDeleted() != null) &&
                    (0 == response.getData().getIsEnabled() || 1 == response.getData().getIsDeleted()) ) {
                String itemName = response.getData().getItemName();
//                itemNameObj = new ScriptDefenseFormat().format(itemName, null);
                results.put("serviceItemWid", serviceItemWid);
                results.put("itemName", itemName);
                results.put("itemNameObj", itemNameObj);
                results.put("appraiseNames", response.getData().getAppraiseNames());
                return results;
            }

            serviceItemInfo = response.getData();
//            if( org.apache.commons.collections.CollectionUtils.isNotEmpty(serviceItemInfo.getIndptModuls())){
//                List<IndptModul> indptModuls = serviceItemInfo.getIndptModuls();
//                indptModuls.forEach(e->{
//
//                });
//            }
            String itemName = serviceItemInfo.getItemName() == null ? "" : serviceItemInfo.getItemName();
            itemNameObj = itemName;

            serviceItemInfo.setItemName(itemName);
            if ( null != serviceItemInfo.getAppraiseMark() ) {
                score = Double.parseDouble(serviceItemInfo.getAppraiseMark());
            }


            linkService = getLinkService(response.getData().getLinkServices());
            if(!CollectionUtils.isEmpty(linkService)) {
                //处理登录前和登录后 服务是否可见
                showOnlineButton = isShowOnlineButton(linkService, user);
                linkService = linkService.stream().sorted(Comparator.comparing(LinkInfo::getOrderNumber)).collect(Collectors.toList());
            }

        }

        //是否收藏标识
        boolean favorite = false;
        //是否登录，为true跳转到登录界面
        boolean hasToLogin = true;
        if ( null == user || StringUtil.isEmpty(user.getWid()) ) {
            logger.error("未登录....");
        } else {
            hasToLogin = false;
            favorite = isFavorite(user, serviceItemWid);
        }

        CasProperties casProperties = ApplicationContextUtil.get(CasProperties.class);
        String redirectUrl = casProperties.getCas().getServerUrl() + "login?service=";

        String sdkAk = getBaiduSdk();

        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("config", configMap);
        results.put("serviceItemWid", serviceItemWid);
        results.put("favorite", favorite);
        results.put("hasToLogin", hasToLogin);
        results.put("redirectUrl", redirectUrl);
        results.put("linkService", linkService);
        results.put("linkSize", linkSize);
        results.put("appraiseMark", score);
        results.put("itemName", itemNameObj);
        results.put("serviceItemInfo", serviceItemInfo);
        results.put("akKey", sdkAk);
        results.put("showOnlineButton", showOnlineButton);

        return results;
    }

    private String getBaiduSdk(){
        String sdkAk = "false";
        SystemConfigEntity configEntity = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfig("baidu_sdk_ak");
        if ( null != configEntity && null != configEntity.getConfigValue() && ! "".equals(configEntity.getConfigValue()) ) {
            sdkAk = configEntity.getConfigValue();
        }
        return sdkAk;
    }

    private List<LinkInfo> getLinkService(List<String> link){
        List<LinkInfo> linkService = null;
        if ( !CollectionUtils.isEmpty(link) ) {
            Map<String,Integer> orderMap= Maps.newHashMap();
            for ( int i = 0 ; i <link.size()  ; i++ ) {
                orderMap.put(link.get(i),i);
            }
            linkService = getOnlineService(link);
            linkService.forEach(k->{
                k.setOrderNumber(orderMap.get(k.getWid()));
            });
        }

        return linkService;
    }

    /**
     * 在线办理按钮 0 不显示 1 置灰 2 可点击
     * 测试用例
     * 1、如果事项只关联了一个登录前不可见的服务，点击事项提示：当前暂无权限
     * 2、如果事项关联的服务部分登录前可见、部分不可见，点击事项弹出跳转服务弹框，弹框中只显示登录前可见的服务，不可见服务不显示
     * 3、如果事项关联的服务全部为登录前不可见服务，点击事项提示：当前暂无权限
     *
     * @param linkInfoList
     * @param user
     * @return
     */
    private String isShowOnlineButton (List<LinkInfo> linkInfoList, UserInfo user) {
        // 判断是否关联了服务，如果没有 在线办理按钮不显示
        if ( CollectionUtils.isEmpty(linkInfoList) ) {
            return "0";
        }
        List<String> linkInfoWids = linkInfoList.stream().map(LinkInfo::getWid).collect(Collectors.toList());
        String userAccount = ApplicationContextUtil.get(UserUtil.class).getUserAccount();
        List<String> grantServices = ApplicationContextUtil.get(ServiceApiService.class).getGrantServices(userAccount);
        List<String> visitedServices = ApplicationContextUtil.get(ServiceApiService.class).getVisitedServices(userAccount);

        // dubbo返回的用户可见的服务列表与事项关联的服务列表作交集
        List<String> canVisitedWids = CollectionUtil.intersection(linkInfoWids, visitedServices);
        // 交集后为空集，则表示没有可见的服务则在线办理置灰可点
        if ( CollectionUtils.isEmpty(canVisitedWids) ) {
            return "1";
        } else {
            // 交集后如果有可见服务，这些可见的服务再与用户可用的服务交集
            List<String> canUseWids = CollectionUtil.intersection(canVisitedWids, grantServices);
            // 如果有权限服务列表不为空，则在线办理不置灰可点
            if ( ! CollectionUtils.isEmpty(canUseWids) ) {
                return "2";
            } else {
                return "1";
            }
        }

    }

    /**
     * 查询查用检户某个服务是否有权限（服务批量）
     * 如果其中有一个为true ，则需要显示返回
     *
     * @param user
     * @param serviceWids
     */
    private List<ServiceGrantModel> isUserService (UserInfo user, List<String> serviceWids) {
        BatchCheckUserServiceGrantRequest batchCheckUserServiceGrantRequest = new BatchCheckUserServiceGrantRequest();
        batchCheckUserServiceGrantRequest.setUserId(user.getUserAccount());
        batchCheckUserServiceGrantRequest.setServiceWids(serviceWids);
        BatchCheckUserServiceGrantResponse batchCheckUserServiceGrantResponse = null;
        logger.debug("调用查用检户某个服务是否有权限接口..., 请求参数 = " + JSON.toJSONString(batchCheckUserServiceGrantRequest));
        try {
            batchCheckUserServiceGrantResponse = RestTemplateUtils.post("/service/batchCheckUserServiceGrant", batchCheckUserServiceGrantRequest, BatchCheckUserServiceGrantResponse.class).getBody();
            logger.debug("调用查用检户某个服务是否有权限接口成功..., 返回结果 = " + JSON.toJSONString(batchCheckUserServiceGrantResponse));
        } catch ( Exception e ) {
            logger.error("调用查用检户某个服务是否有权限接口失败", e);
        }
        if ( null != batchCheckUserServiceGrantResponse && null != batchCheckUserServiceGrantResponse.getData() ) {
            return batchCheckUserServiceGrantResponse.getData();
        }
        return new ArrayList<>();
    }

    private Map<String, Object> getConfig (CardAjaxRequest request) {
        Map<String, Object> configMap = new HashMap<>();
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if ( StringUtil.isNotEmpty(config) ) {
            config = JSON.parse(config).toString();
            configMap = ResultUtil.toMap(config);
        } else {
            String[] detailConfigure = { "evaluation", "favourite", "share" };
            String[] pageConfigure = { "icon", "favourite", "navbar" };
            configMap.put("detailConfigure", detailConfigure);
            configMap.put("pageConfigure", pageConfigure);
        }
        return configMap;
    }

    private AppInfoResponse getServiceItemDetails (String serviceItemWid, String langCountry) {
        DetailRequest detailRequest = new DetailRequest();
        if ( StringUtil.isNotEmpty(serviceItemWid) ) {
            detailRequest.setServiceItemWid(serviceItemWid);
        }
        if ( StringUtil.isNotEmpty(langCountry) ) {
            detailRequest.setLangCountry(langCountry);
        } else {
            detailRequest.setLangCountry("String");
        }
        HttpEntity entity = new HttpEntity<>(detailRequest);

        AppInfoResponse response = null;
        //获取热门应用查询接口地址
        try {
            response = RestTemplateUtils.post("/coosk/serviceItem/selectById", entity, AppInfoResponse.class).getBody();
        } catch ( Exception e ) {
            logger.error("调用服务事项详情接口失败", e);
        }
        return response;
    }

    private List<LinkInfo> getOnlineService (List<String> link) {
        List<LinkInfo> linkService = new ArrayList<>();
        LinkServicesResponse linkServicesResponse = null;
        LinkRequest links = new LinkRequest();
        links.setData(link);
        logger.debug("开始调用queryByWids接口...,url=/service/queryByWids, 参数=" + JSON.toJSONString(links));
        try {
            linkServicesResponse = RestTemplateUtils.post("/service/queryByWids", links, LinkServicesResponse.class).getBody();
            logger.debug("调用queryByWids接口成功，返回结果：" + JSON.toJSONString(linkServicesResponse));
        } catch ( Exception e ) {
            logger.error("调用queryByWids接口失败", e);
        }
        if ( null != linkServicesResponse && null != linkServicesResponse.getData() ) {
            linkService = linkServicesResponse.getData();
        }
        return linkService;
    }

    private boolean isFavorite (UserInfo user, String serviceItemWid) {
        boolean favorite = false;
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUserId(user.getWid());
        favoriteRequest.setUserAccount(user.getUserAccount());
//            favoite.setIsRelate("1");
        logger.debug("开始调用获取事项收藏列表接口...,参数=" + JSON.toJSONString(favoriteRequest));
        FavoriteResponse favoriteResponse = null;
        try {
            favoriteResponse = RestTemplateUtils.post("/coosk/userItemFavoriteList", favoriteRequest, FavoriteResponse.class).getBody();
            logger.debug("调用获取事项收藏列表接口成功..., 返回结果 = " + JSON.toJSONString(favoriteRequest));
        } catch ( Exception e ) {
            logger.error("调用获取事项收藏列表接口失败", e);
        }
        List<FavoriteInfo> favoriteInfoList;
        if ( null != favoriteResponse && null != favoriteResponse.getData() ) {
            favoriteInfoList = favoriteResponse.getData();
            for ( FavoriteInfo favoriteInfo : favoriteInfoList ) {
                if ( StringUtil.isNotEmpty(favoriteInfo.getItemWid()) && favoriteInfo.getItemWid().equals(serviceItemWid) ) {
                    favorite = true;
                    break;
                }
            }
        }
        return favorite;
    }

    public Object getAppraisement(CardAjaxRequest request){
        ItemAppraiseDetailSearchForPortalResponse response = null;
        ItemAppraiseDetailSearchRequest detailSearchRequest = processAppraiseParam(request);
        try {
            response = RestTemplateUtils.post("/coosk/itemAppraise/queryAppraisesForPortalList", detailSearchRequest, ItemAppraiseDetailSearchForPortalResponse.class).getBody();
            logger.debug("调用获取事项评价列表接口成功..., 返回结果 = " + JSON.toJSONString(detailSearchRequest));
        } catch ( Exception e ) {
            logger.error("调用获取事项评价列表接口失败", e);
        }
        if(null!=response && null != response.getData() ){
            Map<String,Object> appraiseMap=new HashMap<>();
            appraiseMap.put("pageSize",response.getPageSize());
            appraiseMap.put("total",response.getTotalSize());
            appraiseMap.put("pageNumber",response.getPageNumber());
            appraiseMap.put("data",response.getData().getItemDimenAppraiseDetailList());
            Map<String,Object> map=new HashMap<>();
            map.put("badAppraiseHidden", 0);
            map.put("appraiseDetail",appraiseMap);
            return map;
        }else{
            throw new BusinessException("获取事项详情接口");
        }
    }
    /***
     * processAppraiseParam
     * 处理评价参数<p/>
     *
     * @param request
     * @throws
     * @return com.wisedu.amp.card.sys.cqdetail.model.appraise.ItemAppraiseDetailSearchRequest
     * @date 2021/6/15 7:32
     */
    private ItemAppraiseDetailSearchRequest processAppraiseParam(CardAjaxRequest request){

        String itemWid = request.getParam().get("itemWid");
        //1 好评 2 差评 其他(0)全部
       int markType = Integer.parseInt(request.getParam().get("markType"));
       int pageNumber = Integer.parseInt(request.getParam().get("pageNumber"));
        int pageSize = Integer.parseInt(request.getParam().get("pageSize"));
        ItemAppraiseDetailSearchRequest appraiseDetailSearchRequest = new ItemAppraiseDetailSearchRequest();
        appraiseDetailSearchRequest.setItemWid(itemWid);
        appraiseDetailSearchRequest.setMarkType(markType);
        appraiseDetailSearchRequest.setPageNumber(pageNumber);
        appraiseDetailSearchRequest.setPageSize(pageSize);
        appraiseDetailSearchRequest.setLang(request.getParam().get("lang"));
        return appraiseDetailSearchRequest;
    }
}
