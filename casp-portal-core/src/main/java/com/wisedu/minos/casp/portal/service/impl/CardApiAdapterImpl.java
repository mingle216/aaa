package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.page.PageFactory;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.CardEntity;
import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.VersionManagementEntity;
import com.wisedu.minos.casp.portal.dao.mapper.CardMapper;
import com.wisedu.minos.casp.portal.dao.mapper.PageInfoMapper;
import com.wisedu.minos.casp.portal.dao.mapper.VersionManagementMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.CardApiAdapter;
import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述：卡片相关实现类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardApiAdapterImpl
 * @Author: jcx
 * @Date: 2020/9/17
 */
@Service
public class CardApiAdapterImpl implements CardApiAdapter {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private VersionManagementMapper versionManagementMapper;
    @Autowired
    private PageInfoMapper pageInfoMapper;
    @Autowired
    private PageInfoService pageInfoService;
    @Value("${file.cardUploadPath}")
    private String cardLocalUploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InlineResponse2006 deleteCard(List<String> body) {
        Assert.notNull(body, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
        Assert.isTrue(CollectionUtils.isNotEmpty(body), GlobalEnum.VALIDATION.getCode(), GlobalEnum.VALIDATION.getMsg());
        List<CardEntity> list = new ArrayList<>();
        List<VersionManagementEntity> versionManagements = new ArrayList<>();
        List<String> jarNames = new ArrayList<>();
        for (String wid : body) {
            CardEntity cardEntityFir = cardMapper.selectById(wid);
            VersionManagementEntity versionManagement = new VersionManagementEntity();
            versionManagement.setForeignKey(cardEntityFir.getCardId());
            versionManagement.setVersionNumber(cardEntityFir.getVersionNumber());
            versionManagements.add(versionManagement);
            //查询页面表信息，看看是否有关联信息
            QueryWrapper<PageInfoEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId())
                    .apply(MinosCommonUtil.getLikeEscapeSql(DbFieldConstant.CARD_LAYOUT), MinosCommonUtil.getLikeEscapeValue("\"" + cardEntityFir.getCardId() + "\""));
            List<PageInfoEntity> pageInfoEntities = pageInfoMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(pageInfoEntities)) {
                throw new BusinessException("展示方案关联到此卡片，如已关联则必须解除关联方可删除!");
            }
            CardEntity cardEntity = new CardEntity();
            cardEntity.setWid(wid);
            cardEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
            cardEntity.setCardStatus(Global.CARD_STATUS_NOT_AVAILABLE);
            list.add(cardEntity);

            String jarCardName = cardEntityFir.getCardId() + ".jar";
            jarNames.add(jarCardName);
        }
        cardService.updateBatchById(list);
        //删除卡片的同时需连版本管理表记录也停用
        if (CollectionUtils.isNotEmpty(versionManagements)) {
            for (VersionManagementEntity versionManagementEntity : versionManagements) {
                QueryWrapper<VersionManagementEntity> wrapper = new QueryWrapper<>();
                wrapper.eq(DbFieldConstant.FOREIGN_KEY, versionManagementEntity.getForeignKey())
                        .eq(DbFieldConstant.VERSION_NUMBER, versionManagementEntity.getVersionNumber())
                        .eq(DbFieldConstant.VERSION_TYPE, Integer.parseInt(Global.CARD_UPLOAD_TYPE))
                        .eq(DbFieldConstant.VERSION_STATUS, Global.EnableStatus.ENABLE.getId());
                VersionManagementEntity dbVersionManagement = versionManagementMapper.selectOne(wrapper);
                if (null != dbVersionManagement) {
                    dbVersionManagement.setVersionStatus(Global.EnableStatus.DISABLE.getId());
                    versionManagementMapper.updateById(dbVersionManagement);
                }
            }
        }
        //删除Jar包
        if (CollectionUtils.isNotEmpty(jarNames)) {
            FileService.deleteJar(cardLocalUploadPath, jarNames);
        }
        InlineResponse2006 inlineResponse2006 = new InlineResponse2006();
        inlineResponse2006.setErrmsg("删除成功");
        return inlineResponse2006;
    }

    @Override
    public InlineResponse2005 getCardInfo(String wid) {
        Assert.notNull(wid, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
        QueryWrapper<CardEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CardEntity::getWid, wid).eq(CardEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        CardEntity data = cardService.getOne(wrapper);
        if (ObjectUtils.isEmpty(data)) {
            return new InlineResponse2005();
        }
        return new InlineResponse2005().data((CardInfo) ObjectUtil.copyProperties(data, new CardInfo()));
    }

    @Override
    public InlineResponse2004 queryCardList(CommonQueryListReq body) {
        //开启分页
        Page<CardEntity> page = PageFactory.createPageBeginPage(body);
        QueryWrapper<CardEntity> queryWrapper = AppBeanUtil.searchObjectCondition(body.getSearchCriteria(), CardEntity.class);
        queryWrapper.orderByDesc(DbFieldConstant.UPDATE_TIME);
        IPage<CardEntity> cardEntityIPage = cardMapper.selectPage(page, queryWrapper);
        List<CardEntity> records = cardEntityIPage.getRecords();
        InlineResponse2004 inlineResponse2004 = ObjectUtil.copyProperties(new PageResult(cardEntityIPage), new InlineResponse2004());
        if (CollectionUtils.isNotEmpty(records)) {
            List<CardInfo> cardInfos = ObjectUtil.copyListProperties(records, CardInfo::new, (cardEntity, cardInfo) -> {
                // 这里可以定义特定的转换规则，用来转换时间
                cardInfo.setUpdateTime(DateUtil.getStrFromDate(cardEntity.getUpdateTime(), DateUtil.DATE_FORMATE_STRING_A));
            });
            // 管理端且分页，暂时忽略性能
            List<PageInfoEntity> pageInfos = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
            cardInfos.forEach(cardInfo -> {
                pageInfos.forEach(pageInfo -> {
                    if( StringUtil.isNotEmpty(pageInfo.getCardLayout())&&pageInfo.getCardLayout().contains(cardInfo.getCardId())){
                        cardInfo.setRefPage(true);
                    }
                });
            });
            return inlineResponse2004.data(cardInfos);
        }
        return inlineResponse2004;
    }

    @Override
    public InlineResponse2005 saveCardConfig(CommonStringInfo body) {
        return null;
    }

}
