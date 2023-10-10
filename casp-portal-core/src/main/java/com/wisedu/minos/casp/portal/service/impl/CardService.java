package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.CardEntity;
import com.wisedu.minos.casp.portal.dao.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService extends MyServiceImpl<CardMapper, CardEntity> {

    /**
     * 更新状态
     * @param cardId
     */
    public void updateCardStatus(String cardId) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardStatus(Global.CARD_STATUS_NORMAL);
        baseMapper.update(cardEntity,
                new UpdateWrapper<CardEntity>().lambda()
                        .eq(CardEntity::getCardId, cardId)
                        .eq(CardEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                        .eq(CardEntity::getCardStatus, Global.CARD_STATUS_TO_UPDATE));
    }
}
