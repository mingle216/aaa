package com.wisedu.casp.portal.template.sys.cqwork.service;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.casp.portal.template.sys.cqwork.util.SourceTypeUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserXmlACLInformationService {
    private static final Logger LOGGER = LogManager.getLogger(UserXmlACLInformationService.class);

    private UserXmlACLInformationService () {

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
                resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
            } else {// get请求
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            }
            Assert.hasLength(resultStr, "获取到的数据信息为空");
            Document document = DocumentHelper.parseText(resultStr);
            Element element = document.getRootElement();

            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfo(item.getSubInfo(), element));
                // 次要信息拼装
                item.setMainInfo(buildInfo(item.getMainInfo(), element));
            });
        } catch ( Exception e ) {
            LOGGER.warn("XML方式获取数据出错", e);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(SourceTypeUtil.buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(SourceTypeUtil.buildErrorInfo(item.getMainInfo()));
            });
        }
    }

    // city~0/content~2/ss@name
    private static String getInfo (String key, Element element) {
        String[] chKeys = StringUtil.split(key, SourceTypeUtil.SEPARATOR_LINE);
        for ( String chKey : chKeys ) {
            if ( chKey.contains(SourceTypeUtil.SEPARATOR_ATTR) ) {
                // 获取属性中的值
                String[] attrKeys = StringUtil.split(chKey, SourceTypeUtil.SEPARATOR_ATTR);
                String[] childKeys = StringUtil.split(attrKeys[ 0 ], SourceTypeUtil.SEPARATOR);

                List list = element.selectNodes(childKeys[ 0 ]);
                int index = childKeys.length > 1 && StringUtil.isNumeric(childKeys[ 1 ]) ? Integer.parseInt(childKeys[ 1 ]) : 0;
                if ( ! CollectionUtils.isEmpty(list) ) {
                    element = ( Element ) list.get(index);
                    return element.attribute(attrKeys[ 1 ]).getText();
                }
                return "-";
            }

            String[] childKeys = StringUtil.split(chKey, SourceTypeUtil.SEPARATOR);
            List list = element.selectNodes(childKeys[ 0 ]);
            int index = childKeys.length > 1 && StringUtil.isNumeric(childKeys[ 1 ]) ? Integer.parseInt(childKeys[ 1 ]) : 0;
            if ( ! CollectionUtils.isEmpty(list) ) {
                element = ( Element ) list.get(index);
            }
        }
        // 获取文本内容
        return element.getText();
    }


    private static String buildInfo (String s, Element element) {
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SourceTypeUtil.SEPARATOR_LEFT, SourceTypeUtil.SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = getInfo(key, element);
                    s = s.replaceFirst(
                            SourceTypeUtil.getSeparatorWithContent(key),
                            SourceTypeUtil.getSpanWithContent(value));
                }
            }
        }
        return s;
    }
}
