package com.wisedu.amp.card.cus.recommendapp.model;

public class ResultUtil {

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
