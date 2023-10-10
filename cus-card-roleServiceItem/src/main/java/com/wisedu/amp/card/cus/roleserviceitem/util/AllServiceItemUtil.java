package com.wisedu.amp.card.cus.roleserviceitem.util;

import com.alibaba.fastjson.JSON;
import com.wisedu.amp.card.cus.roleserviceitem.model.NavModel;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数枚举+工具类
 *
 * @author dali(01317002)
 * @date 2020年9月8日
 */
public class AllServiceItemUtil {

    /**
     * 26个英文字母
     */
    public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String BRACKETS = "[]";


    /**
     * 获取某个字母附近的四个字母【Y-Z为两个】
     *
     * @param str
     * @return
     */
    public static String nearly(String str) {
        int position = LETTERS.indexOf(str);
        int index = (int) Math.ceil((position + 4) / 4);// 0>1 1>1.x 2>1.x
        if (index < 7) {
            return LETTERS.substring((index - 1) * 4, index * 4);//0 4 4 8
        } else {
            return "YZ";
        }
    }


    /**
     * 获取字母导航条
     *
     * @return
     */
    public static List<NavModel> navItem() {
        List<NavModel> list = new ArrayList<>();
        NavModel map;

        for (int j = 0; j < LETTERS.length(); j++) {
            String str = LETTERS.substring(j, j + 1);
            map = new NavModel();
            map.setId(str);
            map.setName(str);
            map.setHref(str);
            map.setHasValue(false);
            list.add(map);
        }
        //其他归类为 #
        map = new NavModel();
        map.setId("other");
        map.setName("#");
        map.setHref("other");
        map.setHasValue(false);
        list.add(map);

        return list;
    }


    /**
     * String转List
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> toListMap(String json) {
        List<Object> list = JSON.parseArray(json);
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        for (Object object : list) {
            Map<String, Object> ret = (Map<String, Object>) object;
            lists.add(ret);
        }
        return lists;
    }

    /**
     * String转Map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !BRACKETS.equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }

    /**
     * 登录前
     */
    public enum BeforeLogin {
        SHOW_ALL("显示全部", "1"),
        SHOW_GUEST("只显示游客应用", "0");
        private String name;
        private String index;

        BeforeLogin(String name, String index) {
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
     * 登录后
     */
    public enum AfterLogin {
        SHOW_ALL("显示全部", "1"),
        ABOUT_ME("只显示与我相关", "0");
        private String name;
        private String index;

        AfterLogin(String name, String index) {
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
     * 服务事项搜索框是否显示
     */
    public enum DisplayType {
        SHOW("显示", "1"),
        HIDE("隐藏", "0");
        private String name;
        private String index;

        DisplayType(String name, String index) {
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
     * 服务组织排序规则
     */
    public enum SortType {
        ALPHABET("按首字母排序", "1"),
        VIEWS("按访问量排序", "2");
        private String name;
        private String index;

        SortType(String name, String index) {
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
        SERVICE_SUBJECT("服务主题", "1"),
        RESPONSIBLE_DEPARTMENT("责任部门", "2"),
        SERVICE_OBJECT("服务对象", "3"),
        SERVICE_DEPARTMENT("服务部门", "4");
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
}
