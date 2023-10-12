package com.wisedu.amp.card.sys.personaldata.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisedu.amp.card.sys.personaldata.util.SourceTypeUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.spi.itf.IAuth;
import com.wisedu.minos.casp.portal.utils.AuthUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCustomACLInformationService {

    private UserCustomACLInformationService () {

    }

    public static void getInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            // 判断是什么插件
            IAuth authPlugin = ApplicationContextUtil.get(AuthUtil.class).getAuthPluginById(information.getSourceKey());
            Map<String, Object> paramsMap = new HashMap<>(16);
            String authConfig = information.getAuthConfig();
            if ( StringUtil.isNotEmpty(authConfig) ) {
                if ( authConfig.contains(SourceTypeUtil.USER_ACCOUNT) ) {
                    authConfig = authConfig.replace(SourceTypeUtil.USER_ACCOUNT, user.getUserAccount());
                }
                paramsMap = JSONObject.parseObject(authConfig, new TypeReference<Map<String, Object>>(){});
            }
            String authData = authPlugin.getAuthData(paramsMap);
//            String authData = "{\"res\":{\"data\":[{\"account_balance\":31.5800000000000000,\"accumulate_times_of_use_card\":11822,\"card_type\":\"正式卡\",\"card_report_loss_status\":\"正常卡\",\"card_id\":\"1990063854\",\"open_account_date\":\"2007-09-25 00:00:00\",\"card_balance\":31.5800000000000000,\"card_status\":\"正常\",\"card_freeze_reason\":\"对帐不平差额超过警戒额\",\"update_time\":\"2021-05-21 14:14:53\",\"full_name\":\"金智巡检卡                    \",\"current_transit_balance\":0.09000000000000000000,\"effective_date\":\"2019-04-01 00:00:00\",\"seu_card_id\":\"109000473\",\"card_freeze_status\":\"正常\"}]}}\n";

            Assert.hasLength(authData, "获取到的数据信息为空");
            JSONObject resultJson = JSONObject.parseObject(authData);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
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
            throw new MinosException("自定义插件获取数据出错", e);
        }
    }
}
