package com.wisedu.minos.timedtask;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.api.config.LanguageConfigService;
import com.wisedu.minos.api.constant.MinosCommonConstant;
import com.wisedu.minos.api.model.DubboLanguageConfigResp;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title GetSupportLanguageJob
 * @Author: 01120034
 * @Date: 2022/11/1
 */
@Component
public class GetSupportLanguageJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSupportLanguageJob.class);

    @DubboReference
    private LanguageConfigService languageConfigService;


    /***
     * @Author jcx
     * @Description 一分钟执行一次  查询当前门户支持的多语言信息
     * @Date 17:12 2022/10/28
     * @return void
     **/
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute(){
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
