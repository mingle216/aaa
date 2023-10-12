package com.wisedu.amp.card.sys.personaldata.service;


import com.wisedu.amp.card.sys.personaldata.util.SourceTypeUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.utils.MailUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserMailACLInformationService {
    private static final Logger LOGGER = LogManager.getLogger(UserMailACLInformationService.class);

    private UserMailACLInformationService () {
    }

    public static void getInformation (Map<String, PersonalDataEntity> dataInfoMap, String extraInfo) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        try {
            MailUtil mailUtil = ApplicationContextUtil.get(MailUtil.class);
            // 获取未读邮件数量
            int result = mailUtil.getUnreadCount(extraInfo);
            // 拼装信息
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 未读邮件数量：【unreadCount】
                item.setSubInfo(buildInfo(item.getSubInfo(), extraInfo));
                // 主要信息拼装
                item.setMainInfo(buildInfo(item.getMainInfo(), String.valueOf(result)));
            });

        } catch ( Exception e ) {
            LOGGER.warn("获取未读邮件数量异常", e);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfo(item.getSubInfo(), extraInfo));
                // 次要信息拼装
                item.setMainInfo(SourceTypeUtil.buildErrorInfo(item.getMainInfo()));
            });
        }
    }

    private static String buildInfo (String s, String result) {
        // 信息不为空
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SourceTypeUtil.SEPARATOR_LEFT, SourceTypeUtil.SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    s = s.replaceFirst(
                            SourceTypeUtil.getSeparatorWithContent(key),
                            SourceTypeUtil.getSpanWithContent(result));
                }
            }
        }
        return s;
    }
}
