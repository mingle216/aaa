package com.wisedu.amp.card.cus.personalinfo.service;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cus.personalinfo.util.SourceTypeUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.util.MinosException;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserJsonACLInformationService {

    private UserJsonACLInformationService () {

    }

    public static void getInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            // 接口地址
            Assert.hasLength(information.getSourceKey(), "接口地址为空");
            StringBuilder url = new StringBuilder(information.getSourceKey());
            JSONObject entityJson = new JSONObject();
            if ( StringUtil.isNotBlank(information.getUserKey()) ) {
                if ( url.toString().contains(SourceTypeUtil.URL_PARAM_HOLDER) ) {
                    url.append(SourceTypeUtil.URL_PARAM_AND);
                } else {
                    url.append(SourceTypeUtil.URL_PARAM_HOLDER);
                }
                url.append(information.getUserKey()).append(SourceTypeUtil.SEPARATOR_EQUAL).append(user.getUserAccount());
                entityJson.put(information.getUserKey(), user.getUserAccount());
            }

            String resultStr;
            if ( SourceTypeUtil.POST_REQUEST.equals(information.getHttpMethod()) ) {
                // post请求
                resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
            } else {
                // get请求
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            }

            Assert.hasLength(resultStr, "获取到的数据信息为空");
            JSONObject resultJson = JSONObject.parseObject(resultStr);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(SourceTypeUtil.buildInfoWithJson(item.getSubInfo(), resultJson));
                // 次要信息拼装
                item.setMainInfo(SourceTypeUtil.buildInfoWithJson(item.getMainInfo(), resultJson));
            });
        } catch ( Exception e ) {
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(SourceTypeUtil.buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(SourceTypeUtil.buildErrorInfo(item.getMainInfo()));
            });
            throw new MinosException("调用JSON接口出错", e);
        }
    }
}
