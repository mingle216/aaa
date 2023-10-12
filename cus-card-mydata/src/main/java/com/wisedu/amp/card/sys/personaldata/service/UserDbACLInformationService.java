package com.wisedu.amp.card.sys.personaldata.service;

import com.wisedu.amp.card.sys.personaldata.util.SourceTypeUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.DataSourceUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import com.wisedu.minos.util.jdbc.JDBCTools;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.*;

public class UserDbACLInformationService {

    private UserDbACLInformationService () {

    }

    public static void getInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            Assert.hasLength(information.getSourceKey(), "数据源为空");
            JDBCConnectParamBean jdbcConnectParamBean = ApplicationContextUtil.get(DataSourceUtil.class).getJDBCConnectParamBeanByName(information.getSourceKey());
            List<Map<String, String>> matchInformationList;
            //根据sql是否含有(#{userId})决定是否加上当前登录用户ID
            if ( information.getDataSql().contains(SourceTypeUtil.URL_PARAM_HOLDER) ) {
                String[][] params = {{"1", user.getUserAccount()}};
                matchInformationList = JDBCTools.query(jdbcConnectParamBean, information.getDataSql(), params);
            } else {
                matchInformationList = JDBCTools.query(jdbcConnectParamBean, information.getDataSql(),null);
            }
            Map<String, String> matchInformationMap = CollectionUtils.isNotEmpty(matchInformationList) ? matchInformationList.get(0) : Collections.emptyMap();

            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfo(item.getSubInfo(), matchInformationMap));
                // 次要信息拼装
                item.setMainInfo(buildInfo(item.getMainInfo(), matchInformationMap));
            });

        } catch ( Exception e ) {
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(SourceTypeUtil.buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(SourceTypeUtil.buildErrorInfo(item.getMainInfo()));
            });
            throw new MinosException("调用DB接口出错", e);
        }
    }

    private static String buildInfo (String s, Map<String, String> matchInformationMap) {
        if ( StringUtil.isNotBlank(s) ) { // 次要信息不为空
            String[] keys = StringUtil.substringsBetween(s, SourceTypeUtil.SEPARATOR_LEFT, SourceTypeUtil.SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = StringUtil.isBlank(key) ?
                            SourceTypeUtil.NO_DATA : matchInformationMap.get(key.toLowerCase(Locale.US));

                    s = s.replaceAll(
                            SourceTypeUtil.getSeparatorWithContent(key),
                            SourceTypeUtil.getSpanWithContent(value));
                }
            }
        }
        return s;
    }
}
