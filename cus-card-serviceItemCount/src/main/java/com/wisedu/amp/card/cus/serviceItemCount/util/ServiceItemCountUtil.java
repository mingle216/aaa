package com.wisedu.amp.card.cus.serviceItemCount.util;


public class ServiceItemCountUtil {

    //接口地址
    public static String TASKCENTER_URL="taskcenter_url";
    //当前进驻事项
    public static String CURRENT_ITEM = "进驻事项数量";

    //可在线办理事项
    public static String ONLINE_ITEM = "在线办理事项";

    //进驻服务部门
    public static String CURRENT_DEPT = "进驻服务部门";

    //学生事项统计
    public static String STUDENT_ITEM = "学生事项统计";

    //教师事项统计
    public static String TEACHER_ITEM = "教师事项统计";

    //在线办理覆盖率
    public static String ONLINE_PERCENT = "在线办理覆盖率";

    //总办件数量
    public static String ALL_COUNT = "总办件数";

    //当前正在办件
    public static String CURRENT_COUNT = "正在办理";

    //已完成办件
    public static String DONE_COUNT = "办理完成";

    //累计服务师生
    public static String ALL_SERVICE = "累计服务师生";

    //事项平均分
    public static String ITEM_SCORE = "事项评价平均分";
    //事项统计
    public static String STATISTICS = "事项统计";
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
        SERVICE_COUNT_URL("所有服务对象接口地址", "seriveitem_count_interface"),
        FLOW_COUNT_URL(" 一级分类维度接口地址", "flow_count_interface");
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
}
