package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.mapper.InternationalizationMapper;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title InternationalizationImpl
 * @Author: jcx
 * @Date: 2020/9/23
 */
@Service
@Slf4j
public class InternationalizationService extends MyServiceImpl<InternationalizationMapper, InternationalizationEntity> {

    @Autowired
    I18nService i18nService;

    /**
     * getByLangKey
     * <p/>
     * 获取当前国际化信息
     *
     * @param langCountry
     * @param langKey
     * @return com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity
     * @throws
     * @date 2020-10-1 22:27
     */
    public InternationalizationEntity getByLangKey(String langCountry, String langKey) {
        return this.getOne(Wrappers.<InternationalizationEntity>lambdaQuery()
                .eq(InternationalizationEntity::getLangCountry, langCountry)
                .eq(InternationalizationEntity::getLangKey, langKey)
                .eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
        );
    }

    /**
     * getlangValue
     * <p/>
     * 根据国际化key获取显示值
     *
     * @param langKey
     * @return java.lang.String
     * @throws
     * @date 2020/10/24 10:37
     */
    public String getlangValue(String langKey) {
        if (StringUtil.isEmpty(langKey)) {
            return "";
        }
        Locale currentLanguage = i18nService.getCurrentLanguage();
        InternationalizationEntity entity = getByLangKey(currentLanguage.toString(), langKey);
        if (entity == null) {
            LOGGER.info("未匹配到的国际化key："+langKey);
            return "";
        }
        return entity.getLangValue();
    }
    public List<InternationalizationEntity> getByLangKeyList(String langCountry, Collection<String> langKey) {
        return this.list(Wrappers.<InternationalizationEntity>lambdaQuery()
                .eq(InternationalizationEntity::getLangCountry, langCountry)
                .in(InternationalizationEntity::getLangKey, langKey)
                .eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
        );
    }
}
