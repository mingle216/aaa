package com.wisedu.amp3compatible.util;

import com.wisedu.minos.api.model.DubboAmpMobileCategoryInfo;
import com.wisedu.minos.api.model.DubboAmpMobileServiceInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;

public class NewMobileClientDataAdapter {


    public static List<Map<String, Object>> toClientAppData(List<DubboAmpMobileServiceInfo> appList) {
        if (CollectionUtils.isEmpty(appList)) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (DubboAmpMobileServiceInfo appInfo: appList) {
            Map<String, Object> clientAppDataMap = toClientAppData(appInfo);
            if (MapUtils.isNotEmpty(clientAppDataMap)) {
                resultList.add(clientAppDataMap);
            }
        }
        return resultList;
    }

    public static Map<String, Object> toClientAppData(DubboAmpMobileServiceInfo appInfo) {
        Map<String, Object> clientAppDataMap = new HashMap<String, Object>();
        clientAppDataMap.put("appId", appInfo.getServiceId());
        clientAppDataMap.put("iconUrl", appInfo.getMobileIconUrl());
        clientAppDataMap.put("name", appInfo.getServiceName());
        clientAppDataMap.put("desc", appInfo.getServiceDesc());
        clientAppDataMap.put("appType", appInfo.getAppType());
        clientAppDataMap.put("openUrl", appInfo.getMobileUrl());

        //这些字段暂时没有
        clientAppDataMap.put("version", appInfo.getServiceVersion());
//        if(appInfo.getAppMobile() != null){
            clientAppDataMap.put("serverDomain",appInfo.getServiceDomain());
            clientAppDataMap.put("hasBackend","");
            clientAppDataMap.put("installUrl", "");
            clientAppDataMap.put("appPic", "");
//        }

        clientAppDataMap.put("assessMark", "");
        clientAppDataMap.put("assessCount","");
        clientAppDataMap.put("useCount", appInfo.getUseCount());
        clientAppDataMap.put("hasPermission", appInfo.getHasPermission());
        clientAppDataMap.put("accessAuth",appInfo.getAccessAuth());
        clientAppDataMap.put("categoryNames", appInfo.getCategoryNames());
        return clientAppDataMap;
    }


    public static List<Map<String, Object>> toClientCategoryData(List<DubboAmpMobileCategoryInfo> appCategoryList) {
        if (CollectionUtils.isEmpty(appCategoryList)) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (DubboAmpMobileCategoryInfo appCategory : appCategoryList) {
            Map<String, Object> clientMapData = new HashMap<>();
            clientMapData.put("categoryId", appCategory.getWid());
            clientMapData.put("name", appCategory.getName());
            resultList.add(clientMapData);
        }
        return resultList;
    }


}
