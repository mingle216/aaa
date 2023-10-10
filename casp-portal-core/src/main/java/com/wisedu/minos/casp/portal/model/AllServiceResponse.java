package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AllServiceResponse  extends BaseResponse<List<AllServiceResponse.ServiceModel>>{

    @Data
    public static class ServiceModel {
        String serviceWid;
        String serviceName;
        String iconLink;
        String mobileIconLink;
        boolean favorite;
        boolean permission;
        List<ClassifyInfo> classifyInfos;
        String pcAccessUrl;
        String mobileAccessUrl;
        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        Date useTime;
        String time;
        /**
         * 自定义：排序值（用于本地排序）
         */
        private String order;
        /**
         * 自定义：服务拼音首字母（用于本地排序）
         */
        private String pinyingFirstChar;

        /**
         * 自定义：是否属于Other分组（用于本地）
         */
        private boolean isNotOther;

        private String headChar; //首字母

        private Integer serviceStation;

        @Data
        public static class ClassifyInfo {
            String wid;
            String classifyName;
            Integer sortNum;
        }
    }
}
