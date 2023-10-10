package com.wisedu.amp.card.cus.serviceItemCount.util;

public class ConstantUtil {

    public enum SERVICE_ITEM{
        CURRENT_ITEM("当前进驻事项",1,true,"0","当前进驻事项","currentItem"),
        ONLINE_ITEM("可在线办理事项",2,true,"0","可在线办理事项","onlineItem"),
        CURRENT_DEPT("进驻服务部门",3,true,"0","进驻服务部门","currentDept"),
        ALL_COUNT("总办件数量",4,true,"0","总办件数量","allCount"),
        CURRENT_COUNT("当前正在办件",5,false,"0","当前正在办件","currentCount"),
        DONE_COUNT("已完成办件",6,true,"0","已完成办件","doneCount"),
        //STUDENT_ITEM("学生事项统计",7,false,"0","学生事项统计"),
        //TEACHER_ITEM("教师事项统计",8,false,"0","教师事项统计"),
        ONLINE_PERCENT("在线办理覆盖率",9,false,"0","在线办理覆盖率","onlinePercent"),
        ALL_SERVICE("累计服务师生",10,false,"0","累计服务师生","allService"),
        ITEM_SCORE("事项平均分",11,false,"0","事项平均分","itemScore");

        private String name;
        private int index;
        private boolean isUse;
        private String count;
        private String customerName;
        private String wid;

        SERVICE_ITEM(String name, int index, boolean isUse, String count, String customerName,String wid) {
            this.name = name;
            this.index = index;
            this.isUse = isUse;
            this.count = count;
            this.customerName = customerName;
            this.wid = wid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isUse() {
            return isUse;
        }

        public void setUse(boolean use) {
            isUse = use;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getWid() {
            return wid;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }
    }
}
