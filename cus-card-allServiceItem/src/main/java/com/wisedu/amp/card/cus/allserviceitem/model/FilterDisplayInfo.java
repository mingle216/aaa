package com.wisedu.amp.card.cus.allserviceitem.model;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

/**
 * @ClassName FilterDisplayInfo
 * @Description //TODO
 * @Date 2021/5/24 15:23
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
public class FilterDisplayInfo {
    String label;
    String value;
    public FilterDisplayInfo(String value,String label){
        this.label= label;
        this.value = value;
    }
    public static List<FilterDisplayInfo> initList(){
        List<FilterDisplayInfo> list = new LinkedList<>();
        list.add(new FilterDisplayInfo("-1","可在线办理") );
        list.add(new FilterDisplayInfo( "0","访问量") );
        list.add(new FilterDisplayInfo( "1","服务主题") );
        list.add(new FilterDisplayInfo( "2","责任部门") );
        list.add(new FilterDisplayInfo( "3","服务对象") );
        return  list;
    }
}
