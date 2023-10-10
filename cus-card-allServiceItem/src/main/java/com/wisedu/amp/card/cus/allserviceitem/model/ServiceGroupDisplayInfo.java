package com.wisedu.amp.card.cus.allserviceitem.model;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

/**
 * @ClassName ItemDisplayInfo
 * @Description //TODO
 * @Date 2021/5/24 15:32
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
public class ServiceGroupDisplayInfo {
    String label;
    String value;
    public ServiceGroupDisplayInfo(String value,String label){
        this.label= label;
        this.value = value;
    }
    public static List<ServiceGroupDisplayInfo> initList(){
        List<ServiceGroupDisplayInfo> list = new LinkedList<>();
        list.add(new ServiceGroupDisplayInfo("0","按首字母分组") );
        return  list;
    }
}
