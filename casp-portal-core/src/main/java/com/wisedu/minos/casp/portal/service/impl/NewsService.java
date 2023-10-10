package com.wisedu.minos.casp.portal.service.impl;

import com.wisedu.minos.api.model.*;
import com.wisedu.minos.api.news.DubboNewsService;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName NewsService
 * @Description 新闻相关service
 * @Date 2021/4/16 13:51
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Service
public class NewsService {

    @DubboReference
    DubboNewsService dubboNewsService;
    @Autowired
    I18nService i18nService;

    /***
     * getNewsColumnsAndChannel
     * 获取新闻的栏目和通道<p/>
     *
     * @param
     * @throws
     * @return void
     * @date 2021/4/16 13:52
     */
    public DubboResponse<List<DubboObjectVo>> getNewsColumnsAndChannel() {
        return dubboNewsService.getProgram();
    }

    /***
     * getCardChannel
     * 获取用户卡片下订阅的频道<p/>
     *
     * @param dubboCardChannelRequest
     * @throws
     * @return com.wisedu.minos.api.model.DubboResponse<java.util.List<com.wisedu.minos.api.model.DubboSubscribeVo>>
     * @date 2021/4/19 14:25
     */
    public DubboResponse<List<DubboSubscribeVo>> getCardChannel(DubboCardChannelRequest dubboCardChannelRequest) {
        return dubboNewsService.getCardChannel(dubboCardChannelRequest);
    }

    /***
     * subscribeChannel
     * 订阅频道<p/>
     *
     * @param request
     * @throws
     * @return com.wisedu.minos.api.model.DubboModel
     * @date 2021/4/19 15:32
     */
    public DubboModel subscribeChannel(DubboSubscribeRequest request){
        return  dubboNewsService.getSubscribeService(request);
    }
    /***
     * getNewsByChannelId
     * 通过通道id获取新闻<p/>
     *
     * @param request
     * @throws
     * @return com.wisedu.minos.api.model.DubboPaginationResponse<java.util.List<com.wisedu.minos.api.model.DubboNewsVo>>
     * @date 2021/4/19 15:51
     */
    public DubboPaginationResponse<List<DubboNewsVo>> getNewsByChannelId(DubboNewsRequest request){
        return  dubboNewsService.getNewsByChannelId(request);
    }

    /**
     * 根据新闻ID获取新闻详情
     */
    public DubboResponse<DubboNewsVo> getNewsByWid(String wid, String ... lang){
        return dubboNewsService.getNewsByWid(wid, lang);
    }

    /**
     * 设置新闻已读
     */
    public DubboModel updateNewsReadStatus(DubboCardNewsReadRequest request){
        return dubboNewsService.updateNewsReadStatus(request);
    }

    /**
     * 根据站点获取栏目
     */
    public DubboResponse<List<ChannelVo>> getNewsChannelByProgram(DubboNewsGetChannelRequest request){
        return dubboNewsService.getNewsChannelByProgram(request);
    }

    public DubboPaginationResponse<List<DubboNewsVo>> getNewsByFavorite(DubboNewsRequest request){
        return dubboNewsService.getNewsByFavorite(request);
    }

    public DubboModel collectNews(DubboFavoriteVo request){
        return dubboNewsService.collectNews(request);
    }

}
