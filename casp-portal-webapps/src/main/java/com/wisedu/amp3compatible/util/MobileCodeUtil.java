package com.wisedu.amp3compatible.util;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp3compatible.controller.NewMobileAppInfoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Set;

@Component
public class MobileCodeUtil implements InitializingBean {

    private static final String LOCATION = "/opt/settings/mobile_error_code.properties";

    private Properties errorCodeProperty = new Properties();

    private static final Logger logger = LoggerFactory.getLogger(NewMobileAppInfoController.class);

    /**
     * 初始化时从配置文件读入错误码与描述的对应关系
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        errorCodeProperty = ConfigUtil.loadProperties(LOCATION);
        if(!errorCodeProperty.isEmpty()){
            Set<String> names = errorCodeProperty.stringPropertyNames();
            logger.info("移动模块初始化，从配置文件 {} 中读取数据 ", LOCATION);
            for(String name:names){
                logger.info("输出配置文件内容。key:{},value:{}",name,errorCodeProperty.getProperty(name));
            }
        }
    }

    /**
     * 根据错误码返回详细错误JSON字符串
     * @param code
     * @return
     */
    public String getErrorOutputStr(Integer code){
        String msg = "";
        if(errorCodeProperty.containsKey(String.valueOf(code))){
            msg = errorCodeProperty.getProperty(String.valueOf(code));
        }else{
            code = 999;
            msg = "未知错误";
        }
        JSONObject out = new JSONObject();
        out.put("code",code);
        out.put("msg",msg);
        return out.toString();
    }

    /**
     * 根据错误码返回错误描述
     * @param code
     * @return
     */
    public String getMsgByCode(Integer code){
        String msg = "";
        if(errorCodeProperty.containsKey(String.valueOf(code))){
            msg = errorCodeProperty.getProperty(String.valueOf(code));
        }else{
            msg = "未知错误";
        }
        return msg;
    }

}