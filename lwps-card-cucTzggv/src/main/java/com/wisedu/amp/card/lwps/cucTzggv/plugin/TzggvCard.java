package com.wisedu.amp.card.lwps.cucTzggv.plugin;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TzggvCard
 * @Author: 01315003
 * @Date: 2022/7/5
 */

@MinosSPI
public class TzggvCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(TzggvCard.class);

    private static final String BRACKETS = "[]";

    @Override
    public String getCardId () {
        return "LWPS_CARD_CUCTZGGV";
    }

    @Override
    public void destroy () {

    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getConfig":
                result = this.getConfig(request);
                break;
            default:
        }
        return result;
    }

    /**
     * 获取卡片配置
     *
     * @return
     */
    private Map<String, Object> getConfig(CardAjaxRequest request) {
        Map<String, Object> configMap = new HashMap<>();

        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if ( StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = toMap(config);
        } else {
            configMap.put("newsColumns", 1);
            configMap.put("lmDisplayType", "1");
            configMap.put("newsPageSize", 10);
            configMap.put("contextPath", "http://172.16.72.225:8081/emap/");
        }
        return configMap;
    }

    /**
     * String转Map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !BRACKETS.equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }
}
