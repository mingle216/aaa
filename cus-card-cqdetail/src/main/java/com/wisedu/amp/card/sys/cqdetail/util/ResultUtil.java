package com.wisedu.amp.card.sys.cqdetail.util;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class ResultUtil {
//    public static int ZERO=0;
//    public static String NINETY_NINE="99+";
//    public static String CHECKED="checked";
//    public static String EMPTY="";
//    public static String INPUTNUM="inputNum";
//    public static String HEIGHT_STR="height";
//    public static String CARDID="cardId";
//    public static String CARDWID="cardWid";
//    public static String YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
//    public static String SOURCELIST="sourceList";
//    public static String TASKINFO="taskInfo";
//    public static String CONFIG="config";
//    public static String PC_APP="PC_APP";
//    public static String QUERY_TODO_TASK="queryTodoTask";
//    public static String TASKDATA="taskData";
//    public static String SOURCEWID="sourceWid";
//    public static String AMP_URL="amp_url";

    /*
     * 卡片高度分类
     * author jdwan
     */
    public enum HeightType{
        //自适应（无最大限制）
        NO_MAXIMUM_LIMIT("heightFlag1", "1"),
        //自适应（有最大限制）
        MAXIMUM_LIMIT("heightFlag2", "2"),
        //固定高度
        FIXED_HEIGHT("heightFlag3", "3");
        private String name;
        private String index;

        HeightType(String name, String index) {
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
     * 单条服务事项展示信息
     */
    public enum SingleItemDisplayType {
        SERVICE_SUBJECT("服务主题", "001"),
        RESPONSIBLE_DEPARTMENT("责任部门", "002"),
        SERVICE_ITEM_ICON("服务事项图标", "003");
        private String name;
        private String index;

        SingleItemDisplayType(String name, String index) {
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
     * 筛选项展示信息
     */
    public enum SearchType {
        SERVICE_SUBJECT("评价", "001"),
        RESPONSIBLE_DEPARTMENT("收藏", "002"),
        SERVICE_OBJECT("分享", "003"),
        SERVICE_TYPE("图标", "004");
        private String name;
        private String index;

        SearchType(String name, String index) {
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
     * String转Map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !"[]".equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }
    /*
     * 卡片高度输入框隐藏标识
     * author jdwan
     */
    public enum HeightInputShowType{
        //自适应（有最大限制）
        MAXIMUM_LIMIT("hideClass1", "2"),
        //固定高度
        FIXED_HEIGHT("hideClass2", "3");
        private String name;
        private String index;

        HeightInputShowType(String name, String index) {
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
}
