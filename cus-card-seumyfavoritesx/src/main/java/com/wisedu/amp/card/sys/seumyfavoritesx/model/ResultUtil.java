package com.wisedu.amp.card.sys.seumyfavoritesx.model;

public class ResultUtil {
    public static int ZERO=0;
    public static String NINETY_NINE="99+";
    public static String CHECKED="checked";
    public static String EMPTY="";
    public static String INPUTNUM="inputNum";
    public static String HEIGHT_STR="height";
    public static String CARDID="cardId";
    public static String CARDWID="cardWid";
    public static String CONFIG="config";
    public static String PC_APP="PC_APP";
    public static String AMP_URL="amp_url";
    public static int NUM=6;
    public static int HEIGHT=466;
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
}
