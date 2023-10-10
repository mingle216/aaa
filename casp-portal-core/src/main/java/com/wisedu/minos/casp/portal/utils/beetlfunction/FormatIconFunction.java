package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

/**
 * FormatIconFunction
 * <p/>
 * 图标格式化
 *
 * @author hyluan
 * @date 2020/9/29 14:59
 * Copyright (c) 2018 wisedu
 */
@Component
public class FormatIconFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(FormatIconFunction.class);

    @Override
    public Object call(Object[] obj, Context context) {
        ServerProperties serverProperties = ApplicationContextUtil.get(ServerProperties.class);
        String iconElement = "";
        String iconUrl = null;
        Integer iconType = null;
        try {
            iconUrl = (String) obj[0];
            if (iconUrl != null) {
                iconType = (Integer) obj[1];
                if (iconType == Global.IconType.FONT.getId()) {
                    iconElement = String.format("<i class='iconfont %s'></i>", iconUrl);
//                    iconElement = String.format("<svg class=\"icon\" aria-hidden=\"true\">\n" +
//                            "                        <use xlink:href=\"#%s\"></use>\n" +
//                            "                    </svg>", iconUrl);
                } else if (iconType == Global.IconType.PNG.getId()) {
                    iconElement = String.format("<img class='png-icon' src='%s' onerror=\"this.src='%s/images/imgErr.png'\"/>", iconUrl, serverProperties.getServlet().getContextPath());
                }
            }
        } catch (Exception e) {
            logger.error("格式化图标失败：{},{}", iconUrl, iconType, e);
        }
        return iconElement;
    }

    @Override
    public String functionName() {
        return "formatIcon";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        return this;
    }
}
