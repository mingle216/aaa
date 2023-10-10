package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description:给运营中心应用信息接口
 * @Author: 01120012
 * @Date: 2020/4/29
 */
@Getter
@Setter
public class AppInfoForCaspRes extends ModelApiResponse {
    private List<AppInfoForCasp> data;

}
