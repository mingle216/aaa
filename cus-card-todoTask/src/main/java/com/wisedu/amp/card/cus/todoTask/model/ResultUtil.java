package com.wisedu.amp.card.cus.todoTask.model;

public class ResultUtil {
    public static final int ZERO = 0;
    public static final String NINETY_NINE = "99+";
    public static final String CHECKED = "checked";
    public static final String EMPTY = "";
    public static final String INPUTNUM = "inputNum";
    public static final String HEIGHT_STR = "height";
    public static final String CARDID = "cardId";
    public static final String CARDWID = "cardWid";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String SOURCELIST = "sourceList";
    public static final String TASKINFO = "taskInfo";
    public static final String CONFIG = "config";
    public static final String PC_APP = "PC_APP";
    public static final String QUERY_TODO_TASK = "queryTodoTask";
    public static final String TASKDATA = "taskData";
    public static final String SOURCEWID = "sourceWid";
    public static final String TASKCENTER_URL = "taskcenter_url";
    /*
     * 任务来源列表是否展示项字典
     * author jdwan
     */
    public enum RadioFlag {
        YES("显示", "1"),
        NO("不显示", "0");
        private String name;
        private String index;

        RadioFlag(String name, String index) {
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
     * 任务配置项字典
     * author jdwan
     */
    public enum CheckedFlag {
        //紧急程度
        DEGREE_OF_URGENCY("configure0", "0"),
        //任务来源
        TASK_SOURCE("configure1", "1"),
        //发起人
        SPONSOR("configure2", "2"),
        //发起部门
        INITIATING_DEPARTMENT("configure3", "3"),
        //流程图
        FLOW_CHART("configure4", "4"),
        //任务接收时间
        TASK_RECEIVING_TIME("configure5", "5");
        private String name;
        private String index;

        CheckedFlag(String name, String index) {
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
     * 任务查询分类
     * author jdwan
     */
    public enum TaskType {
        TODO("待办任务", "1"),
        DONE("已办任务", "2"),
        MYTASK("我发起的", "3");
        private String name;
        private String index;

        TaskType(String name, String index) {
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
     * 卡片高度分类
     * author jdwan
     */
    public enum HeightType {
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

    /*
     * 接口请求地址
     * author jdwan
     */
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
     * 时间展示
     * author jdwan
     */
    public enum DateShowType {
        //刚刚
        EXACTLY("刚刚", "1"),
        //几分钟前
        MINUTES_AGO("分钟前", "2"),
        //几小时前
        HOURS_AGO("小时前", "3"),
        //昨天
        YESTERDAY("昨天", "4");
        private String name;
        private String index;

        DateShowType(String name, String index) {
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
     * 任务来源单选框选中标识
     * author jdwan
     */
    public enum SourceRadioType {
        //显示
        SHOW("source1", "1"),
        //不显示
        NOT_SHOW("source0", "0");
        private String name;
        private String index;

        SourceRadioType(String name, String index) {
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
     * 卡片高度输入框隐藏标识
     * author jdwan
     */
    public enum HeightInputShowType {
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
