package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.*;

import java.util.List;

public interface CardApiAdapter {


    /**
    * 删除部分卡片
    **/
    public InlineResponse2006 deleteCard(List<String> body);


    /**
    * 卡片管理--根据卡片WId获取卡片内容详情
    **/
    public InlineResponse2005 getCardInfo(String wid);


    /**
    * 卡片管理--获取卡片列表(有分页)
    **/
    public InlineResponse2004 queryCardList(CommonQueryListReq body);


    /**
    * 保存卡片配置
    **/
    public InlineResponse2005 saveCardConfig(CommonStringInfo body);


}
