package com.wisedu.minos.condition;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * 功能描述：是否使用IDS的判断类(用作是否注册Bean条件判断)
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title IDSCondition
 * @Author: jcx
 * @Date: 2020/8/20
 */
@Slf4j
public class IDSCondition implements Condition {


    /**
     * @return boolean
     * @Author jcx
     * @Description 是否使用IDS的判断条件
     * @Date 16:38 2020/8/20
     * @Param conditionContext: 判读条件能使用的上下文
     * @Param annotatedTypeMetadata:注解信息
     **/
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //IDS开启统一认证开关
        String moduleDomain = conditionContext.getEnvironment().getProperty("module.domain");
        if( StringUtils.isBlank(moduleDomain) ){
            try {
                moduleDomain="http://"+ InetAddress.getLocalHost().getHostAddress()+":8116";
            } catch ( UnknownHostException e ) {
                LOGGER.error("设置系统域名module.domain错误：" + e);
                moduleDomain = "http://127.0.0.1:8116";
            }
        }
        String casServerUrl = conditionContext.getEnvironment().getProperty("cas.server-url");
        boolean enable = StringUtil.isNotEmpty(moduleDomain) && StringUtil.isNotEmpty(casServerUrl);
        return enable;
    }
}
