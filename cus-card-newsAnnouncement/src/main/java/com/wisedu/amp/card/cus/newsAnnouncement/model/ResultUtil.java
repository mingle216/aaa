package com.wisedu.amp.card.cus.newsAnnouncement.model;

public class ResultUtil {
    public static int NUM=6;
    public static int HEIGHT=466;
    public static int ZERO=0;
    /*
     *卡片高度分类
     * author jdwan
     */
    public static interface HEIGHT_TYPE{
        String ZSYWZDXZ="1";
        String ZSYYZDXZ="2";
        String GDGD="3";
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
    /**
     * 展示方式分类
     */
    public static interface SHOW_TYPE{
        String BTSJ="1";
        String BTSJZY="2";
    }
}
