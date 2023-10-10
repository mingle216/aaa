package com.wisedu.amp.card.cus.serviceItemCategoryDetail.util;

import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.TimeUnit;

public class ServiveItemCategoryUtil {

    //服务事项分类信息
    public static String ITEM_INFO ="serviceItemInfo";
    //tab选项
    public static String ROLE_WID = "roleWid";
    //一级分类
    public static String CATEGORY_WID = "categoryWid";
    //接口地址
    public static String AMP_URL="amp_url";
    //卡片ID
    public static String CARD_ID="cardId";
    //卡片运行时ID
    public static String CARD_WID="cardWid";
    //tab选项
    public static String TAB = "tabInfo";
    //展示行数
    public static  String ROWS = "rows";
    //一行展示数量
    public static String COLUMNS = "columns";
    //分类配置
    public static String CATEGORYITEM = "categoryItemInfo";
    //是否相关 isRelate
    public static String  IS_RELATE = "isRelate";
    //游客wid
    public static String GUEST = "guest";
    //默认按部门分类
    public static String DEFAULT_CATEGORY = "按部门分类";
    //内容
    public static String CONTENT = "content";
    /**
     * 角色展示方式
     */
    public enum SHOW_ROLE_RULES{
        TAB("tab","1"),
        TILE("平铺","2");

        private String name;
        private String index;

        SHOW_ROLE_RULES(String name, String index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

    /**
     * Tab展示选项类型
     */
    public enum SOURCE_ITEM_TYPE{
            STUDENT("学生办事","student"),
            TEACHER("教师办事","teacher"),
            GUEST("游客办事","guest");

        private String name;
        private String index;

        SOURCE_ITEM_TYPE(String name, String index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

    /*
     * 请求状态
     * author jdwan
     */
    public enum ResponseStatus{
        SUCCESS("请求成功", "0"),
        FAIL("请求失败", "99");
        private String name;
        private String index;

        ResponseStatus(String name, String index) {
            this.name = name;
            this.index = index;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

    /*
     * 接口请求地址
     * author jdwan
     */
    public enum RequestUrl{
        ROLE_LIST_URL("所有服务对象接口地址", "seriveitemcategory_role_list_interface"),
        CATEGORY_LIST_URL(" 一级分类维度接口地址", "seriveitemcategory_category_list_interface"),
        ALL_SERVICE_URL(" 所有服务分类及一二级主题接口地址", "seriveitemcategory_all_service_list_interface"),
        ALL_SUBJECT_URL(" 按服务对象服务分类获取服务主题接口地址", "getSubjectByRoleCateGoryList_interface");
        private String name;
        private String index;

        RequestUrl(String name, String index) {
            this.name = name;
            this.index = index;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

    public static Object getRedisData(CardAjaxRequest cardAjaxRequest){
        String redisKey =
                cardAjaxRequest.getCardId() + ":" + cardAjaxRequest.getCardWid() + ":" + RequestContextHolder.getRequestAttributes().getSessionId()+":"+ cardAjaxRequest.getMethod();
        return ApplicationContextUtil.get(RedisUtil.class).get(redisKey);
    }

    public static void setRedisData(CardAjaxRequest cardAjaxRequest,Object data){
        String redisKey =
                cardAjaxRequest.getCardId() + ":" + cardAjaxRequest.getCardWid() + ":" + RequestContextHolder.getRequestAttributes().getSessionId()+":"+ cardAjaxRequest.getMethod();
        ApplicationContextUtil.get(RedisUtil.class).set(redisKey,data,10, TimeUnit.SECONDS);
    }
}
