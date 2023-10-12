package com.wisedu.amp.card.sys.mynews.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.sys.mynews.common.IaInfo;
import com.wisedu.amp.card.sys.mynews.model.NewDisplayEnum;
import com.wisedu.amp.card.sys.mynews.model.NewsColumnAndChannelEnum;
import com.wisedu.amp.card.sys.mynews.model.NewsColumnsAndChannelVo;
import com.wisedu.amp.card.sys.mynews.model.NewsVo;
import com.wisedu.amp.card.sys.mynews.model.SubscribedChannelVo;
import com.wisedu.amp.card.sys.mynews.model.TimeDisplayEnum;
import com.wisedu.minos.api.model.DubboCardChannelRequest;
import com.wisedu.minos.api.model.DubboModel;
import com.wisedu.minos.api.model.DubboMultiLangVo;
import com.wisedu.minos.api.model.DubboNewsRequest;
import com.wisedu.minos.api.model.DubboNewsVo;
import com.wisedu.minos.api.model.DubboObjectVo;
import com.wisedu.minos.api.model.DubboPaginationResponse;
import com.wisedu.minos.api.model.DubboResponse;
import com.wisedu.minos.api.model.DubboSubscribeRequest;
import com.wisedu.minos.api.model.DubboSubscribeVo;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.Global.ComponentParam;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.model.configcomponent.NewsDisplayComponent;
import com.wisedu.minos.casp.portal.service.impl.NewsService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author kaisir
 */
@MinosSPI
public class NewsAnnouncementCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(NewsAnnouncementCard.class);

    //定义script的正则表达式，去除js可以防止注入
    private static final String SCRIPT_REGEX = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
    private static final String STYLE_REGEX = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    //定义HTML标签的正则表达式，去除标签，只提取文字内容
    private static final String HTML_REGEX = "<[^>]+>";
    //定义空格,回车,换行符,制表符
    //    private static final String SPACE_REGEX = "\\s*|\t|\r|\n";
    //定义回车,换行符,制表符
    private static final String SPACE_REGEX = "\\t|\r|\n";
    // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    private static final String REG_EX_SPECIAL = "\\&[a-zA-Z]{1,10};";


    @Override
    public String getCardId() {
        return "CUS_CARD_MYNEWS";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getSubscribedChannels": //获取订阅的频道
                result = this.getSubscribedChannels(request);
                break;
            case "subscribeChannel": //订阅频道
                result = this.subscribeChannel(request);
                break;
            case "getChannelNews": //获取通道新闻
                result = this.getChannelNews(request);
                break;
            case "getConfiguredChannel": //获取配置通道
                result = this.getConfiguredChannel(request);
                break;
            default:
                break;
        }

        return result;
    }

    private Object getConfiguredChannel(CardAjaxRequest request) {
        String cardWid = request.getCardWid();
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, request.getCardId());
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        Map<String, String> param = request.getParam();
        String paramLang = StringUtil.isNotEmpty(param.get("lang"))?param.get("lang"):Global.DEFAULT_LANGUAGE;
        List<Map<String, Object>> newsContent = (List<Map<String, Object>>) configMap.get("newsContent");
        List<NewsColumnsAndChannelVo> newsColumnsAndChannelVos = filterUserColumnsAndChannel(getNewsColumnsAndChannel(paramLang),
            newsContent);
        return newsColumnsAndChannelVos;
    }

    /***
     * getChannelNews
     * 获取通道新闻<p/>
     *
     * @param request
     * @throws
     * @return java.lang.Object
     * @date 2021/4/19 15:57
     */
    private Object getChannelNews(CardAjaxRequest request) {

        String cardWid = request.getCardWid();
        Map<String, String> params = request.getParam();
        String paramLang = StringUtil.isNotEmpty(params.get("lang"))?params.get("lang"):Global.DEFAULT_LANGUAGE;
        String channelIds = params.get("channelIds");
        List<String> channelList = channelIds == null ? Collections.EMPTY_LIST
            : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(channelIds);

        //获取配置的
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, request.getCardId());
        Map<String, Object> configMap = new HashMap<>(16);
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        Map<String, Object> res = new HashMap<>(16);
        res.put("configInfo", configMap);

        if (channelList.isEmpty()) {
            res.put("datas", new PageResult<NewsVo>());
            return  res;
        }
        //请求新闻接口，封装数据
        long pageNumber;
        long pageSize;
        boolean isMobile = CommonUtil.isMobileDevice();
        if (isMobile) {
            pageNumber = Long.parseLong(params.get("pageNumber"));
            pageSize = Long.parseLong(configMap.get("newsColumns").toString());
        } else {
            pageNumber = Long.parseLong(params.get("pageNumber"));
            pageSize = Long.parseLong(configMap.get("newsTotal").toString());
        }
        DubboNewsRequest dubboNewsRequest = new DubboNewsRequest();
        dubboNewsRequest.setPageNumber(pageNumber);
        dubboNewsRequest.setPageSize(pageSize);
        dubboNewsRequest.setWid(channelList);
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            dubboNewsRequest.setUserId(currentUser.getWid());
            dubboNewsRequest.setLang(currentUser.getPreferredLanguage());
        }else{
            dubboNewsRequest.setLang(paramLang);
        }
        DubboPaginationResponse<List<DubboNewsVo>> pageRes = ApplicationContextUtil.get(NewsService.class)
            .getNewsByChannelId(dubboNewsRequest);
        if (pageRes == null) {
            logger.error("获取新闻失败，请求返回为null");
            throw new BusinessException("获取新闻失败");
        } else if (IaInfo.IA_SUCCESS_CODE.equals(pageRes.getErrcode())) {
            String newsRules = configMap.get("newsRules").toString();
            String timeDisplay = null;
            if (!CommonUtil.isMobileDevice()) {
                timeDisplay = ((Map<String, Object>) configMap.get("newsDisplay")).get("dateFormat").toString();
            }
            List<DubboNewsVo> data = pageRes.getData();
            String finalTimeDisplay = timeDisplay;
            if(CollectionUtils.isNotEmpty(data)){
                configMap.put("isEnableRead", data.get(0).getIsEnableRead());
            }
            List<NewsVo> newsVoList = data.stream().map(e -> new NewsVo()
                .title(e.getTitle())
                .channelName(e.getChannelName())
                .columnName(e.getProgramName())
                .picUrl(e.getPicUrl())
                .newDisplay(newDisplay(e.getPubTime(), newsRules))
                .pubTime(formatDateTime(e.getPubTime(), finalTimeDisplay))
                .day(getDay(e.getPubTime()))
                .month(getMonth(e.getPubTime()))
                .isTop(e.getIsTop())
                .contents(getTextFromHtml(e.getContents()))
                .url(e.getUrl())
                .abstractVal(getTextFromHtml(e.getAbstractVal()))
                .author(e.getAuthor())
                .wid(e.getWid())
                .isRead(e.getIsRead())
                .isEnableRead(e.getIsEnableRead()))
                .collect(Collectors.toList());
            PageResult<NewsVo> page = new PageResult<>();
            page.setData(newsVoList);
            page.setPageNumber(pageRes.getPageNumber());
            page.setPageSize(pageRes.getPageSize());
            page.setTotalSize(pageRes.getTotalSize());
            res.put("datas", page);
            return res;
        } else {
            logger.error("获取新闻失败，" + pageRes.getErrmsg());
            throw new BusinessException("获取新闻失败，" + pageRes.getErrmsg());
        }
    }

    /***
     * subscribeChannel
     * 订阅频道<p/>
     *
     * @param request
     * @throws
     * @return java.lang.Object
     * @date 2021/4/19 15:21
     */
    private Object subscribeChannel(CardAjaxRequest request) {
        //检验用户是否存在
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (currentUser == null) {
            throw new NoLoginException();
//            throw new BusinessException("用户未登录，不可以执行订阅操作");
        }
        String subscribedChannels = request.getParam().get("channelIds");
        List<String> channels = subscribedChannels == null ? Collections.EMPTY_LIST
            : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(subscribedChannels);
        String cardWid = request.getCardWid();
        DubboSubscribeRequest dubboSubscribeRequest = new DubboSubscribeRequest();
        dubboSubscribeRequest.setCardWid(cardWid);
        dubboSubscribeRequest.setUserWid(currentUser.getWid());
        dubboSubscribeRequest.setWid(channels);
        //查询塞入新闻卡片配置的栏目
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, request.getCardId());
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        JSONArray newsContent = (JSONArray) configMap.get("newsContent");
        List<String> unfixedWids = newsContent.stream().filter(e -> !((JSONObject) e).getBoolean("fixed"))
                .map(e -> ((JSONObject) e).getString("id"))
                .collect(Collectors.toList());
        dubboSubscribeRequest.setChannelList(unfixedWids);
        DubboModel model = ApplicationContextUtil.get(NewsService.class)
            .subscribeChannel(dubboSubscribeRequest);
        if (model == null) {
            logger.error("订阅接口返回结果为null");
            throw new BusinessException("订阅失败");

        } else if (IaInfo.IA_SUCCESS_CODE.equals(model.getErrcode())) {
            return true;
        } else {
            throw new BusinessException("订阅失败，" + model.getErrmsg());
        }
    }

    /***
     * getSubscribedChannels
     * 获取订阅的频道<p/>
     *
     * @param request
     * @throws
     * @return java.lang.Object
     * @date 2021/4/19 10:31
     */
    private Object getSubscribedChannels(CardAjaxRequest request) {
        String cardWid = request.getCardWid();
        Map<String, String> param = request.getParam();
        String paramLang = StringUtil.isNotEmpty(param.get("lang"))?param.get("lang"):Global.DEFAULT_LANGUAGE;
        //1.获取本地的固定频道
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, request.getCardId());
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        JSONArray newsContent = (JSONArray) configMap.get("newsContent");
        List<String> fixedIds = newsContent.stream().filter(e -> ((JSONObject) e).getBoolean("fixed"))
            .map(e -> ((JSONObject) e).getString("id"))
            .collect(Collectors.toList());
        List<String> localIds = newsContent.stream()
            .map(e -> ((JSONObject) e).getString("id"))
            .collect(Collectors.toList());
        //2.调用接口回去订阅频道
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        DubboCardChannelRequest dubboCardChannelRequest = new DubboCardChannelRequest();
        dubboCardChannelRequest.setCardWid("");
        dubboCardChannelRequest.setLang(currentUser==null?paramLang:currentUser.getPreferredLanguage());
        dubboCardChannelRequest.setUserWid(currentUser == null ? null : currentUser.getWid());
        dubboCardChannelRequest.setChannelWid(fixedIds);
        DubboResponse<List<DubboSubscribeVo>> cardChannel = ApplicationContextUtil.get(NewsService.class)
            .getCardChannel(dubboCardChannelRequest);
        if (cardChannel == null) {
            logger.error("获取用户订阅的频道为null");
        } else if (IaInfo.IA_SUCCESS_CODE.equals(cardChannel.getErrcode())) {
            if (cardChannel.getData() == null) {
                return CollectionUtils.EMPTY_COLLECTION;
            }

            List<NewsColumnsAndChannelVo> vos = getNewsColumnsAndChannel(paramLang);
            List<NewsColumnsAndChannelVo> tree = new NewsAnnouncementCard().buildTree(vos);
            List<NewsColumnsAndChannelVo> sortList = new NewsAnnouncementCard().treeToList(tree);
            List<String> subscribedWids = cardChannel.getData().stream().filter(e->localIds.contains(e.getChannelWid())).map(DubboSubscribeVo::getChannelWid)
                .collect(Collectors.toList());

            //  按配置文件顺序
            Set<String> channelIds = sortList.stream().map(NewsColumnsAndChannelVo::getId).collect(Collectors.toSet());
            Map<String, NewsColumnsAndChannelVo> map = sortList.stream().collect(Collectors.toMap(NewsColumnsAndChannelVo::getId, e -> e));
            return localIds.stream().filter(e -> channelIds.contains(e) && subscribedWids.contains(e) ).map(e -> {
                NewsColumnsAndChannelVo vo = map.get(e);
                return new SubscribedChannelVo()
                        .setName(vo.getName())
                        .setFixed(fixedIds.contains(vo.getId()))
                        .setWid(vo.getId());
            }).collect(Collectors.toList());
            } else {
            logger.error("获取用户订阅的频道失败，" + cardChannel.getErrmsg());
        }
        return CollectionUtils.EMPTY_COLLECTION;
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();
        List<NewsColumnsAndChannelVo> newsColumnsAndChannel = getNewsColumnsAndChannel(Global.DEFAULT_LANGUAGE);
        List<NewsColumnsAndChannelVo> tree = new NewsAnnouncementCard().buildTree(newsColumnsAndChannel);
        List<NewsColumnsAndChannelVo> sortList = new NewsAnnouncementCard().treeToList(tree);
        Object value = componentContainer.getComponents().stream()
            .filter(e -> "newsContent".equals(e.getKey())).findFirst().orElse(new NewsDisplayComponent())
            .getValue();
        List<String> localFixedId=new ArrayList<>(16);
        List<String> localId=new ArrayList<>(16);
      if(value instanceof List && !((List) value).isEmpty()){
            ((JSONArray)value).forEach(e->{
                localId.add(((JSONObject)e).getString("id"));
                if(((JSONObject)e).getBoolean("fixed")){
                    localFixedId.add(((JSONObject)e).getString("id"));
                }
            });
        }
        Set<String> channelIds = sortList.stream().map(NewsColumnsAndChannelVo::getId).collect(Collectors.toSet());
        // 不需要按照新闻的顺序，按照原有顺序
        List<HashMap> collect = localId.stream().filter(channelIds::contains).map(e -> {
            HashMap hashMap = new HashMap();
            hashMap.put("id", e);
            hashMap.put("fixed", localFixedId.contains(e));
            return hashMap;
        }).collect(Collectors.toList());
        componentContainer.setData("newsContent", Global.ComponentParam.DATAS, sortList);
        componentContainer.setData("newsContent", ComponentParam.VALUE, collect);

        return componentContainer;
    }

    /**
     * 去除html代码中含有的标签
     * @param htmlStr
     * @return
     */
    private String delHtmlTags(String htmlStr) {
        // 过滤script标签
        htmlStr = htmlStr.replaceAll(SCRIPT_REGEX, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(STYLE_REGEX, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(HTML_REGEX, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(SPACE_REGEX, "");
        // 过滤特殊字符
        htmlStr = htmlStr.replaceAll(REG_EX_SPECIAL, "");
        // 返回文本字符串
        return htmlStr.trim();
    }

    /**
     * 获取HTML代码里的内容
     * @param content
     * @return
     */
    private String getTextFromHtml(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        } else {
            return delHtmlTags(content);
        }
    }


    /***
     * newDisplay
     * 根据时间判断new标签是否展示<p/>
     *
     * @param pubTime
     * @param code
     * @throws
     * @return boolean
     * @date 2021/4/20 14:35
     */
    boolean newDisplay(LocalDateTime pubTime, String code) {
        if (NewDisplayEnum.HIDDEN.getCode().equals(code)) {
            return false;
        }
        if (pubTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(pubTime, now);
        if (NewDisplayEnum.ONE_DAY.getCode().equals(code)) {
            return duration.toHours() < 24;
        } else if (NewDisplayEnum.THREE_DAYS.getCode().equals(code)) {
            return duration.toHours() < 24 * 3;
        } else if (NewDisplayEnum.ONE_WEEK.getCode().equals(code)) {
            return duration.toHours() < 24 * 7;
        }
        return false;
    }

    /***
     * formatDateTime
     * 时间格式化<p/>
     *
     * @param time
     * @param code
     * @throws
     * @return java.lang.String
     * @date 2021/4/20 14:21
     */
    public String formatDateTime(LocalDateTime time, String code) {
        if (time == null) {
            return null;
        }
        //移动端的code为null
        if (code == null) {
            DateTimeFormatter commonFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            return commonFormatter.format(time);
        }
        if (TimeDisplayEnum.HIDDEN.getCode().equals(code)) {
            return null;
        } else if (TimeDisplayEnum.MONTH_FORMAT.getCode().equals(code)) {
            DateTimeFormatter mothFormatter = DateTimeFormatter.ofPattern("yyyy/MM");
            return time.format(mothFormatter);
        } else if (TimeDisplayEnum.DAY_FORMAT.getCode().equals(code)) {
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return time.format(dayFormatter);
        } else if (TimeDisplayEnum.MINUTE_FORMAT.getCode().equals(code)) {
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            return time.format(dayFormatter);
        }
        return null;
    }

    /***
     * getDay
     * 获取时间<p/>
     *
     * @param time
     * @throws
     * @return java.lang.String
     * @date 2021/4/20 14:36
     */
    private String getDay(LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            String day = time.getDayOfMonth() + "";
            return day.indexOf("0") == 0 ? day.substring(1) : day;
        }
    }

    /***
     * getMonth
     * 获取月份<p/>
     *
     * @param time
     * @throws
     * @return java.lang.String
     * @date 2021/4/20 14:36
     */
    private String getMonth(LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            String month = time.getMonth().getValue() + "";
            return month.indexOf("0") == 0 ? month.substring(1) : month;
        }
    }

    /**
     * String转Map
     *
     * @param json
     * @return
     */
    public Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !"[]".equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }

    /***
     * getNewsColumnsAndChannel
     * 获取新闻栏目和通道<p/>
     *
     * @param
     * @throws
     * @return void
     * @date 2021/4/16 13:47
     */
    public List<NewsColumnsAndChannelVo> getNewsColumnsAndChannel(String lang) {
        DubboResponse<List<DubboObjectVo>> newsDubboRes = ApplicationContextUtil.get(NewsService.class)
            .getNewsColumnsAndChannel();
        if (IaInfo.IA_SUCCESS_CODE.equals(newsDubboRes.getErrcode())) {
            UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if (null != currentUser) {
                lang = currentUser.getPreferredLanguage();
            } else {
                lang = StringUtil.isNotEmpty(lang) ? lang : Global.DEFAULT_LANGUAGE;
            }
            String finalLang = lang;
            return newsDubboRes.getData().stream().map(e ->
                new NewsColumnsAndChannelVo().setId(e.getWid())
                    .setParentId(e.getParentWid())
                    .setIsChannel(NewsColumnAndChannelEnum.CHANNEL.getCode() == e.getType())
                    .setName(getName(e.getName(), finalLang))
                    .setSort(e.getSort())
            ).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public String getName(List<DubboMultiLangVo> langList,String currentLang) {
        if (CollectionUtils.isEmpty(langList)) {
            return "";
        }

        for(DubboMultiLangVo dubboMultiLangVo:langList) {
            if (dubboMultiLangVo.getLangKey().equals(currentLang) && org.apache.commons.lang3.StringUtils.isNotBlank(dubboMultiLangVo.getLangValue())) {
                return dubboMultiLangVo.getLangValue();
            }
        }
         return langList.stream().filter(e -> e.getLangKey().equals(Global.DEFAULT_LANGUAGE)).findAny()
                 .orElse(langList.get(0)).getLangValue();
    }

    /***
     * filterUserColumnsAndChannel
     * <p/>
     *
     * @param remoteNews  远端的新闻
     * @param localColumns 本地栏目
     * @throws
     * @return java.util.Set<com.wisedu.amp.card.sys.mynews.model.NewsColumnsAndChannelVo>
     * @date 2021/5/13 15:53
     */
    public List<NewsColumnsAndChannelVo> filterUserColumnsAndChannel(List<NewsColumnsAndChannelVo> remoteNews,
        List<Map<String, Object>> localColumns) {
        List<String> fixedColumns = localColumns.stream().filter(e -> (boolean) e.get("fixed"))
            .map(e -> (String) e.get("id")).collect(Collectors.toList());
        List<String> allColumns = localColumns.stream().map(e -> (String) e.get("id")).collect(Collectors.toList());
        Set<NewsColumnsAndChannelVo> parents = new HashSet<>(16);
        List<NewsColumnsAndChannelVo> localColumnNames = remoteNews.stream().filter(e -> allColumns.contains(e.getId()))
            .collect(Collectors.toList());
        localColumnNames.forEach(e -> findParent(e, remoteNews, parents));
        List<String> existParentIds = parents.stream().map(NewsColumnsAndChannelVo::getId)
            .collect(Collectors.toList());
        List<NewsColumnsAndChannelVo> collect = remoteNews.stream()
                //如果是栏目要提取 如果是频道要本地的频道
                .filter(e -> (! e.getIsChannel() && existParentIds.contains(e.getId())) || (e.getIsChannel() && allColumns
                        .contains(e.getId())))
                .peek(e -> e.setIsFixed(fixedColumns.contains(e.getId()))).collect(Collectors.toList());
        return collect;
    }

    public void findParent(NewsColumnsAndChannelVo child, List<NewsColumnsAndChannelVo> list
        , Set<NewsColumnsAndChannelVo> data) {
        if (child == null || "-1".equals(child.getParentId())) {
        } else {
            List<NewsColumnsAndChannelVo> collect = list.stream()
                .filter(e -> e.getId().equals(child.getParentId())).collect(Collectors.toList());
            data.addAll(collect);
            findParent(collect.isEmpty() ? null : collect.get(0), list, data);
        }
    }

    public List<NewsColumnsAndChannelVo> buildTree(List<NewsColumnsAndChannelVo> list) {
        List<NewsColumnsAndChannelVo> tree = new ArrayList<>();
        for (NewsColumnsAndChannelVo node : list) {
            if ("-1".equals(node.getParentId())) {
                tree.add(findChild(node, list));
            }
        }
        return tree;
    }

    public NewsColumnsAndChannelVo findChild(NewsColumnsAndChannelVo node, List<NewsColumnsAndChannelVo> list) {
        for (NewsColumnsAndChannelVo n : list) {
            if (n.getParentId().equals(node.getId())) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<>());
                }
                node.getChildren().add(findChild(n, list));
            }
        }
        return node;
    }

    public List<NewsColumnsAndChannelVo> treeToList( List<NewsColumnsAndChannelVo>  tree){
       List<NewsColumnsAndChannelVo> list = new ArrayList<>();
        Collections.sort(tree);
        for (int i=0;i<tree.size();i++){
            NewsColumnsAndChannelVo newsColumnsAndChannelVo = tree.get(i);
            List<NewsColumnsAndChannelVo> children = newsColumnsAndChannelVo.getChildren();
            list.addAll(CollectionUtils.isEmpty(children)? Lists.newArrayList():treeToList(children));
            list.addAll(Lists.newArrayList(newsColumnsAndChannelVo));

        }
        return list;
    }

    public static void main(String[] args) {
//        NewsColumnsAndChannelVo v1 = new NewsColumnsAndChannelVo()
//            .setSort(1)
//            .setName("1")
//            .setIsChannel(true)
//            .setId("1")
//            .setParentId("-1");
//
//        NewsColumnsAndChannelVo v2 = new NewsColumnsAndChannelVo()
//            .setSort(2)
//            .setName("2")
//            .setIsChannel(true)
//            .setId("2")
//            .setParentId("-1");
//
//        NewsColumnsAndChannelVo v3 = new NewsColumnsAndChannelVo()
//            .setSort(3)
//            .setName("3")
//            .setId("3")
//            .setIsChannel(false)
//            .setParentId("-1");
//
//
//        NewsColumnsAndChannelVo v11 = new NewsColumnsAndChannelVo()
//            .setSort(1)
//            .setName("11")
//            .setId("11")
//            .setIsChannel(true)
//            .setParentId("1");
//
//        NewsColumnsAndChannelVo v12 = new NewsColumnsAndChannelVo()
//            .setSort(2)
//            .setName("12")
//            .setId("12")
//            .setIsChannel(false)
//            .setParentId("1");
//        NewsColumnsAndChannelVo v121 = new NewsColumnsAndChannelVo()
//            .setSort(2)
//            .setName("121")
//            .setId("121")
//            .setIsChannel(true)
//            .setParentId("12");
//
//        ArrayList<NewsColumnsAndChannelVo> vos = new ArrayList<>();
//        vos.add(v1);
//        vos.add(v2);
//        vos.add(v3);
//        vos.add(v11);
//        vos.add(v12);
//        vos.add(v121);
//
//        List<NewsColumnsAndChannelVo> tree = new NewsAnnouncementCard().buildTree(vos);
//        List<NewsColumnsAndChannelVo> sortList = new NewsAnnouncementCard().treeToList(tree);
//        System.out.println("-------------");
    }

}
