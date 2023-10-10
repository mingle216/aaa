package com.wisedu.minos.casp.portal.model.configcomponent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.JacksonUtil;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：组件设计器
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ComponentContainer
 * @Author: jcx
 * @Date: 2021/1/18
 */
@Getter
@Setter
public class ComponentContainer {

    public ComponentContainer () {

    }

    private static final Logger logger = LogManager.getLogger(ComponentContainer.class);
    /**
     * LanguageConfigService
     * 配置组件集合
     */
    private List<AbstractComponent> components = new ArrayList<>();

    /***
     * @Author jcx
     * @Description 拿到默认配置 ，将其中list中的每个对象转换成具体实例
     * @Date 9:56 2021/1/19
     * @Param configJson:
     **/
    public ComponentContainer (String configJson, String valueConfig) {
        boolean isSuccess = false;
        try {
            Map<String, AbstractComponent> beansOfType = ApplicationContextUtil.getBeansOfType(AbstractComponent.class);
            logger.debug("---ComponentContainer实例化开始-----，参数为--{}", configJson);
            if(StringUtil.isNotEmpty(configJson)) {
                JSONArray configJsonArray = JSONArray.parseArray(configJson);
                for (Object object : configJsonArray) {
                    JSONObject configJsonObj = (JSONObject) object;
                    beansOfType.forEach((key, value) -> {
                        if (configJsonObj.getString(Global.ComponentParam.COMPONENT.getParam()).equals(value.getComponent())) {
                            //前端需要组件中有labelAlias  、valueAlias 的默认值 为 label value
                            handleConfiJsonObj(configJsonObj, value);
                            //转换
                            AbstractComponent abstractComponent = JacksonUtil.toObject(configJsonObj.toJSONString(), value.getClass());
                            processComponent(valueConfig, abstractComponent);
                        }
                    });
                }
            }
        } catch ( Exception e ) {
            isSuccess = true;
            logger.error("---ComponentContainer设计器实例化失败，失败原因为--", e);
        }
        if ( isSuccess ) {
            throw new BusinessException("ComponentContainer设计器实例化失败，请检查configMetaData.json配置是否有误");
        }
    }

    private void handleConfiJsonObj(JSONObject configJsonObj, AbstractComponent value) {
        Method getLabelMethod = ReflectionUtils.findMethod(value.getClass(), "getLabelAlias");
        Method getValueAliasMethod = ReflectionUtils.findMethod(value.getClass(), "getValueAlias");
        if (null != getLabelMethod) {
            configJsonObj.putIfAbsent("labelAlias", "");
        }
        if (null != getValueAliasMethod) {
            configJsonObj.putIfAbsent("valueAlias", "");
        }
    }

    private void processComponent(String valueConfig, AbstractComponent abstractComponent) {
        if ( null != abstractComponent ) {
            if ( StringUtil.isNotEmpty(valueConfig) ) {
                //当前卡片有设置当前值
                //格式化
                String str = StringEscapeUtils.unescapeJava(valueConfig);
                //转化JSONObject
                JSONObject valueConfigJsonObj = JSONObject.parseObject(str.substring(1, str.length() - 1));
//                boolean valueObj = valueConfigJsonObj.containsKey(abstractComponent.getKey());
//                if (!valueObj) {
//                    throw new BusinessException("数据库config无此key值，格式错误，请修改！");
//                }
                abstractComponent.setValueResult(valueConfigJsonObj.get(abstractComponent.getKey()));
            }
            components.add(abstractComponent);
        } else {
            throw new BusinessException("ComponentContainer设计器实例化失败，请检查configMetaData.json配置是否有误");
        }
    }

    /***
     * @Author jcx
     * @Description 设置 datas defaultValue参数的值
     * @Date 9:53 2021/1/19
     * @Param componentKey:  基础组件的唯一key
     * @Param componentParam:  具体要设置的参数 包括（datas defaultValue）
     * @Param data: 要设置的值
     * @return void
     **/
    public ComponentContainer setData (String componentKey, Global.ComponentParam componentParam, Object data) {
        boolean isSuccess = false;
        boolean isFindKey = false;
        int componentKeyNum = 0;
        try {
            if ( CollectionUtils.isNotEmpty(components) ) {
                logger.debug("-----------ComponentContainer设置值前components-------{}", JSON.toJSONString(components));
                for ( AbstractComponent component : components ) {
                    //得到唯一值
                    Object obj = ObjectUtil.getObjectFromClass(component, "getKey");
                    if ( componentKey.equals(obj) ) {
                        componentKeyNum++;
                        isFindKey = true;
                        switch ( componentParam ) {
                            case DATAS:
                                component.setDataResult(data);
                                break;
                            case VALUE:
                                component.setValueResult(data);
                                break;
                            case DEFAULT_VALUE:
                                component.setDefaultValueResult(data);
                                break;
                        }
                    }
                }
                logger.debug("-----------ComponentContainer设置值后components-------{}", JSON.toJSONString(components));
            }
        } catch ( Exception e ) {
            isSuccess = true;
            logger.error("ComponentContainer设置值失败，data key为--{},失败原因：{}", componentKey, e);
        }
        if ( isSuccess ) {
            throw new BusinessException("ComponentContainer设置值失败");
        }
        if ( componentKeyNum > 1 ) {
            throw new BusinessException("componentKey重复,请重新配置json配置文件");
        }
        if ( ! isFindKey ) {
            throw new BusinessException("传入的componentKey在对应json配置文件中未能匹配,请重新配置对应json配置文件或者修改componentKey参数");
        }
        return this;
    }

    public String toJsonComponent () {
        return JSON.toJSONString(components);
    }
}
