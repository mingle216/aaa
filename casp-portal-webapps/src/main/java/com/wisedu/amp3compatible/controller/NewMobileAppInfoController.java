package com.wisedu.amp3compatible.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wisedu.amp3compatible.util.ConfigUtil;
import com.wisedu.amp3compatible.util.MobileCodeUtil;
import com.wisedu.amp3compatible.util.NewMobileClientDataAdapter;
import com.wisedu.minos.api.data.ServiceAmpMobileService;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.dao.entity.PageCardConfigEntity;
import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PageCardConfigMapper;
import com.wisedu.minos.casp.portal.dao.mapper.PageInfoMapper;
import com.wisedu.minos.casp.portal.dao.mapper.ShowProgrammeMapper;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 从amp 3.0.3_TR7 NewMobileAppInfoController迁移过来
 * 为了兼容今日校园接口
 */
@Controller
@RequestMapping("/newmobile/client")
public class NewMobileAppInfoController {

    private static final Logger logger = LoggerFactory.getLogger(NewMobileAppInfoController.class);


    @Autowired
    MobileCodeUtil mobileCodeUtil;


    @DubboReference
    ServiceAmpMobileService serviceAmpMobileService;

    @Autowired
    PageCardConfigMapper pageCardConfigMapper;

    @Autowired
    private ShowProgrammeMapper showProgrammeMapper;

    @Autowired
    private PageInfoMapper pageInfoMapper;

    public static final String UN_CATEGORY_ID = "un-categoryId";
    public static final String UN_CATEGORY_NAME = "未分类";


    /**
     * 用户有权看到的应用列表,输出结果按照category分类,可按关键字搜索
     * {
     * "total": 7,
     * "message": "",
     * "data":[
     * {
     * "categoryName": "迎新服务",
     * "apps": [
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * openUrl: "打开地址，绝对路径"
     * }
     * ],
     * "categoryId": "2__9"
     * },
     * {
     * "categoryName": "未分类",
     * "apps":[
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * openUrl: "打开地址，绝对路径"
     * },
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * openUrl: "打开地址，绝对路径"
     * }
     * ],
     * "categoryId": "un-categoryId"
     * }
     * ],
     * "code": "200"
     * }
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userAppListGroupByCategory", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> userAppListGroupByCategory(HttpServletRequest request,
                                                          @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                                          @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "16") int pageSize,
                                                          @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        logger.debug("准备调用MobileAppInfoController.userAppList方法，传入参数  categoryId：{}; keyword: {}; pageNumber: {}; pageSize:{}",
                categoryId, keyword, pageNumber, pageSize);
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            DubboAmpMobileServiceRequest ampMobileServiceRequest = new DubboAmpMobileServiceRequest();
            ampMobileServiceRequest.setPageSize(pageSize);
            ampMobileServiceRequest.setPageNumber(pageNumber);
            ampMobileServiceRequest.setUserAccount(user.getUserAccount());
            ampMobileServiceRequest.setCategoryId(categoryId);
            ampMobileServiceRequest.setKeyword(keyword);
            //设置推荐服务
            ampMobileServiceRequest.setRecommendServiceWids(getRecommendServiceList());
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.userAppList(ampMobileServiceRequest);
            return toClientAppListGroupByCategory(response.getData(), categoryId);
        } catch (Exception e) {
            logger.error("查询用户有权看到的应用列表出错，原因: ", e);
            return responseError(e.getMessage());
        }
    }


    /**
     * 查看用户有权看到的应用列表
     * <p>
     * <p>
     * code：200,
     * message: "",
     * data: [
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * openUrl: "打开地址，绝对路径"
     * },
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * openUrl: "打开地址，绝对路径"
     * }
     * ],
     * total: 10
     */
    @ResponseBody
    @RequestMapping(value = "/userAppList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> userAppList(HttpServletRequest request,
                                           @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        logger.debug("准备调用MobileAppInfoController.userAppList方法，传入参数  categoryId：{}; keyword: {}; pageNumber: {}; pageSize:{}",
                categoryId, keyword, pageNumber, pageSize);
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            DubboAmpMobileServiceRequest ampMobileServiceRequest = new DubboAmpMobileServiceRequest();
            ampMobileServiceRequest.setPageSize(pageSize);
            ampMobileServiceRequest.setPageNumber(pageNumber);
            ampMobileServiceRequest.setUserAccount(user.getUserAccount());
            if (!UN_CATEGORY_ID.equals(categoryId)) {
                ampMobileServiceRequest.setCategoryId(categoryId);
            }
            ampMobileServiceRequest.setKeyword(keyword);
            //设置推荐服务
            ampMobileServiceRequest.setRecommendServiceWids(getRecommendServiceList());
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.userAppList(ampMobileServiceRequest);
            //参数转换
            List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
            return dataTableData(clientAppData, response.getTotalSize().intValue());
        } catch (Exception e) {
            logger.error("查询用户有权看到的应用列表出错，原因: ", e);
            return responseError(e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/guestAppList.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> guestAppList(HttpServletRequest request,
                                            @RequestParam(value = "categoryId", defaultValue = "") String categoryId,
                                            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        logger.debug("准备调用MobileAppInfoController.userAppList方法，传入参数  categoryId：{}; keyword: {}; pageNumber: {}; pageSize:{}",
                categoryId, keyword, pageNumber, pageSize);
        try {
            DubboAmpMobileServiceRequest ampMobileServiceRequest = new DubboAmpMobileServiceRequest();
            ampMobileServiceRequest.setPageSize(pageSize);
            ampMobileServiceRequest.setPageNumber(pageNumber);
            if (!UN_CATEGORY_ID.equals(categoryId)) {
                ampMobileServiceRequest.setCategoryId(categoryId);
            }
            ampMobileServiceRequest.setKeyword(keyword);
            //设置推荐服务
            ampMobileServiceRequest.setRecommendServiceWids(getRecommendServiceList());
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.guestAppList(ampMobileServiceRequest);
            //参数转换
            List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
            return dataTableData(clientAppData, response.getTotalSize().intValue());
        } catch (Exception e) {
            logger.error("查询用户有权看到的应用列表出错，原因: ", e);
            return responseError(e.getMessage());
        }
    }


    /**
     * 用户收藏的所有应用
     * {
     * code: 200,
     * message: "",
     * data: [
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * openUrl: "打开地址，绝对路径"
     * },
     * {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * openUrl: "打开地址，绝对路径"
     * }
     * ]
     * }
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userStoreAppList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> userStoreAppList(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "65535") int limit) {

        String msg = "";
        logger.debug("准备调用MobileAppInfoController.userStoreAppList方法");
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            DubboAmpMobileServiceRequest ampMobileServiceRequest = new DubboAmpMobileServiceRequest();
            ampMobileServiceRequest.setUserAccount(user.getUserAccount());
            ampMobileServiceRequest.setLimit(limit);
            ampMobileServiceRequest.setRecommendServiceWids(getRecommendServiceList());
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.userStoreAppList(ampMobileServiceRequest);
            List<Map<String, Object>> storeAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
            return responseData(storeAppData);
        } catch (Exception e) {
            logger.error("获取用户收藏的应用和推荐应用失败，原因： ", e);
        }

        return responseError(msg, null);
    }


    /**
     * 用户收藏应用
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/storeApp", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> storeApp(HttpServletRequest request, String appId) {
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            logger.debug("\"准备调用MobileAppInfoController.storeApp方法，参数 appId: {}, userId: {}", appId, user.getUserAccount());
            serviceAmpMobileService.storeApp(user.getUserAccount(), appId);
            return responseData(null);
        } catch (Exception e) {
            logger.error("收藏应用失败，原因：", e);
            return responseError(e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/cancelStoreApp", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> cancelStoreApp(HttpServletRequest request, String appId) {
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            logger.debug("准备调用MobileAppInfoController.cancelStoreApp方法，参数 appId: {}, userId: {}", appId, user.getUserAccount());
            serviceAmpMobileService.cancelStoreApp(user.getUserAccount(), appId);
            return responseData(null);
        } catch (Exception e) {
            logger.error("取消收藏应用失败，原因：", e);
            return responseError(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/saveStoreAppPosition", method = {RequestMethod.POST})
    public Map<String, Object> saveStoreAppPosition(HttpServletRequest request, @RequestParam(value = "userFavApps") String userFavApps) {
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            logger.debug("准备调用NewMobileAppInfoController.saveStoreAppPosition，参数 userFavApps: {}", userFavApps);
            if (StringUtils.isEmpty(userFavApps)) {
                return responseError("收藏服务列表为空");
            }
            DubboBatchServiceFavoriteRequest favoriteRequest = new DubboBatchServiceFavoriteRequest();
            favoriteRequest.setUserAccount(user.getUserAccount());
            favoriteRequest.setServiceIds(Lists.newArrayList(userFavApps.split(",")));
            serviceAmpMobileService.saveStoreAppPosition(favoriteRequest);
            return responseData(null);
        } catch (Exception e) {
            logger.error("存储收藏应用的位置失败，原因：", e);
            return responseError(e.getMessage());
        }

    }


    /**
     * 根据应用id得到应用详细信息
     * {
     * code：200,
     * message: "",
     * data: {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * publishTime:"发布时间long",
     * appPic:[
     * {picUrl:"图片地址"},
     * {picUrl:"图片地址"}
     * ],
     * isAssess:"是否已评价0：否；1：是",
     * openUrl: "打开地址，绝对路径"
     * }
     * }
     *
     * @param request
     * @param appId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAppInfoById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAppInfoById(HttpServletRequest request,
                                              @RequestParam(value = "appId", required = true) String appId) {
        logger.debug("准备调用MobileAppInfoController.getAppInfoById方法，参数 appId: {}", appId);
        String msg = "";
        try {
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.getAppInfoById(appId);
            if (CollectionUtils.isEmpty(response.getData())) {
                logger.error("不存在id为{}的应用", appId);
                return responseError(6);
            } else {
                List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
                clientAppData.get(0).put("isAssess", false);
                return responseData(clientAppData.get(0));
            }
        } catch (Exception e) {
            logger.error("根据主键查询应用详情失败，原因：", e);
            return responseError(msg, null);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getAppInfoByIdBatch", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAppInfoByIdBatch(HttpServletRequest request,
                                                   @RequestParam(value = "appIds", required = true) String appIds) {
        logger.debug("准备调用NewMobileAppInfoController.getAppInfoByIdBatch，参数 appIds: {}", appIds);
        String msg = "";
        List<String> appIdList = Arrays.asList(appIds.split(","));
        try {
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.getAppInfoByIdBatch(appIdList);
            if (CollectionUtils.isEmpty(response.getData())) {
                logger.error("不存在id为{}的应用", appIds);
                return responseError(6);
            } else {
                List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
                for (Map<String, Object> map : clientAppData) {
                    map.put("isAssess", false);
                }
                return responseData(clientAppData);
            }
        } catch (Exception e) {
            logger.error("根据主键查询应用详情失败，原因：", e);
            return responseError(msg, null);
        }

    }

    /**
     * 根据应用打开地址得到应用详细信息
     * {
     * code：200,
     * message: "",
     * data: {
     * appId: 1,
     * iconUrl: "图标地址，相对路径",
     * name: "应用名称",
     * version:"版本信息",
     * appSize:"应用大小，字节数",
     * desc:"描述",
     * appType:"1:hybird,2:h5;3:原生应用;4:第三方应用（微信）",
     * assessMark:"平均分",
     * useCount:"使用数",
     * publishTime:"发布时间long",
     * appPic:[
     * {picUrl:"图片地址"},
     * {picUrl:"图片地址"}
     * ],
     * isAssess:"是否已评价0：否；1：是",
     * openUrl: "打开地址，绝对路径"
     * }
     * }
     *
     * @param request
     * @param openUrl
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAppInfoByOpenUrl", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getAppInfoByOpenUrl(HttpServletRequest request,
                                                   @RequestParam(value = "openUrl", required = true) String openUrl) {
        logger.debug("准备调用MobileAppInfoController.getAppInfoByOpenUrl方法，参数 openUrl: {}", openUrl);
        try {
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.getAppInfoByOpenUrl(openUrl);
            if (CollectionUtils.isEmpty(response.getData())) {
                logger.error("不存在openUrl为{}的应用", openUrl);
                return responseError(6);
            } else {
                List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
                Map<String, Object> resultMap = clientAppData.get(0);
                if (resultMap.get("openUrl") == null) {
                    resultMap.put("openUrl", openUrl);
                }
                resultMap.put("isAssess", false);
                return responseData(resultMap);
            }
        } catch (Exception e) {
            logger.error("根据应用打开地址查询应用详情失败，原因：", e);
            return responseError(e.getMessage());
        }
    }


    /**
     * 推荐服务接口
     * {
     * code: 200,
     * message: "",
     * data: [
     * {
     * appId: 1,
     * iconUrl: "图标地址，绝对路径",
     * name: "应用名称",
     * openUrl: "打开地址，绝对路径"
     * },
     * {
     * appId: 1,
     * iconUrl: "图标地址，绝对路径",
     * name: "应用名称",
     * openUrl: "打开地址，绝对路径"
     * }
     * ]
     * }
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/recommendAppList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> recommendAppList(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        logger.debug("准备调用MobileAppInfoController.recommendAppList方法");
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

            DubboAmpMobileServiceRequest ampMobileServiceRequest = new DubboAmpMobileServiceRequest();
            ampMobileServiceRequest.setRecommendServiceWids(getRecommendServiceList());
            ampMobileServiceRequest.setUserAccount(user.getUserAccount());
            DubboAmpMobileServiceResponse response = serviceAmpMobileService.recommendAppList(ampMobileServiceRequest);
            List<Map<String, Object>> clientAppData = NewMobileClientDataAdapter.toClientAppData(response.getData());
            return responseData(clientAppData);
        } catch (Exception e) {
            logger.error("获取推荐应用列表失败，原因：", e);
            return responseError(e.getMessage());
        }
    }

    /**
     * 获取应用类别category
     * {
     * code：200,
     * message: "",
     * data: [
     * {
     * categoryId: 1,
     * name: "类别名称"
     * },
     * {
     * categoryId: 2,
     * name: "类别名称"
     * }
     * ]
     * }
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCategoryList", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getCategoryList(HttpServletRequest request) {
        try {
            logger.debug("准备调用MobileAppInfoController.getCategoryList方法");
            DubboAmpMobileCategoryResponse response = serviceAmpMobileService.getCategoryList();
            List<Map<String, Object>> clientCategoryData = NewMobileClientDataAdapter.toClientCategoryData(response.getData());
            return responseData(clientCategoryData);
        } catch (Exception e) {
            logger.error("获取应用类别列表出错，原因：", e);
            return responseError(e.getMessage());
        }
    }


    public Map<String, Object> toClientAppListGroupByCategory(List<DubboAmpMobileServiceInfo> serviceInfoList, String categoryId) {
        List<Map<String, Object>> categoryList = new ArrayList<>();
        DubboAmpMobileCategoryResponse categoryResponse = serviceAmpMobileService.getCategoryAndServiceList(categoryId);
        List<DubboAmpMobileCategoryInfo> categoryInfoList = categoryResponse.getData();
        String showUnuse = ConfigUtil.getInnerProperty("mobile.interface.return.cannotuseApp");
        boolean flag = Boolean.parseBoolean(showUnuse);
        //所有服务wid，计算总数用
        List<String> setAppIds = new ArrayList<>();
        for (DubboAmpMobileCategoryInfo categoryInfo : categoryInfoList) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryId", categoryInfo.getWid());
            map.put("categoryName", categoryInfo.getName());

            List<Map<String, Object>> list = new ArrayList<>();

            for (DubboAmpMobileServiceInfo serviceInfo : serviceInfoList) {
                if (categoryInfo.getRecursionServiceWids().contains(serviceInfo.getWid())
                        && (flag || serviceInfo.getHasPermission())) {
                    setAppIds.add(serviceInfo.getWid());
                    Map<String, Object> objectMap = NewMobileClientDataAdapter.toClientAppData(serviceInfo);
                    if (objectMap != null) {
                        list.add(objectMap);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(list)) {
                map.put("apps", list);
                categoryList.add(map);
            }
        }
        //处理未分类的服务
        categoryList(categoryList, flag,serviceInfoList,categoryInfoList);
        return dataTableDataGroupByCategory(categoryList, setAppIds.stream().distinct().collect(Collectors.toList()).size());
    }

    private List<Map<String, Object>> categoryList(List<Map<String, Object>> categoryList,boolean flag,
                                                   List<DubboAmpMobileServiceInfo> serviceInfoList,List<DubboAmpMobileCategoryInfo> categoryInfoList){
        //处理未分类的服务
        List<Map<String, Object>> unCategoryList = new ArrayList<>();
        for (DubboAmpMobileServiceInfo serviceInfo : serviceInfoList) {
            boolean find = false;
            for (DubboAmpMobileCategoryInfo categoryInfo : categoryInfoList) {
                if (categoryInfo.getRecursionServiceWids().contains(serviceInfo.getWid())) {
                    find = true;
                    break;
                }
            }
            if(!find && (flag || serviceInfo.getHasPermission())){
                Map<String, Object> objectMap = NewMobileClientDataAdapter.toClientAppData(serviceInfo);
                if (objectMap != null) {
                    unCategoryList.add(objectMap);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(unCategoryList)) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryId", UN_CATEGORY_ID);
            map.put("categoryName", UN_CATEGORY_NAME);
            map.put("apps", unCategoryList);
            categoryList.add(map);
        }
      return categoryList;
    }

    private Map<String, Object> dataTableDataGroupByCategory(Collection<Map<String, Object>> records, int total) {
        return responseData("0", "", records, Collections.singletonMap("total", (Object) total));
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

    private Map<String, Object> responseError(int code) {
        String msg = mobileCodeUtil.getMsgByCode(code);
        return responseData(String.valueOf(code), msg, null, null);
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


    /**
     * 获取移动端配置的推荐服务
     */
    private List<String> getRecommendServiceList() {

        QueryWrapper<ShowProgrammeEntity> programmeWrapper = new QueryWrapper<>();
        programmeWrapper.lambda().eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        programmeWrapper.lambda().eq(ShowProgrammeEntity::getPageStatus, Global.Status.ENABLE.getId());
        programmeWrapper.lambda().eq(ShowProgrammeEntity::getPlatformType, Global.PlatformType.MOBILE.getType());
        List<ShowProgrammeEntity> programmeEntityList = showProgrammeMapper.selectList(programmeWrapper);
        if (CollectionUtils.isEmpty(programmeEntityList)) {
            return new ArrayList<>();
        }

        QueryWrapper<PageInfoEntity> pageInfoWrapper = new QueryWrapper<>();
        pageInfoWrapper.lambda().eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        pageInfoWrapper.lambda().eq(PageInfoEntity::getProgrammeId, programmeEntityList.get(0).getWid());
        List<PageInfoEntity> pageInfoList = pageInfoMapper.selectList(pageInfoWrapper);
        if (CollectionUtils.isEmpty(pageInfoList)) {
            return new ArrayList<>();
        }

        QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PageCardConfigEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        wrapper.lambda().eq(PageCardConfigEntity::getEnabledFlag, 1);
        wrapper.lambda().eq(PageCardConfigEntity::getCardId, "SYS_CARD_RECOMMENDAPP");
        wrapper.lambda().eq(PageCardConfigEntity::getPlatformType, Global.PlatformType.MOBILE.getType());
        wrapper.lambda().in(PageCardConfigEntity::getPageWid, pageInfoList.stream().map(PageInfoEntity::getWid).collect(Collectors.toList()));
        List<PageCardConfigEntity> list = pageCardConfigMapper.selectList(wrapper);

        List<String> serviceWids = new ArrayList<>();
        for (PageCardConfigEntity pageCardConfigEntity : list) {
            if (StringUtils.isNotEmpty(pageCardConfigEntity.getCardConfig())) {
                String configInfo = JSON.parse(pageCardConfigEntity.getCardConfig()).toString();
                Config config = JSONObject.parseObject(configInfo, Config.class);
                if (CollectionUtils.isNotEmpty(config.getAddServiceList())) {
                    serviceWids.addAll(config.getAddServiceList());
                }
            }
        }
        return serviceWids.stream().distinct().collect(Collectors.toList());
    }

}
