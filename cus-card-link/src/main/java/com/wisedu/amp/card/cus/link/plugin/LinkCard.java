package com.wisedu.amp.card.cus.link.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cus.link.model.*;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.SysIconEntity;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.SysIconService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.vo.SysInconVo;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MinosSPI
public class LinkCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(LinkCard.class);

    @Override
    public String getCardId() {
        return "CUS_CARD_LINK";
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();

        //获取目前所支持的语言类型
        DubboI18nService dubboI18nService = ApplicationContextUtil.get(DubboI18nService.class);
        List<Lang> supportLanguages = dubboI18nService.getSupportLanguages();
        //bean参数的转换，转换成自己定义的bean
        List<LangResoure> languageBizList = new ArrayList<>();
        setLanguages(supportLanguages, languageBizList);

        componentContainer.getComponents().forEach(abstractComponent -> {
            //修改addLinkList组件中配置的值中的label的国际化语言，使其与当前所支持的语言同步
            if ("linkList".equals(abstractComponent.getKey()) && null != abstractComponent.getValue()) {
                //从配置中取出value 并转成相应对象
                List<LinkModel> linkModels = JSONObject.parseArray(JSON.toJSONString(abstractComponent.getValue()), LinkModel.class);
                //存放value的list
                for(LinkModel linkModel : linkModels){
                    processLinkModel(languageBizList, linkModel);
                }
                abstractComponent.setValue(linkModels);
                abstractComponent.setDatas(languageBizList);
            }
        });
        return componentContainer;
    }

    private void setLanguages(List<Lang> supportLanguages, List<LangResoure> languageBizList) {
        for (Lang lang : supportLanguages) {
            LangResoure languageBiz = new LangResoure();
            languageBiz.setLangKey(lang.getLangCode());
            languageBiz.setLangValue("");
            languageBiz.setName(lang.getLangName());
            languageBizList.add(languageBiz);
        }
    }

    private void processLinkModel(List<LangResoure> languageBizList, LinkModel linkModel) {
        //从配置中取出的多语言数据
        List<LangResoure> langResoures = linkModel.getLabel();
        //存放处理过的多语言
        List<LangResoure> languageBizListNew = new ArrayList<>();
        for(LangResoure languageBiz : languageBizList){
            LangResoure langResoureNew = new LangResoure();
            processLangResource(langResoures, languageBiz, langResoureNew);
            languageBizListNew.add(langResoureNew);
        }
        linkModel.setLabel(languageBizListNew);
    }

    private void processLangResource(List<LangResoure> langResoures, LangResoure languageBiz, LangResoure langResoureNew) {
        for(LangResoure langResoureConfig : langResoures){
            if(null != langResoureConfig.getLangKey()){
                if (langResoureConfig.getLangKey().equals(languageBiz.getLangKey())){
                    langResoureNew.setLangKey(langResoureConfig.getLangKey());
                    langResoureNew.setLangValue(langResoureConfig.getLangValue());
                    langResoureNew.setName("");
                }
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "renderData":
                result = this.renderData2(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "getIconList":
                result = this.getIconList(request);
                break;
        }

        return result;
    }


    /**
     * 重构后台配置修改或重写的方法
     */


    /**
     * renderData 方法
     * @param request
     * @return
     */
    private Map<String,Object> renderData2(CardAjaxRequest request) {

        //获取配置信息
        String configInfoStr = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        if (StringUtil.isNotEmpty(configInfoStr)) {
            configInfoStr = JSON.parse(configInfoStr).toString();
        }
        ConfigInfoModel configInfo = JSONObject.parseObject(configInfoStr, ConfigInfoModel.class);
        if (null == configInfo) {
            configInfo = new ConfigInfoModel();
            HeightModel heightModel = new HeightModel();
            heightModel.setType(0);
            heightModel.setValue(500);
            configInfo.setLinkHeight(heightModel);
        }

        Map<String,Object> results = new HashMap<>();

        results.put("cardId", getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("configInfo", configInfo);

        return results;
    }

    /**
     * 获取icon图标 的 分页接口
     * @param request
     * @return
     */
    private Map<String, Object> getIconList(CardAjaxRequest request) {

        String pageNum = request.getParam().get("pageNum");

        SysInconVo sysInconVo = new SysInconVo();

        sysInconVo.setIconType("0");
        sysInconVo.setPageSize(60);
        sysInconVo.setPageNumber(Integer.parseInt(null == pageNum || "".equals(pageNum) ? "1" : pageNum));

        PageResult<SysIconEntity> result = ApplicationContextUtil.get(SysIconService.class).getSysInconPage(sysInconVo);

        List<Map<String, Object>> iconList = new ArrayList<>();
//        List<SysIconEntity> iconEntityList = new ArrayList<>();
        String countStart = "";
        String countEnd = "";
        Long totalSize = 0L;
        Long pageSize;
        Long pageNumber = 0L;
        long totalPage = 0L;

        if (null != result && null != result.getData() && result.getData().size() > 0) {
            for (int i = 0; i < result.getData().size(); i++) {
                Map<String, Object> iconMap = new HashMap<>();
                SysIconEntity sysIcon = result.getData().get(i);
                iconMap.put("iconUrl", sysIcon.getIconUrl());
                iconList.add(iconMap);
            }
//            iconEntityList = result.getData();

            countStart = StringUtil.getString((result.getPageNumber() - 1) * result.getPageSize() + 1);
            countEnd = StringUtil.getString(result.getPageNumber() * result.getPageSize());
            totalSize = result.getTotalSize();
            pageSize = result.getPageSize();
            totalPage = totalSize / pageSize;
            pageNumber = result.getPageNumber();
        }

        Map<String, Object> list = new HashMap();
        list.put("cardId", getCardId());
        list.put("cardWid", request.getCardWid());
        list.put("totalSize", totalSize);
        list.put("totalPage", totalPage + 1);
        list.put("currentPage", pageNumber);
        list.put("currentCount", countStart + "~" + (Long.parseLong(countEnd) > totalSize ? totalSize : countEnd));
        list.put("iconData", iconList);

        return list;
    }
}
