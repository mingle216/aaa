package com.wisedu.amp.card.sys.mycount.plugin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.sys.mycount.model.*;
import com.wisedu.amp.card.sys.mycount.util.ConstantUtil.SERVICE_ITEM;
import com.wisedu.amp.card.sys.mycount.util.PageUtils;
import com.wisedu.amp.card.sys.mycount.util.ServiceItemCountUtil;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author kaisir
 */
@MinosSPI
public class ServiceItemCountCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(ServiceItemCountCard.class);

    private final static String DATAIDS = "currentItem,onlineItem,currentDept,allCount,currentCount,doneCount,onlinePercent,allService,itemScore";
    public static final String ADD_INFO = "addInfo";
    public static final String ZH_CN = "zh_CN";

    @Override
    public String getCardId() {
        return "CUS_CARD_MYCOUNT";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        List<AbstractComponent> list = cardConfigReq.getComponentContainer().getComponents();
        String url = "/casp-sim/itemManager/roleList";
        logger.debug("开始调用获取当前事项服务对象,url=" + url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        AmpBaseResponse response = RestTemplateUtils.get(url, headers, AmpBaseResponse.class)
            .getBody();
        logger.debug("当前事项服务对象接口返回结果..." + JSON.toJSONString(response));
        List<RoleModel> roleList = Lists.newArrayList();
        if (null != response && ServiceItemCountUtil.ResponseStatus.SUCCESS.getIndex()
            .equals(response.getErrcode())) {
            Object data = response.getData();
            roleList = JSON.parseArray(JSON.toJSONString(data), RoleModel.class);
        }
        Map<String, List<MultiLangData>> roleInfo = roleList.stream()
            .collect(Collectors.toMap(RoleModel::getRoleWid, RoleModel::getRoleName));
        List<String> roleIds = roleList.stream().map(RoleModel::getRoleWid)
            .collect(Collectors.toList());
        list.forEach(e -> {
            if (e.getComponent().equals(ADD_INFO)) {
                String configInfo = JSONObject.toJSONString(e.getValue());
                List<ServiceItem> serviceItems = Lists.newArrayList();
                if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
                    configInfo = JSON.parse(configInfo).toString();
                }
                if (!StringUtils.isEmpty(configInfo)) {
                    serviceItems = JSONObject.parseArray(configInfo, ServiceItem.class);
                }
                List<ServiceItem> result = serviceItems.stream()
                    .filter(a -> roleIds.contains(a.getDataId()) || DATAIDS.contains(a.getDataId()))
                    .collect(Collectors.toList());
                result.forEach(i -> {
                    if (!DATAIDS.contains(i.getDataId())) {
                        i.setDataName(roleInfo.get(i.getDataId()));
                        i.setOldName(roleInfo.get(i.getDataId()).stream()
                            .filter(o -> o.getLangKey().equals(ZH_CN)).findAny().get()
                            .getLangValue());
                    }
                });
                cardConfigReq.getComponentContainer()
                    .setData("dataItemList", Global.ComponentParam.VALUE, result);
            }
        });
        return cardConfigReq.getComponentContainer();
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getMarkNumber":
                result = 0;
                break;
            case "getItemList":
                result = this.getItemList(request);
                break;
            case "render2":
                result = this.getStatistics(request);
                break;
        }

        return result;
    }

    private CardResponse getStatistics(CardAjaxRequest request) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class)
            .getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        CardConfig cardConfig = new CardConfig();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        if (!StringUtils.isEmpty(configInfo)) {
            cardConfig = JSONObject.parseObject(configInfo, CardConfig.class);
        }
       /* Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");*/
        String url = "/casp-sim/itemManager/queryItemStatistics2";
        logger.debug("开始调用获取事项统计接口,url=" + url);
        ItemStatisticsRequest itemStatisticsRequest = new ItemStatisticsRequest();
        List<String> roleIds = cardConfig.getDataItemList().stream().map(ServiceItem::getDataId)
            .filter(e -> !DATAIDS.contains(e)).collect(Collectors.toList());
        List<ItemStatisticsInfo> list = Lists.newArrayList();
        itemStatisticsRequest.setRoleIds(roleIds);
        HttpEntity entity = new HttpEntity(itemStatisticsRequest);
        AmpBaseResponse response = RestTemplateUtils
            .post(url, entity, AmpBaseResponse.class).getBody();
        logger.debug("获取事项统计接口返回结果..." + JSON.toJSONString(response));

        if (null != response && ServiceItemCountUtil.ResponseStatus.SUCCESS.getIndex()
            .equals(response.getErrcode())) {
            Object data = response.getData();
            list = JSON.parseArray(JSON.toJSONString(data), ItemStatisticsInfo.class);
        }
        Map<String, String> data = list.stream()
            .collect(Collectors.toMap(ItemStatisticsInfo::getDataId, ItemStatisticsInfo::getCount));
        ItemStatisticsInfo onlineItem = list.stream()
            .filter(e -> String.valueOf(SERVICE_ITEM.ONLINE_ITEM.getWid()).equals(e.getDataId()))
            .findAny().orElse(new ItemStatisticsInfo());
        ItemStatisticsInfo item = list.stream()
            .filter(e -> String.valueOf(SERVICE_ITEM.CURRENT_ITEM.getWid()).equals(e.getDataId()))
            .findAny().orElse(new ItemStatisticsInfo());
        Map<String, List<MultiLangData>> roleInfo = list.stream()
            .filter(
                e -> !DATAIDS.contains(e.getDataId()) && !CollectionUtils.isEmpty(e.getDataName()))
            .collect(
                Collectors.toMap(ItemStatisticsInfo::getDataId, ItemStatisticsInfo::getDataName));
        //在线办理覆盖率
        int onlineServiceCount = Integer.parseInt(onlineItem.getCount());
        int allServiceCount = Integer.parseInt(item.getCount());
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后1位
        numberFormat.setMaximumFractionDigits(1);
        String count = "";
        if (allServiceCount != 0) {
            //所占百分比
            count = numberFormat
                .format((float) onlineServiceCount / (float) allServiceCount * 100);
        } else {
            count = "0";
            logger.debug("当前事项为0");
        }
        data.put(SERVICE_ITEM.ONLINE_PERCENT.getWid(), count + "%");
        //--------------------------------------事项统计处理结束---------------------------------------
        ServiceItemCountRequest itemCategoryRequest = new ServiceItemCountRequest();
        String ampUrl = ApplicationContextUtil.get(SystemConfigService.class)
            .getSystemConfigValue(ServiceItemCountUtil.TASKCENTER_URL);
        HttpEntity entity2 = new HttpEntity(itemCategoryRequest);
        String flowCountUrl = "/taskCenter/task/getProcessCount.do";
        logger
            .debug("开始调用获取流程数量接口,url=" + ampUrl + flowCountUrl, "参数=" + JSON.toJSONString(entity2));
        FlowCountResponse response2 = RestTemplateUtils
            .post(ampUrl + flowCountUrl, entity2, FlowCountResponse.class).getBody();
        logger.debug("获取流程数量接口返回结果..." + JSON.toJSONString(response2));
        FlowCountModel handleFlowCountList = handleFlowCountList(response2);
        int allCount =
            handleFlowCountList.getRunningCount() + handleFlowCountList.getCompleteCount();
        data.put(SERVICE_ITEM.ALL_COUNT.getWid(), allCount + "");
        data.put(SERVICE_ITEM.CURRENT_COUNT.getWid(), handleFlowCountList.getRunningCount() + "");
        data.put(SERVICE_ITEM.DONE_COUNT.getWid(), handleFlowCountList.getCompleteCount() + "");
        //--------------------------------------任务中心处理结束---------------------------------------
        List<ItemStatisticsInfo> result = Lists.newArrayList();
        for (ServiceItem i : cardConfig.getDataItemList()) {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(data.get(i.getDataId()))) {
                ItemStatisticsInfo info = new ItemStatisticsInfo();
                info.setDataId(i.getDataId());
                info.setDataName(i.getDataName());
                if (i.getType() == 0) {
                    info.setDataName(roleInfo.get(i.getDataId()));
                }
                info.setCount(data.get(i.getDataId()));
                result.add(info);
            }
        }
        CardResponse res = new CardResponse();
        res.setServiceItemList(result);
        res.setColumns(cardConfig.getSelect());
        return res;
    }

    private PageUtils<Item> getItemList(CardAjaxRequest request) {
        List<Item> list = Lists.newArrayList();
        list.add(new Item(String.valueOf(SERVICE_ITEM.CURRENT_ITEM.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.CURRENT_ITEM.getName())),
            1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.ONLINE_ITEM.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.ONLINE_ITEM.getName())), 1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.CURRENT_DEPT.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.CURRENT_DEPT.getName())),
            1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.ALL_COUNT.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.ALL_COUNT.getName())), 1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.CURRENT_COUNT.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.CURRENT_COUNT.getName())),
            1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.DONE_COUNT.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.DONE_COUNT.getName())), 1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.ONLINE_PERCENT.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.ONLINE_PERCENT.getName())),
            1));
        list.add(new Item(String.valueOf(SERVICE_ITEM.ITEM_SCORE.getWid()),
            Lists.newArrayList(new MultiLangData(Global.DEFAULT_LANGUAGE, SERVICE_ITEM.ITEM_SCORE.getName())), 1));
        String url = "/casp-sim/itemManager/roleList";
        logger.debug("开始调用获取当前事项服务对象,url=" + url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        AmpBaseResponse response = RestTemplateUtils.get(url, headers, AmpBaseResponse.class)
            .getBody();
        logger.debug("当前事项服务对象接口返回结果..." + JSON.toJSONString(response));
        List<RoleModel> roleList = Lists.newArrayList();
        if (null != response && ServiceItemCountUtil.ResponseStatus.SUCCESS.getIndex()
            .equals(response.getErrcode())) {
            Object data = response.getData();
            roleList = JSON.parseArray(JSON.toJSONString(data), RoleModel.class);
        }
        final Item[] item = new Item[1];
        List<Item> itemList = roleList.stream().map(e -> {
            item[0] = new Item(e.getRoleWid(), e.getRoleName(), 0);
            return item[0];
        }).collect(Collectors.toList());
        list.addAll(itemList);
        PageUtils<Item> page = null;
        try {
            int pageSize = Integer.parseInt(request.getParam().get("pageSize"));
            int pageNum = Integer.parseInt(request.getParam().get("pageNum"));
            String searchKey = request.getParam().get("searchKey");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(searchKey)) {
                list = list.stream().filter(
                    e -> e.getDataName().get(0).getLangValue().toUpperCase()
                        .contains(searchKey.toUpperCase().trim()))
                    .collect(Collectors.toList());
            }
            page = new PageUtils<>(list, pageNum, pageSize);
        } catch (Exception e) {
            logger.debug("获取事项服务对象接口参数异常:", e);
        }
        return page;
    }


    /**
     * 调用接口查询对应事项的数量
     *
     * @param list
     */
    private void setServiceCount(List<ItemList> list) {
        ServiceItemCountRequest itemCategoryRequest = new ServiceItemCountRequest();
        //String serviceCountUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ServiceItemCountUtil.RequestUrl.SERVICE_COUNT_URL.getIndex());
        HttpEntity entity = new HttpEntity(itemCategoryRequest);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String ampUrl = "/casp-sim";
        String serviceCountUrl = "/itemManager/queryItemStatistics";
        logger.debug("开始调用获取当前事项和可在线办理事项接口,url=" + ampUrl + serviceCountUrl,
            "参数=" + JSON.toJSONString(entity));
        AmpBaseResponse response = RestTemplateUtils
            .get(ampUrl + serviceCountUrl, headers, AmpBaseResponse.class).getBody();
        logger.debug("当前事项和可在线办理事项接口返回结果..." + JSON.toJSONString(response));
        ServiceCountModel serviceCountModel = handleServiceCountList(response);

        for (ItemList itemList : list) {
            if (ServiceItemCountUtil.CURRENT_ITEM.equals(itemList.getCustomerName())) {
                itemList.setCount(serviceCountModel.getItemCount() + "");
            } else if (ServiceItemCountUtil.ONLINE_ITEM.equals(itemList.getCustomerName())) {
                itemList.setCount(serviceCountModel.getOnlineItemCount() + "");
            } else if (ServiceItemCountUtil.ITEM_SCORE.equals(itemList.getCustomerName())) {
                itemList.setCount(serviceCountModel.getAppraiseMark() + "");
            } else if (ServiceItemCountUtil.CURRENT_DEPT.equals(itemList.getCustomerName())) {
                itemList.setCount(serviceCountModel.getDeptCount() + "");
            } else if (ServiceItemCountUtil.ONLINE_PERCENT
                .equals(itemList.getCustomerName())) {//在线办理覆盖率
                int onlineServiceCount = serviceCountModel.getOnlineItemCount();
                int allServiceCount = serviceCountModel.getItemCount();
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后1位
                numberFormat.setMaximumFractionDigits(1);
                String result = "";
                if (allServiceCount != 0) {
                    result = numberFormat
                        .format((float) onlineServiceCount / (float) allServiceCount * 100);//所占百分比
                } else {
                    result = "0";
                    logger.debug("当前事项为0");
                }
                itemList.setCount(result + "%");
            }
        }
    }

    /**
     * 将接口返回的的数量赋值给前端
     *
     * @param response
     */
    private ServiceCountModel handleServiceCountList(AmpBaseResponse response) {
        ServiceCountModel serviceCountModel = new ServiceCountModel();
        if (ServiceItemCountUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
            Object data = response.getData();
            serviceCountModel = JSON.parseObject(JSON.toJSONString(data), ServiceCountModel.class);
        }
        return serviceCountModel;
    }

    /**
     * 调用接口查询对应流程的数量
     *
     * @param list
     */
    private void setFlowCount(List<ItemList> list) {
        ServiceItemCountRequest itemCategoryRequest = new ServiceItemCountRequest();
        String ampUrl = ApplicationContextUtil.get(SystemConfigService.class)
            .getSystemConfigValue(ServiceItemCountUtil.TASKCENTER_URL);
        // String flowCountUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ServiceItemCountUtil.RequestUrl.FLOW_COUNT_URL.getIndex());
        HttpEntity entity = new HttpEntity(itemCategoryRequest);
        String flowCountUrl = "/taskCenter/task/getProcessCount.do";
        logger
            .debug("开始调用获取流程数量接口,url=" + ampUrl + flowCountUrl, "参数=" + JSON.toJSONString(entity));
        FlowCountResponse response = RestTemplateUtils
            .post(ampUrl + flowCountUrl, entity, FlowCountResponse.class).getBody();
        logger.debug("获取流程数量接口返回结果..." + JSON.toJSONString(response));
        FlowCountModel handleFlowCountList = handleFlowCountList(response);

        for (ItemList itemList : list) {
            if (ServiceItemCountUtil.ALL_COUNT.equals(itemList.getName())) {
                int allCount =
                    handleFlowCountList.getRunningCount() + handleFlowCountList.getCompleteCount();
                itemList.setCount(allCount + "");
            } else if (ServiceItemCountUtil.CURRENT_COUNT.equals(itemList.getName())) {
                itemList.setCount(handleFlowCountList.getRunningCount() + "");
            } else if (ServiceItemCountUtil.DONE_COUNT.equals(itemList.getName())) {
                itemList.setCount(handleFlowCountList.getCompleteCount() + "");
            }
        }
    }

    /**
     * 将接口返回的的流程数量赋值给前端
     *
     * @param response
     */
    private FlowCountModel handleFlowCountList(FlowCountResponse response) {
        FlowCountModel flowCountModel = new FlowCountModel();
        if (ServiceItemCountUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getCode())) {
            Object data = response.getDatas();
            flowCountModel = JSON.parseObject(JSON.toJSONString(data), FlowCountModel.class);
        }
        return flowCountModel;
    }

    /*private String config(CardAjaxRequest request) {
        logger.debug("load serviceItemCount config...");

        Template t = groupTemplate.getTemplate("/" + this.getCardId() + "/config.html");

        //获取amp接口url
        String ampUrl = ApplicationContextUtil.get(SystemConfigService.class)
            .getSystemConfigValue(ServiceItemCountUtil.TASKCENTER_URL);

        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo;
        if (null != request.getParam() && null != request.getParam().get("config")) {
            configInfo = request.getParam().get("config");
        } else {
            configInfo = ApplicationContextUtil.get(CardUtil.class)
                .getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }

        ServiceItemCountConfig config = JSONObject
            .parseObject(configInfo, ServiceItemCountConfig.class);
        if (null == config) {
            config = new ServiceItemCountConfig(new ArrayList<>());
        }

        List<ItemList> itemList = config.getItemList();
        //排序 启用的放在上面
        Collections.sort(itemList, new Comparator<ItemList>() {
            @Override
            public int compare(ItemList o1, ItemList o2) {
                boolean onLine1 = o1.isUse();
                boolean onLine2 = o2.isUse();
                //两个值进行位运算,值不同为1,为true,参与运算
                //值相同为0,为false,不参与运算
                if (onLine1 ^ onLine2) {
                    return onLine1 ? -1 : 1;
                } else {

                    return 0;
                }
            }
        });
        logger.debug("卡片配置信息:" + config.toString());

        t.binding("cardId", getCardId());
        t.binding("cardWid", request.getCardWid());
        t.binding("configInfo", config);
        String str = t.render();
        return str;
    }*/
}
