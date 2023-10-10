package com.wisedu.amp.card.cus.oa.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wisedu.amp.card.cus.oa.model.NewDisplayEnum;
import com.wisedu.amp.card.cus.oa.model.NewsVo;
import com.wisedu.amp.card.cus.oa.model.TimeDisplayEnum;
import com.wisedu.amp.card.cus.oa.utils.HttpUtils;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import jdk.nashorn.internal.scripts.JO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author kaisir
 */
@MinosSPI
public class OaCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(OaCard.class);

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
    private static final String SUBSCRIBE_PROGRAMME = "subscribe_programme";


    //办公信息
    private static String bgxxUrl = "https://oa.cuc.edu.cn/r/w?cmd=com.actionsoft.apps.cms_get_site_widget_list";

    //发文
    private static String fwUrl = "https://oa.cuc.edu.cn/r/w?cmd=com.actionsoft.apps.cms_get_fw_list&limit=";




    @Override
    public String getCardId() {
        return "CUS_CARD_OA";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getChannelNews": //获取通道新闻
                result = this.getChannelNews(request);
                break;
            default:
                break;
        }

        return result;
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

        //检验用户是否存在
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (currentUser == null) {
            throw new NoLoginException();
        }

        Map<String, String> params = request.getParam();

        //获取配置信息
        Map<String, Object> configMap = getCardConfigMap(request);
        Map<String, Object> res = new HashMap<>(16);
        res.put("configInfo", configMap);


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

        String newsRules = configMap.get("newsRules").toString();
        String timeDisplay = null;
        if (!CommonUtil.isMobileDevice()) {
            timeDisplay = ((Map<String, Object>) configMap.get("newsDisplay")).get("dateFormat").toString();
        }

        String finalTimeDisplay = timeDisplay;

        List<NewsVo> newsVoList = new ArrayList();


        String ggType = configMap.get("ggType").toString();

        logger.info("=======公告类型====ggType====="+ggType);

        //开始具体逻辑
        if(StringUtils.equals(ggType,"1")){
            String  bgxxResult = HttpUtils.get(bgxxUrl+"&uid="+currentUser.getUserAccount()+"&limit="+pageSize);
            logger.info("=======bgxxResult========="+bgxxResult);
            //String  bgxxResult = "{\"msg\":\"\",\"result\":\"ok\",\"data\":[{\"releaseTime\":1670497840000,\"readNum\":29,\"author\":\"人事处001\",\"replyFlag\":\"显示发布人 记录阅读信息\",\"zdFlag\":1,\"releaseDepartMent\":\"人事处\",\"id\":\"c13dc6b0-0a67-49f3-914a-4387b18d609f\",\"source\":\"单位信息\",\"title\":\"测试1208\",\"bindId\":\"c79b7b08-9e8a-4991-a91a-00445057357c\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=c13dc6b0-0a67-49f3-914a-4387b18d609f\"},{\"releaseTime\":1670471910000,\"readNum\":29,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人\",\"zdFlag\":1,\"releaseDepartMent\":\"人事处\",\"id\":\"69b5e346-38f4-4bcb-8fa1-4c909d2217c5\",\"source\":\"单位信息\",\"title\":\"测试\",\"bindId\":\"c9aa5b6f-3d39-4862-80f7-dd2d0a565b8c\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=69b5e346-38f4-4bcb-8fa1-4c909d2217c5\"},{\"releaseTime\":1669172191000,\"readNum\":56,\"author\":\"管理员\",\"zdFlag\":1,\"releaseDepartMent\":\"部门1\",\"id\":\"16ccdac2-1f1a-46c5-8b09-25a74f81b0eb\",\"source\":\"单位信息\",\"title\":\"2222222222\",\"bindId\":\"ff42ab7b-2536-49ad-a787-6c9f35b19df9\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=16ccdac2-1f1a-46c5-8b09-25a74f81b0eb\"},{\"releaseTime\":1670507496000,\"readNum\":8,\"author\":\"管理员\",\"zdFlag\":0,\"releaseDepartMent\":\"部门1\",\"id\":\"5aa665c6-92af-42bb-afca-68515b7c93ea\",\"source\":\"单位信息\",\"title\":\"123123123123\",\"bindId\":\"95ca4018-dbe8-4e70-a068-d93ae847c4bb\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=5aa665c6-92af-42bb-afca-68515b7c93ea\"},{\"releaseTime\":1670505916000,\"readNum\":3,\"author\":\"办公室001\",\"replyFlag\":\"显示发布人 记录阅读信息\",\"zdFlag\":0,\"releaseDepartMent\":\"党政办公室\",\"id\":\"8d34e107-66b8-4d5f-afc5-acfec54d11da\",\"source\":\"单位信息\",\"title\":\"测试\",\"bindId\":\"a2ae33ad-9364-4a94-a81f-12bff2049271\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=8d34e107-66b8-4d5f-afc5-acfec54d11da\"},{\"releaseTime\":1670505562000,\"readNum\":2,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人 记录阅读信息\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"ac9de808-42ce-43b0-b6ba-d267150f0bb8\",\"source\":\"单位信息\",\"title\":\"测试数据1208first\",\"bindId\":\"6c17821c-bc6a-435b-8d7b-67e94004784a\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=ac9de808-42ce-43b0-b6ba-d267150f0bb8\"},{\"releaseTime\":1670503044000,\"readNum\":8,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人 记录阅读信息 允许打印\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"2a87f557-a4bd-494b-880d-750d8ccee07f\",\"source\":\"单位信息\",\"title\":\"测试\",\"bindId\":\"dde1aed5-5ba2-4932-a3b7-f88baf87ddc6\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=2a87f557-a4bd-494b-880d-750d8ccee07f\"},{\"releaseTime\":1670472036000,\"readNum\":38,\"author\":\"管理员\",\"replyFlag\":\"显示发布人\",\"zdFlag\":0,\"releaseDepartMent\":\"部门1\",\"id\":\"b29a46aa-35d3-4931-b113-f2023dddfaa0\",\"source\":\"单位信息\",\"title\":\"测试通知数据，请勿点击阅读或办理！\",\"bindId\":\"b0742647-4560-4cc4-84b1-c1cab550c59c\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=b29a46aa-35d3-4931-b113-f2023dddfaa0\"},{\"releaseTime\":1670471496000,\"readNum\":15,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"8e25bda6-b342-4153-a31a-da227617d5b2\",\"source\":\"单位信息\",\"title\":\"测试数据\",\"bindId\":\"928ae2b1-857e-488c-b0ae-c706c03dcb34\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=8e25bda6-b342-4153-a31a-da227617d5b2\"},{\"releaseTime\":1670467965000,\"readNum\":36,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"5b06dfad-fcf5-4b39-b4d1-fa7862fab118\",\"source\":\"单位信息\",\"title\":\"测试数据1208返回效果\",\"bindId\":\"90e33786-6790-43e1-a29e-2bd4b6ea3429\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=5b06dfad-fcf5-4b39-b4d1-fa7862fab118\"},{\"releaseTime\":1670464864000,\"readNum\":10,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"67e9c7ff-f702-4fad-96e7-9ed1b4295cbf\",\"source\":\"单位信息\",\"title\":\"测试数据1208zr\",\"bindId\":\"0f74bacf-1942-42af-8afa-e5d6d038b1a3\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=67e9c7ff-f702-4fad-96e7-9ed1b4295cbf\"},{\"releaseTime\":1670464685000,\"readNum\":21,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人 记录阅读信息 允许打印\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"6e925604-c09d-4c09-a27e-f486cdc1b902\",\"source\":\"个人信息\",\"title\":\"发布办公信息\",\"bindId\":\"89be77b2-00c3-4c2f-9ab8-3f37f8956168\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=6e925604-c09d-4c09-a27e-f486cdc1b902\"},{\"releaseTime\":1669687571000,\"readNum\":76,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人 记录阅读信息\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"10592d37-97ef-4e45-961b-47e8cd15cd48\",\"source\":\"单位信息\",\"title\":\"发布公告内容\",\"bindId\":\"3830f635-c4f3-4a96-ae2c-761bea6beee3\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=10592d37-97ef-4e45-961b-47e8cd15cd48\"},{\"releaseTime\":1669272617000,\"readNum\":34,\"author\":\"人事处003\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"4954e043-7554-490a-a986-9bce4fadc842\",\"source\":\"个人信息\",\"title\":\"测试\",\"bindId\":\"ce90dee4-2359-4e14-87fe-ace426220b37\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=4954e043-7554-490a-a986-9bce4fadc842\"},{\"releaseTime\":1669272566000,\"readNum\":14,\"author\":\"人事处002\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"5a9b11a8-f146-4a99-b1ed-375e54c24a26\",\"source\":\"单位信息\",\"title\":\"测试\",\"bindId\":\"a40f6b9f-0922-4cdb-b49a-f0d77de25bee\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=5a9b11a8-f146-4a99-b1ed-375e54c24a26\"},{\"releaseTime\":1669259287000,\"readNum\":33,\"author\":\"人事处003\",\"replyFlag\":\"显示发布人 记录阅读信息 允许打印\",\"zdFlag\":0,\"releaseDepartMent\":\"人事处\",\"id\":\"220bfb24-4b31-4741-a772-1740bcf5aacd\",\"source\":\"单位信息\",\"title\":\"协同办公系统测试\",\"bindId\":\"55511719-3a58-4297-9c12-ce0ce052c25c\",\"url\":\"http://oa.cuc.edu.cn/integrate/CmsPage.jsp?uid=admin&id=220bfb24-4b31-4741-a772-1740bcf5aacd\"}],\"id\":\":RO;\"}";

            JSONObject bgxxResultObj = JSON.parseObject(bgxxResult);

            String result = bgxxResultObj.getString("result");

            if(StringUtils.equals(result,"ok")){

                String data = bgxxResultObj.getString("data");

                List<Map<String,String>> dataList = JSON.parseObject(data,new TypeReference<List<Map<String,String>>>(){});
                for (int i = 0; i < dataList.size(); i++) {

                    Map<String,String> tempMap = dataList.get(i);
                    String title = tempMap.get("title");
                    String releaseDepartMent = tempMap.get("releaseDepartMent");
                    String author = tempMap.get("author");

                    logger.info("=======title========="+title);

                    String detail_url = tempMap.get("url");

                    String releaseTime = tempMap.get("releaseTime");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long releaseTimeLong = new Long(releaseTime);
                    Date date = new Date(releaseTimeLong);
                    String releaseTimeStr = simpleDateFormat.format(date);

                    LocalDateTime localDate=LocalDateTime.parse(releaseTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    NewsVo newsVo = new NewsVo();
                    newsVo.setTitle(title);
                    newsVo.setUrl(detail_url);
                    newsVo.setNewDisplay(newDisplay(localDate, newsRules));
                    newsVo.setPubTime(formatDateTime(localDate, finalTimeDisplay));
                    newsVo.setDay(getDay(localDate));
                    newsVo.setMonth(getMonth(localDate));
                    newsVo.setAuthor(author);
                    newsVo.setDept(releaseDepartMent);
                    //newsVo.isTop();
                    //newsVo.isRead();
                    newsVoList.add(newsVo);
                }




            }else{
                res.put("datas", null);
                return res;
            }
        }else{
            String  fwResult = HttpUtils.get(fwUrl+pageSize);
            logger.info("=======fwResult========="+fwResult);
            //String  fwResult = "{\"msg\":\"\",\"result\":\"ok\",\"data\":[{\"BINDID\":\"eed26fe6-4438-4864-a1f7-6843c88a8e4c\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=eed26fe6-4438-4864-a1f7-6843c88a8e4c\"},{\"BINDID\":\"71bad25b-6652-4e71-8214-4035a0f68f38\",\"TITLE\":\"测试数据1208\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=71bad25b-6652-4e71-8214-4035a0f68f38\"},{\"BINDID\":\"6d4f6186-61bd-4db4-86b5-2c5e1c169efa\",\"TITLE\":\"发布人事任免制度\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=6d4f6186-61bd-4db4-86b5-2c5e1c169efa\"},{\"BINDID\":\"5d463e65-ed12-4468-a51a-3de1e86a1761\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=5d463e65-ed12-4468-a51a-3de1e86a1761\"},{\"BINDID\":\"a766f9aa-caf2-4128-b423-d58b1051f52f\",\"TITLE\":\"1111111111\",\"NGRQ\":\"2022-12-07\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=a766f9aa-caf2-4128-b423-d58b1051f52f\"},{\"BINDID\":\"a9042cf4-ecec-4e5c-81ff-844eab76059f\",\"TITLE\":\"1111111\",\"NGRQ\":\"2022-12-01\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=a9042cf4-ecec-4e5c-81ff-844eab76059f\"},{\"BINDID\":\"7eb224c2-5e6c-4055-9ed2-1936a42048c3\",\"TITLE\":\"有关单位全体人员\",\"NGRQ\":\"2022-12-01\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=7eb224c2-5e6c-4055-9ed2-1936a42048c3\"},{\"BINDID\":\"7335a3a1-05f9-4533-a812-fc955ca12d67\",\"TITLE\":\"关于人事任免的xx制度\",\"NGRQ\":\"2022-11-25\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=7335a3a1-05f9-4533-a812-fc955ca12d67\"},{\"BINDID\":\"bf85af18-6c09-4f21-99d8-dbed6951588b\",\"TITLE\":\"\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"党政办公室\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=bf85af18-6c09-4f21-99d8-dbed6951588b\"},{\"BINDID\":\"c657bcfe-d564-455c-b696-5ebe8b2ee09e\",\"TITLE\":\"文件字\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=c657bcfe-d564-455c-b696-5ebe8b2ee09e\"},{\"BINDID\":\"270c26a3-a2aa-4c89-bffa-928b45b019ae\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=270c26a3-a2aa-4c89-bffa-928b45b019ae\"},{\"BINDID\":\"45863df5-0a29-484d-9c57-3cae9b95c3e2\",\"TITLE\":\"关于人事任免类制度的审查\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=45863df5-0a29-484d-9c57-3cae9b95c3e2\"},{\"BINDID\":\"bae58b5d-ffd2-408f-84c1-76412102e400\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-11-23\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=bae58b5d-ffd2-408f-84c1-76412102e400\"}],\"id\":\":RO;\"}";

            JSONObject fwResultObj = JSON.parseObject(fwResult);

            String result = fwResultObj.getString("result");

            if(StringUtils.equals(result,"ok")){

                String data = fwResultObj.getString("data");

                List<Map<String,String>> dataList = JSON.parseObject(data,new TypeReference<List<Map<String,String>>>(){});
                for (int i = 0; i < dataList.size(); i++) {

                    Map<String,String> tempMap = dataList.get(i);

                    String title = tempMap.get("TITLE");
                    String author = tempMap.get("ZBDW");
                    String zbdw = tempMap.get("ZBDW");

                    logger.info("=======title========="+title);

                    String pcUrl = tempMap.get("pcUrl");

                    logger.info("=======pcUrl========="+pcUrl);


                    String mobileUrl = tempMap.get("mobileUrl");

                    logger.info("=======mobileUrl========="+mobileUrl);

                    String ngrq = tempMap.get("NGRQ") + " 00:00:00";

                    LocalDateTime localDate=LocalDateTime.parse(ngrq, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    logger.info("=======localDate========="+localDate);

                    NewsVo newsVo = new NewsVo();
                    newsVo.setTitle(title);
                    newsVo.setUrl(pcUrl);
                    newsVo.setNewDisplay(newDisplay(localDate, newsRules));
                    newsVo.setPubTime(formatDateTime(localDate, finalTimeDisplay));
                    newsVo.setDay(getDay(localDate));
                    newsVo.setMonth(getMonth(localDate));
                    newsVo.setAuthor(author);
                    newsVo.setDept(zbdw);
                    //newsVo.isTop();
                    //newsVo.isRead();
                    newsVoList.add(newsVo);

                }




            }else{
                res.put("datas", null);
                return res;
            }
        }


        //结束具体逻辑

        if(newsVoList!=null&&newsVoList.size()>0){
            PageResult<NewsVo> page = new PageResult<>();
            page.setData(newsVoList);
            page.setPageNumber(Long.valueOf(pageNumber));
            page.setPageSize(Long.valueOf(pageSize));
            page.setTotalSize(Long.valueOf(pageSize));
            res.put("datas", page);
            return res;
        }else{
            res.put("datas", null);
            return res;
        }

    }

    /**
     * 获取卡片配置信息
     * @param request
     * @return
     */
    private Map<String, Object> getCardConfigMap(CardAjaxRequest request){
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = this.toMap(config);
        }
        return configMap;
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


    public static void main(String[] args) {
        String  fwResult = "{\"msg\":\"\",\"result\":\"ok\",\"data\":[{\"BINDID\":\"eed26fe6-4438-4864-a1f7-6843c88a8e4c\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=eed26fe6-4438-4864-a1f7-6843c88a8e4c\"},{\"BINDID\":\"71bad25b-6652-4e71-8214-4035a0f68f38\",\"TITLE\":\"测试数据1208\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=71bad25b-6652-4e71-8214-4035a0f68f38\"},{\"BINDID\":\"6d4f6186-61bd-4db4-86b5-2c5e1c169efa\",\"TITLE\":\"发布人事任免制度\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=6d4f6186-61bd-4db4-86b5-2c5e1c169efa\"},{\"BINDID\":\"5d463e65-ed12-4468-a51a-3de1e86a1761\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-12-08\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=5d463e65-ed12-4468-a51a-3de1e86a1761\"},{\"BINDID\":\"a766f9aa-caf2-4128-b423-d58b1051f52f\",\"TITLE\":\"1111111111\",\"NGRQ\":\"2022-12-07\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=a766f9aa-caf2-4128-b423-d58b1051f52f\"},{\"BINDID\":\"a9042cf4-ecec-4e5c-81ff-844eab76059f\",\"TITLE\":\"1111111\",\"NGRQ\":\"2022-12-01\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=a9042cf4-ecec-4e5c-81ff-844eab76059f\"},{\"BINDID\":\"7eb224c2-5e6c-4055-9ed2-1936a42048c3\",\"TITLE\":\"有关单位全体人员\",\"NGRQ\":\"2022-12-01\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=7eb224c2-5e6c-4055-9ed2-1936a42048c3\"},{\"BINDID\":\"7335a3a1-05f9-4533-a812-fc955ca12d67\",\"TITLE\":\"关于人事任免的xx制度\",\"NGRQ\":\"2022-11-25\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=7335a3a1-05f9-4533-a812-fc955ca12d67\"},{\"BINDID\":\"bf85af18-6c09-4f21-99d8-dbed6951588b\",\"TITLE\":\"\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"党政办公室\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=bf85af18-6c09-4f21-99d8-dbed6951588b\"},{\"BINDID\":\"c657bcfe-d564-455c-b696-5ebe8b2ee09e\",\"TITLE\":\"文件字\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=c657bcfe-d564-455c-b696-5ebe8b2ee09e\"},{\"BINDID\":\"270c26a3-a2aa-4c89-bffa-928b45b019ae\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=270c26a3-a2aa-4c89-bffa-928b45b019ae\"},{\"BINDID\":\"45863df5-0a29-484d-9c57-3cae9b95c3e2\",\"TITLE\":\"关于人事任免类制度的审查\",\"NGRQ\":\"2022-11-24\",\"ZBDW\":\"人事处\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=45863df5-0a29-484d-9c57-3cae9b95c3e2\"},{\"BINDID\":\"bae58b5d-ffd2-408f-84c1-76412102e400\",\"TITLE\":\"测试\",\"NGRQ\":\"2022-11-23\",\"ZBDW\":\"部门1\",\"url\":\"http://oa.cuc.edu.cn/integrate/FwPage.jsp?bindId=bae58b5d-ffd2-408f-84c1-76412102e400\"}],\"id\":\":RO;\"}";

        JSONObject fwResultObj = JSON.parseObject(fwResult);

        String result = fwResultObj.getString("result");

        if(StringUtils.equals(result,"ok")){

            String data = fwResultObj.getString("data");

            List<Map<String,String>> dataList = JSON.parseObject(data,new TypeReference<List<Map<String,String>>>(){});
            for (int i = 0; i < dataList.size(); i++) {

                Map<String,String> tempMap = dataList.get(i);
                String title = tempMap.get("TITLE");
                String author = tempMap.get("ZBDW");

                logger.info("=======title========="+title);

                String detail_url = tempMap.get("url");

                logger.info("=======detail_url========="+detail_url);

                String ngrq = tempMap.get("NGRQ") + " 00:00:00";

                LocalDateTime localDate=LocalDateTime.parse(ngrq, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                logger.info("=======localDate========="+localDate);

                LocalDateTime localDateNow= LocalDateTime.now();

                logger.info("=======localDate========="+localDateNow);
            }




        }
    }
}
