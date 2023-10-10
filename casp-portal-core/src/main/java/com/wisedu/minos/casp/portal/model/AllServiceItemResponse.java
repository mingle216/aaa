package com.wisedu.minos.casp.portal.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllServiceItemResponse extends BaseResponse<List<AllServiceItemResponse.ServiceItemModel>> {

    @Data
    public static class ServiceItemModel {
        private String pinyingFirstChar;
        private String itemWid;
        private String iconLink;
        private String itemName;
        private String itemPinYin;
        private int visitCount;
        String itemCategory;
        String itemDept;
        String score;
        private boolean workGuide;//是否展示办事指南
        private boolean favorite;
        private List<String> serviceList;

        private Integer onlineServiceType;

        private String headChar; //首字母
        private String order;//自定义：排序值（用于本地排序）
        private List<String> groupCategoryList;
        private String roleName;

        private Integer isShow;

        private Integer isEnabled;

        private Double calculatedScore;

        private Double serviceRecommendRating;

        //是否开启权限判断
        private Integer isAuthorized;
        //搜索时对象map
        private Object itemDetailSourceAsMap;
    }
}
