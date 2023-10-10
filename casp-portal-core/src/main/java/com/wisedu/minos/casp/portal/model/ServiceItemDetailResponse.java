package com.wisedu.minos.casp.portal.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceItemDetailResponse extends BaseResponse<ServiceItemDetailResponse.ServiceItemModel> {
    @Data
    public static class ServiceItemModel {
        private List<String> linkServices;

        private Integer isShow;

        private String itemName;

        private Integer isEnabled;
    }
}
