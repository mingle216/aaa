package com.wisedu.amp.card.sys.searchResults.util;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author SlicLi
 * @date 2020/9/28
 */
public class SearchResultsUtil {

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

    /**
     * 页面级展示信息
     */
    public enum PageShowInfo {
        NAV("锚点导航", "001")
        //, BAISHITONG("百事通智能搜索数据", "002")
        ;
        private String name;
        private String index;

        PageShowInfo(String name, String index) {
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
     * 搜索无结果
     */
    public enum NoSearchResult {
        HIDE("隐藏搜索分项", "0"),
        SHOW("显示搜索分项", "1");
        private String name;
        private String index;

        NoSearchResult(String name, String index) {
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
     * 搜索结果展示信息
     */
    public enum SearchResultsItem {
        ONLINE_SERVICE("在线服务", "001"),
        SERVICE_ITEM("服务事项", "002"),
        RESPONBILITY("责任清单", "003"),
        QUESTION("常见问题", "004");
        private String name;
        private String index;

        SearchResultsItem(String name, String index) {
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
     * 搜索结果展示信息
     */
    public enum SearchResultsItem2 {
        ONLINE_SERVICE("在线服务", "onlineService"),
        SERVICE_ITEM("服务事项", "serviceItem"),
        RESPONBILITY("责任清单", "003"),
        QUESTION("常见问题", "FAQ");
        private String name;
        private String index;

        SearchResultsItem2(String name, String index) {
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
