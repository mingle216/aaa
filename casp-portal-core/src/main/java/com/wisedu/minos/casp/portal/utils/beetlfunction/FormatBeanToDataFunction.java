package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.stereotype.Component;

/**
 * FormatObjectToDataFunction
 * <p/>
 * 把java对象转为 html data 属性 便于html存储数据<br>
 * 例如： bean:User 属性为 userId="zhangsan"; userName="张三"; <br>
 * 将转为字符串 data-user-id="zhangsan" data-user-name="张三"
 *
 * @author hyluan
 * @date 2020/11/6 11:01
 * Copyright (c) 2018 wisedu
 */
@Component
public class FormatBeanToDataFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(FormatBeanToDataFunction.class);

    @Override
    public Object call(Object[] objects, Context context) {
        String message = "";
        try {
            Object object = objects[0];
            if (object != null) {
                JSONObject o = (JSONObject) JSONObject.toJSON(object);
                StringBuilder sb = new StringBuilder();
                o.forEach((key, value) -> {
                    String xssStr;
                    if (value == null) {
                        xssStr = "";
                    } else {
                        xssStr = value.toString();
//                        xssStr = StringUtil.xssEncode(value.toString());
                    }
                    sb.append(String.format("data-%s=\"%s\" ", StringUtil.humpToMinus(key), xssStr));
                });
                message = sb.toString();
            }
        } catch (Exception e) {
            logger.error(functionName() + "失败", e);
        }
        return message;
    }

    @Override
    public String functionName() {
        return "formatBeanToData";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        return this;
    }

}
