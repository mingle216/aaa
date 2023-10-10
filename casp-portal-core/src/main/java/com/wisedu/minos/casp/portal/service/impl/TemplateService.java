package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.dao.mapper.TemplateMapper;
import org.springframework.stereotype.Service;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TemplateService
 * @Author: u
 * @Date: 2020/9/21
 */
@Service
public class TemplateService extends ServiceImpl<TemplateMapper, TemplateEntity> {

    /**
     * 更新状态
     * @param templateId
     */
    public void updateTemplatesStatus(String templateId) {
        TemplateEntity templateEntity = new TemplateEntity();
        templateEntity.setTemplateStatus(Global.CARD_STATUS_NORMAL);
        baseMapper.update(templateEntity,
                new UpdateWrapper<TemplateEntity>().lambda()
                        .eq(TemplateEntity::getTemplateId, templateId)
                        .eq(TemplateEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                        .eq(TemplateEntity::getTemplateStatus, Global.CARD_STATUS_TO_UPDATE));
    }

}
