package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/3/7
 */

public class CustomGroupResponse {

    @Data
    public static class CustomGroupModel{
//        String wid;
//        String groupName;
        List<MinosSearchCustomGroupParamForPortal> groupParamList;
        List<Object> groupDataList;
        Integer groupDataSize;
//        Integer orderNumber;

        @Data
        public static class MinosSearchCustomGroupParamForPortal {
            String paramwid;
            String paramName;
            String paramAttr;
            Integer hasPermisson;
        }
    }
}
