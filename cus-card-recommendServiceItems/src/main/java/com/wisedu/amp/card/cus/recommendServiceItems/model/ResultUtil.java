package com.wisedu.amp.card.cus.recommendServiceItems.model;


public class ResultUtil {
    /*
     *任务来源列表是否展示项字典
     * author jdwan
     */
    public static interface RADIO_FLAG{
        String YES="1";
        String NO="0";
    }
    /*
     *服务事项配置项
     * author jdwan
     */
    public static interface CHECKED_FLAG_OLD{
        String FWZT="0";
        String ZRBM="1";
        String FWSXTB="2";
    }

    /*
     *服务事项配置项
     * author jdwan
     */
    public static interface CHECKED_FLAG{
        String ICON="icon";
        String SERVICE="service";
        String DEPT="dept";
    }
    /*
     *任务查询分类
     * author jdwan
     */
    public static interface TASK_TYPE{
        String TODO="1";
        String DONE="2";
        String MYTASK="3";
    }
    /*
     *卡片高度分类
     * author jdwan
     */
    public static interface HEIGHT_TYPE{
        String ZSYWZDXZ="1";
        String ZSYYZDXZ="2";
        String GDGD="3";
    }
}
