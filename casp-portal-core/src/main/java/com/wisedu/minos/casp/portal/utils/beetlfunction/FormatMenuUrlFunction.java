package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

/**
 * FormatMenuUrlFunction
 * <p/>
 * 菜单url格式化
 *
 * @author hyluan
 * @date 2020/9/29 14:59
 * Copyright (c) 2018 wisedu
 */
@Component
public class FormatMenuUrlFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(FormatMenuUrlFunction.class);

    @Override
    public Object call(Object[] obj, Context context) {
        ServerProperties serverProperties = ApplicationContextUtil.get(ServerProperties.class);
        String linkUrl = "";
        String url = null;
        Integer menuType = null;
        try {
            url = (String) obj[0];
            if (url == null) {
                linkUrl = "javascript:void(0)";
            }else{
                menuType = (Integer) obj[1];
                if (menuType == Global.MenuType.INNER_LINK.getId()) {
                    if (url.startsWith("http")) {
                        linkUrl = url;
                    } else {
                        linkUrl = serverProperties.getServlet().getContextPath() + url;
                    }
                } else if (menuType == Global.MenuType.OUTER_LINK.getId()) {
                    linkUrl = url;
                } else {
                    linkUrl = "javascript:void(0)";
                }
            }

        } catch (Exception e) {
            logger.error("格式化菜单地址失败：{},{}", url, menuType, e);
        }
        return linkUrl;
    }

    @Override
    public String functionName() {
        return "formatMenuUrl";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        return this;
    }
}
