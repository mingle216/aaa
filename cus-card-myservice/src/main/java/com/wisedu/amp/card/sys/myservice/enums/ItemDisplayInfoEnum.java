package com.wisedu.amp.card.sys.myservice.enums;

import lombok.Getter;

/**
 * @ClassName ItemDisplayInfoEnum
 * @Description 服务事项列表展示信息
 * @Date 2021/5/24 13:58
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
public enum ItemDisplayInfoEnum implements BaseEnum {
    /***
     * 图标
     */
    ICON("0","图标"),
    SERVICE_TOPIC("1","服务主题"),
    SERVICE_OBJECT("2","服务对象"),
    RESPONSIBLE_DEPART("3","责任部门"),
    VISIT_COUNT("4","访问量"),
        ;
    /***
     * 标签名字
     */
    private String label;
    /***
     * 标签值
     */
    private String value;

    ItemDisplayInfoEnum( String value,String label) {
        this.label = label;
        this.value = value;
    }

}
