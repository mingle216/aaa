package com.wisedu.minos.casp.portal.service.impl;


import com.wisedu.minos.api.config.SystemConfigService;
import com.wisedu.minos.api.constant.SensitiveWordStrategyEnum;
import com.wisedu.minos.api.data.SensitiveWordDubboService;
import com.wisedu.minos.api.model.DubboConfigResponse;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.util.MinosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SensitiveWordService {

    @DubboReference
    private SensitiveWordDubboService sensitiveWordDubboService;

    @DubboReference
    private SystemConfigService systemConfigService;

    public String getConfig(String modelWid, String configKey){
        String config = "";
        DubboConfigResponse systemConfig = systemConfigService.getSystemConfig(modelWid, configKey);
        if(systemConfig != null && !systemConfig.getData().isEmpty()){
            config = systemConfig.getData().get(0).getConfigValue();
        }

        // 如果配置的不是规定范围内的，就取默认值
        if(!SensitiveWordStrategyEnum.ZERO.getCode().equals(config) && !SensitiveWordStrategyEnum.ONE.getCode().equals(config) && !SensitiveWordStrategyEnum.TWO.getCode().equals(config)){
            // 服务评价默认禁发
            config = SensitiveWordStrategyEnum.TWO.getCode();
        }

        LOGGER.debug("服务评价敏感词策略为：{}", config);
        return config;
    }

    /**
     * 批量替换敏感词
     * @param replaceList
     */
    public void batchesReplaceSensitiveWords(List<AppAppraiseEntity> replaceList){
        if(CollectionUtils.isEmpty(replaceList)){
            return;
        }

        List<String> contentList = replaceList.stream().map(AppAppraiseEntity::getContent).collect(Collectors.toList());
        List<String> replaceContents = sensitiveWordDubboService.batchesReplaceSensitiveWords(contentList);

        if(replaceList.size() != replaceContents.size()){
            LOGGER.warn("替换后的敏感词数量与替换前的敏感词数量不一致");
            return;
        }

        for(int i=0; i < replaceList.size(); i++){
            replaceList.get(i).setContent(replaceContents.get(i));
        }

    }

    /**
     * 判断是否包含敏感词
     */
    public boolean isContainsSensitiveWord(String string) {
        if(org.apache.commons.lang3.StringUtils.isBlank(string)){
            return false;
        }

        Set<String> containsSensitiveWord = sensitiveWordDubboService.isContainsSensitiveWord(string);

        return !CollectionUtils.isEmpty(containsSensitiveWord);
    }

}
