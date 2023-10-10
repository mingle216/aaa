package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ServiceRelServiceResponse extends BaseResponse<ServiceRelServiceResponse.ServiceItemAttr>{
    @Data
    public static class ServiceItemAttr {
        /**
         *  无需办理相关指南 （配置完成后，用户在前台可直接跳转至关联服务） 1 不能选服务 or 0 可以选服务
         */
        private Integer isShow;
        /**
         * 事项名称
         */
        private String itemName;
        /**
         * 管理服务列表
         */
        private List<ServiceRelServiceResponse.ServiceModel> serviceList;

        /**
         * 是否启用
         */
        private Integer isEnabled;
    }

    @Data
    public static class ServiceModel {
        private String wid;

        private String appWid;

        private String serviceId;

        private String serviceName;

        private String appName;

        private String serviceNameLangKey;

        private List<MinosMultiLangData> serviceNameLangKeys;

        private String serviceDesc;

        private String serviceDescLangKey;

        private Integer serviceType;

        private Integer serviceStation;

        private int favorite;

        private String pcIconUrl;

        private String pcIconPath;

        private String pcIconName;

        private String mobileIconUrl;

        private String mobileIconPath;

        private String mobileIconName;

        private String pcUrl;

        private String mobileUrl;

        private Integer serviceVisibility;

        private String authenUrl;

        private String pcAccessUrl;

        private String mobileAccessUrl;

        private String serviceAccessWid;

        @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
        private Date useTime;

        private int permission;

        private String mobileIconLink;

        private String iconLink;

    }
}
