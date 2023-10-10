package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryItemPageListForCaspRes
 * @Author: 01120034
 * @Date: 2021/7/23
 */
@Getter
@Setter
public class QueryItemPageListForCasp {
    /**
     * 事项WID
     */
    private String serviceItemId;
    /**
     * 事项名称
     */
    private String serviceItemName;
    /**
     * 事项责任部门
     */
    private String department;
    /**
     * 事项主题
     */
    private String subject;
    /**
     * 事项是否包含服务
     */
    private boolean isOnline;
    /**
     * 事项包含的服务WID集合
     */
    private List<String> apps;
    /**
     * 事项主题集合
     */
    private List<String> themeNameSet;
    /**
     * 事项是否启用  0:启用  1：禁用
     */
    private Integer enable;
    /**
     * 事项评价分数
     */
    private String serviceMark;
    /**
     * 事项评价总数
     */
    private Integer assessCount;
    /**
     * 事项访问量
     */
    private Integer useCount;

}
