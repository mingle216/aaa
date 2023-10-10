package com.wisedu.minos.casp.portal.utils;

public class ResultUtil {

    public static final int ZERO = 0;

    public static final String TASKCENTER_URL = "taskcenter_url";

    public static final String PC_APP = "PC_APP";

    public enum RequestUrl {
        SOURCE_LIST_URL("任务分类列表接口地址", "taskcenter_source_list_interface"),
        TODO_TASK_LIST_URL("待办任务列表接口地址", "taskcenter_task_list_interface"),
        TODO_TASK_COUNT_URL("待办任务数量接口地址", "taskcenter_task_count_interface");
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

    /*
     * 请求状态
     * author jdwan
     */
    public enum ResponseStatus {
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
}
