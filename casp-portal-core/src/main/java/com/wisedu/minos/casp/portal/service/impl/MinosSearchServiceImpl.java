package com.wisedu.minos.casp.portal.service.impl;


import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.model.HotSearchDataRequest;
import com.wisedu.minos.casp.portal.model.MinosMoreTdcForPortalRequest;
import com.wisedu.minos.casp.portal.model.MinosSearchAnalyzeRequest;
import com.wisedu.minos.casp.portal.model.MinosSearchForPortalRequest;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * minos 搜索服务
 */
@Service
public class MinosSearchServiceImpl {

    /**
     * 搜索建议
     * @param request
     * @return
     */
    public Map<String, Object> suggestForPortal(MinosSearchForPortalRequest request){

        HttpEntity<MinosSearchForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/suggestForPortal", httpEntity, Map.class).getBody();
    }

    /**
     * 自建分组搜索建议
     * @param request
     * @return
     */
    public Map<String, Object> suggestCustomForPortal(MinosSearchForPortalRequest request){

        HttpEntity<MinosSearchForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/suggestCustomForPortal", httpEntity, Map.class).getBody();
    }

    /**
     * 门户搜索
     * @param request
     * @return
     */
    public Map<String, Object> searchForPortal(MinosSearchForPortalRequest request){

        HttpEntity<MinosSearchForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/searchForPortal", httpEntity, Map.class).getBody();
    }

    /**
     * 门户搜索
     * @param request
     * @return
     */
    public Map<String, Object> getHotSearchData(HotSearchDataRequest request){

        HttpEntity<HotSearchDataRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/getHotSearchDatas", httpEntity, Map.class).getBody();
    }

    /**
     * 搜索更多自定义分组数据
     * @param request
     * @return
     */
    public Map<String, Object> moreCustomGroupDataForPortal(MinosMoreCustomGroupDataForPortalRequest request){

        HttpEntity<MinosMoreCustomGroupDataForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/moreCustomGroupDataForPortal", httpEntity, Map.class).getBody();
    }
    /**
     * 搜索更多新闻接口
     * @param request
     * @return
     */
    public Map<String, Object> moreNewsForPortal(MinosMoreNewsForPortalRequest request){

        HttpEntity<MinosMoreNewsForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/moreNewsForPortal", httpEntity, Map.class).getBody();
    }

    /**
     * 切换匹配度/时间排序方式搜索新闻接口
     * @param request
     * @return
     */
    public Map<String, Object> sortNewsForPortal(MinosSearchForPortalRequest request){

        HttpEntity<MinosSearchForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/sortNewsForPortal", httpEntity, Map.class).getBody();
    }

    public Map<String, Object> searchAnalyze(MinosSearchAnalyzeRequest request) {

        HttpEntity<MinosSearchAnalyzeRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/analyze", httpEntity, Map.class).getBody();
    }

    public Map<String, Object> moreTdcForPortal(MinosMoreTdcForPortalRequest request) {

        HttpEntity<MinosMoreTdcForPortalRequest> httpEntity = new HttpEntity<>(request);

        return RestTemplateUtils.post("/minos-search/searchData/moreTdcForPortal", httpEntity, Map.class).getBody();
    }
}
