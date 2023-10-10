package com.wisedu.amp.card.sys.searchResults.util;

import com.alibaba.fastjson.JSON;
import com.wisedu.amp.card.sys.searchResults.model.faq.Faq;
import com.wisedu.amp.card.sys.searchResults.model.faq.FaqRequest;
import com.wisedu.amp.card.sys.searchResults.model.faq.FaqResponse;
import com.wisedu.amp.card.sys.searchResults.plugin.SearchResultsCard;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.model.AllServiceRequest;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.ServiceItemApiService;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.casp.portal.model.AllServiceResponse.ServiceModel;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;

import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.wisedu.minos.casp.portal.model.AllServiceItemRequest;
import com.wisedu.minos.casp.portal.model.AllServiceItemResponse.ServiceItemModel;


public class SortingUtil {
    private final static Logger logger = LogManager.getLogger(SearchResultsCard.class);

    /**
     * 26个英文字母
     */
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 10个数字
     */
    private static final String NUMBERS = "0123456789";

    /**
     * 默认最大的数量
     */
    private static final Integer MAXSIZE = 9999;

    /**
     * 搜索服务事项
     *
     * @param keyword
     * @return
     */
    public static List<ServiceItemModel> searchServiceItemList(String keyword, String lang) {
        if (StringUtil.isEmpty(keyword)) {
            return new ArrayList<>();
        } else {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            String userId = null != user ? user.getWid() : "";
            String userAccount = null != user ? user.getUserAccount() : "";
            AllServiceItemRequest allserviceItemRequest = new AllServiceItemRequest();
            allserviceItemRequest.setSearchKey(keyword);
            allserviceItemRequest.setIsRelate(0);
            if(StringUtils.isEmpty(userId) && StringUtils.isNotEmpty(lang)){
                allserviceItemRequest.setLang(lang);
            }else{
                allserviceItemRequest.setUserId(userId);
                allserviceItemRequest.setUserAccount(userAccount);
            }

            List<ServiceItemModel> list = ApplicationContextUtil.get(ServiceItemApiService.class).getAllServiceItem(allserviceItemRequest);

//            List<ServiceItem> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list)) {
                List<String> grantedServiceList = ApplicationContextUtil.get(ServiceApiService.class).getGrantedServiceList();
                for(ServiceItemModel serviceItem : list){
                    serviceItem.setOnlineServiceType(ApplicationContextUtil.get(ServiceApiService.class).getOnlineServiceType(grantedServiceList, serviceItem.getServiceList()));
                }
            }

            return sortByInitialism(list);
        }
    }


    //服务事项 按首字母排序
    //排序规则  按照首字母正序(中文在前，英文在后)-数字正序-特殊字符规则排列
    private static List<ServiceItemModel> sortByInitialism(List<ServiceItemModel> list){
        // 1.判断首字母是否包含数据
        for (ServiceItemModel serviceItem : list) {
            //服务事项名称
            String itemName = serviceItem.getItemName();
            //首字母[多音字不准确]
            String headChar = ChinesePinyinUtil.headChar(itemName);
            serviceItem.setHeadChar(headChar);
            if (!itemName.substring(0, 1).toUpperCase().equals(headChar)) {
                //首字母 中文
                serviceItem.setOrder("0");
            } else if (LETTERS.contains(headChar)) {
                // 首字母 字母
                serviceItem.setOrder("1");
            } else if (NUMBERS.contains(headChar)) {
                // 首字母 数字
                // 创建 Pattern 对象
                String pattern = "[0-9]+";
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(itemName);
                serviceItem.setHeadChar(m.find() ? m.group(0) : "");
                serviceItem.setOrder("2");
            } else {
                // 首字母 特殊字符
                serviceItem.setOrder("3");
            }

        }

        List<ServiceItemModel> chineseAndEnglishList = list.stream().filter(serviceItem -> "0".equals(serviceItem.getOrder()) || "1".equals(serviceItem.getOrder())).collect(Collectors.toList());
        List<ServiceItemModel> numberList = list.stream().filter(serviceItem -> "2".equals(serviceItem.getOrder())).collect(Collectors.toList());
        List<ServiceItemModel> specialList = list.stream().filter(serviceItem -> "3".equals(serviceItem.getOrder())).collect(Collectors.toList());

        compareHeadCNANDEN(chineseAndEnglishList);
        compareHeadNumber(numberList);
        compareHeadChar(specialList);

        List<ServiceItemModel> sortList = new ArrayList<>();
        sortList.addAll(chineseAndEnglishList);
        sortList.addAll(numberList);
        sortList.addAll(specialList);

        return sortList;
    }

    //中英排序，中文在前  英文在后
    private  static  void compareHeadCNANDEN(List<ServiceItemModel> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((e1, e2) -> {
            int result = comparator.compare(StringUtil.getString(e1.getHeadChar()), StringUtil.getString(e2.getHeadChar()));
            if (result == 0) {
                //比较排序值
                return comparator.compare(StringUtil.getString(e1.getOrder()), StringUtil.getString(e2.getOrder()));
            }
            return result;
        });
    }

    //按首字母比较
    private static void compareHeadChar(List<ServiceItemModel> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((o1, o2) -> comparator.compare(o1.getHeadChar(), o2.getHeadChar()));
    }

    //数字比较
    private static void compareHeadNumber(List<ServiceItemModel> list){
        list.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getHeadChar())));
    }

    /**
     * 搜索服务/应用
     *
     * @param keyword
     * @return
     */
    public static List<ServiceModel> searchAppList(String keyword, String lang) {
        if (StringUtil.isEmpty(keyword)) {
            return new ArrayList<>();
        } else {
            String userId = getCurrentUserId();
            List<ServiceModel> list = getAllServiceList(userId, keyword, false, lang);
            return sortByInitialismForService(list);
        }

    }

    //服务 按首字母排序
    //排序规则  按照首字母正序(中文在前，英文在后)-数字正序-特殊字符规则排列
    private static List<ServiceModel> sortByInitialismForService(List<ServiceModel> list){
        // 1.判断首字母是否包含数据
        for (ServiceModel serviceInfo : list) {
            //服务事项名称
            String serviceName = serviceInfo.getServiceName();
            //首字母[多音字不准确]
            String headChar = ChinesePinyinUtil.headChar(serviceName);
            serviceInfo.setHeadChar(headChar);
            if (!serviceName.substring(0, 1).toUpperCase().equals(headChar)) {
                //首字母 中文
                serviceInfo.setOrder("0");
            } else if (LETTERS.contains(headChar)) {
                // 首字母 字母
                serviceInfo.setOrder("1");
            } else if (NUMBERS.contains(headChar)) {
                // 首字母 数字
                // 创建 Pattern 对象
                String pattern = "[0-9]+";
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(serviceName);
                serviceInfo.setHeadChar(m.find() ? m.group(0) : "");
                serviceInfo.setOrder("2");
            } else {
                // 首字母 特殊字符
                serviceInfo.setOrder("3");
            }

        }

        List<ServiceModel> chineseAndEnglishList = list.stream().filter(serviceInfo -> "0".equals(serviceInfo.getOrder()) || "1".equals(serviceInfo.getOrder())).collect(Collectors.toList());
        List<ServiceModel> numberList = list.stream().filter(serviceInfo -> "2".equals(serviceInfo.getOrder())).collect(Collectors.toList());
        List<ServiceModel> specialList = list.stream().filter(serviceInfo -> "3".equals(serviceInfo.getOrder())).collect(Collectors.toList());

        compareHeadCNANDENWithService(chineseAndEnglishList);
        compareHeadNumberWithService(numberList);
        compareHeadCharWithService(specialList);

        List<ServiceModel> sortList = new ArrayList<>();
        sortList.addAll(chineseAndEnglishList);
        sortList.addAll(numberList);
        sortList.addAll(specialList);

        return sortList;
    }

    //中英排序，中文在前  英文在后
    private  static  void compareHeadCNANDENWithService(List<ServiceModel> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((e1, e2) -> {
            int result = comparator.compare(StringUtil.getString(e1.getHeadChar()), StringUtil.getString(e2.getHeadChar()));
            if (result == 0) {
                //比较排序值
                return comparator.compare(StringUtil.getString(e1.getOrder()), StringUtil.getString(e2.getOrder()));
            }
            return result;
        });
    }

    //按首字母比较
    private static void compareHeadCharWithService(List<ServiceModel> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((o1, o2) -> comparator.compare(o1.getHeadChar(), o2.getHeadChar()));
    }

    //数字比较
    private static void compareHeadNumberWithService(List<ServiceModel> list){
        list.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getHeadChar())));
    }

    public static List<Faq> searchQuestionList(String keyword) {
        return searchQuestionList(keyword, "");
    }
    /**
     * 搜索问题列表
     *
     * @param keyword
     * @return
     */
    public static List<Faq> searchQuestionList(String keyword, String lang) {
        String url = "/coosk/serviceItem/faq/query";
        if (StringUtil.isEmpty(keyword)) {
            return new ArrayList<>();
        } else {
            UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            FaqRequest faqRequest = new FaqRequest();
            faqRequest.setKeyword(keyword);
            faqRequest.setPageSize(MAXSIZE);
            faqRequest.setPageNumber(1);
            if(null!=currentUser){
                faqRequest.setUserWid(currentUser.getWid());
            }else{
                faqRequest.setLang(lang);
            }

            HttpEntity entity = new HttpEntity(faqRequest);
            FaqResponse faqResponse = null;
            try {
                faqResponse = RestTemplateUtils.post(url, entity, FaqResponse.class).getBody();
            } catch (Exception e) {
                logger.error("调用/coosk/serviceItem/faq/query接口失败", e);
            }
            List<Faq> list = new ArrayList<>();
            if( null != faqResponse && null != faqResponse.getDatas()){
                list = faqResponse.getDatas();
                //常见问题 按首字母正序排序
                list= sortByInitialismForQuestion(list);
            }

            return list;
        }
    }

    //常见问题 按首字母排序
    private static List<Faq> sortByInitialismForQuestion (List<Faq> list){
        // 1.判断首字母是否包含数据
        for ( Faq faq : list ){
            //服务事项名称
            String question = faq.getQuestion();
            //首字母[多音字不准确]
            String headChar = ChinesePinyinUtil.headChar(question);
            faq.setHeadChar(headChar);
            if (!question.substring(0, 1).toUpperCase().equals(headChar)) {
                //首字母 中文
                faq.setOrder("0");
            } else if (LETTERS.contains(headChar)) {
                // 首字母 字母
                faq.setOrder("1");
            } else if (NUMBERS.contains(headChar)) {
                // 首字母 数字
                // 创建 Pattern 对象
                String pattern = "[0-9]+";
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(question);
                faq.setHeadChar(m.find() ? m.group(0) : "");
                faq.setOrder("2");
            } else {
                // 首字母 特殊字符
                faq.setOrder("3");
            }
        }

        List<Faq> chineseAndEnglishList = list.stream().filter(faq -> "0".equals(faq.getOrder()) || "1".equals(faq.getOrder())).collect(Collectors.toList());
        List<Faq> numberList = list.stream().filter(faq -> "2".equals(faq.getOrder())).collect(Collectors.toList());
        List<Faq> specialList = list.stream().filter(faq -> "3".equals(faq.getOrder())).collect(Collectors.toList());

        compareHeadCNANDENWithFq(chineseAndEnglishList);
        compareHeadNumberWithFq(numberList);
        compareHeadCharWithFq(specialList);

        List<Faq> sortList = new ArrayList<>();
        sortList.addAll(chineseAndEnglishList);
        sortList.addAll(numberList);
        sortList.addAll(specialList);

        return sortList;
    }

    //中英排序，中文在前  英文在后
    private  static  void compareHeadCNANDENWithFq(List<Faq> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((e1, e2) -> {
            int result = comparator.compare(StringUtil.getString(e1.getHeadChar()), StringUtil.getString(e2.getHeadChar()));
            if (result == 0) {
                //比较排序值
                return comparator.compare(StringUtil.getString(e1.getOrder()), StringUtil.getString(e2.getOrder()));
            }
            return result;
        });
    }

    //按首字母比较
    private static void compareHeadCharWithFq(List<Faq> list){
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((o1, o2) -> comparator.compare(o1.getHeadChar(), o2.getHeadChar()));
    }

    //数字比较
    private static void compareHeadNumberWithFq(List<Faq> list){
        list.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getHeadChar())));
    }


    /**
     * 获取当前用户ID
     *
     * @return
     */
    private static String getCurrentUserId() {
        String userId;
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            userId = null != user ? user.getWid() : "";
        } catch (Exception e) {
            logger.error("获取用户信息异常" , e);
            userId = "";
        }
        return userId;
    }

    private static List<ServiceModel> getAllServiceList(String userId, String keyword, boolean console, String lang){
        AllServiceRequest allServiceRequest = new AllServiceRequest();
        if(StringUtils.isEmpty(userId) && StringUtils.isNotEmpty(lang)){
            allServiceRequest.setLang(lang);
        }else {
            allServiceRequest.setUserId(userId);
        }
        allServiceRequest.setSearchKey(keyword);
        allServiceRequest.setPageNumber(1);
        allServiceRequest.setPageSize(MAXSIZE);
        allServiceRequest.setConsole(console);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                allServiceRequest.setServiceStation("1");
            }
        } else{
            allServiceRequest.setServiceStation("0");
        }
        return ApplicationContextUtil.get(ServiceApiService.class).getAllService(allServiceRequest);
    }

}
