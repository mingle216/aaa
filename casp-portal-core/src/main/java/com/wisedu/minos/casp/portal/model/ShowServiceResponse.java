package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/3/3
 */

@Data
public class ShowServiceResponse {
    private List<PersonalDataEntity> personalDataEntityList;

    private List<AppInfo> favoriteServiceList;

    private List<AllServiceResponse.ServiceModel> latestServiceList;

    private List<ServiceBiz> classifyServiceList;
}
