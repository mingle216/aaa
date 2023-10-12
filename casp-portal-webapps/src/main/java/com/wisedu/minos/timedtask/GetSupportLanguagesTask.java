package com.wisedu.minos.timedtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.api.config.LanguageConfigService;
import com.wisedu.minos.api.constant.MinosCommonConstant;
import com.wisedu.minos.api.model.DubboLanguageConfigResp;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.util.scheduler.job.BaseJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 功能描述：获取门户支持的多语言定时任务
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title GetSupportLanguagesTask
 * @Author: 01120034
 * @Date: 2021/9/6
 */
@Component
public class GetSupportLanguagesTask implements BaseJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSupportLanguagesTask.class);

    @DubboReference
    private LanguageConfigService languageConfigService;

    @Override
    public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.debug("=======>>>开始执行获取门户支持的多语言定时任务");
        if(null!=languageConfigService){
            try {
                DubboLanguageConfigResp resp = languageConfigService.getAllLanguageConfigs(MinosCommonConstant.FUNCTION_PORTAL,"");
                LOGGER.debug("=======>>>获取的多语言结果，{}", JSON.toJSONString(resp));
                if("0".equals(resp.getErrcode())){
                    DubboI18nService.setResp(resp);
                }
            } catch ( Exception e ) {
                LOGGER.error("=======>>>执行获取门户支持的多语言定时任务发生异常:",e);
            }
        }

    }
}
