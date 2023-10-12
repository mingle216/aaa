package com.wisedu.amp.card.sys.myservice.model;

import com.wisedu.minos.casp.portal.model.AllServiceItemResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * content页面渲染模型
 */
@Setter
@Getter
public class ContentModel {
    /**
     * 页面位置div的ID
     */
    private String navId;
    /**
     * 导航条包含的所有字母 ,例如：ABCD
     */
    private String navValue;
    /**
     * 显示的名称 ,例如：A-D
     */
    private String navName;

    /**
     * 数据
     */
    private List<AllServiceItemResponse.ServiceItemModel> datas;


}
