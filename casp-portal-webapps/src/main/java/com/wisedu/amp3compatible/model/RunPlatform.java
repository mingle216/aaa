/**
 *
 */
package com.wisedu.amp3compatible.model;

/**
 * 应用运行平台
 * <p>应用管理平台</p>
 * <p>江苏金智教育信息技术有限公司</p>
 * <p>功能说明：</p>
 *
 * @author 丁窍
 * @version 1.0    创建时间：2016年2月25日下午6:31:32	丁窍	发布
 */
public enum RunPlatform {
    PC_APP(1, "PC应用"),
    MOBILE_APP(2, "移动应用"),
    PC_CARD(3, "PC卡片"),
    MOBILE_CARD(4, "移动卡片"),
    SIMPLE_CARD(5, "简版卡片"),
    ALL(0, "全部");

    /**
     * value
     */
    private int value;

    /**
     * name
     */
    private String name;

    /**
     * @param value
     * @param name
     */
    private RunPlatform (int value, String name) {
        this.value = value;
        this.name = name();
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public static RunPlatform getTypeByValue(int value) {
        for (RunPlatform runPlatform : values()) {
            if (runPlatform.getValue() == value) {
                return runPlatform;
            }
        }
        return null;
    }

}
