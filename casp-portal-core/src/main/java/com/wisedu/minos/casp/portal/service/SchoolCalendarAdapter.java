package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.SchoolCalendarModel;

import java.time.LocalDate;

/**
 * @ClassName SchoolCalendarApi
 * @Description
 * @Date 2021/4/7 11:03
 * @Author zkpu
 * @Version 1.0
 **/
public interface SchoolCalendarAdapter {
    /**
     * 东大获取本科生校历信息
     **/
    public SchoolCalendarModel getUnderInfo();

    /**
     * 东大获取研究生校历信息
     **/
    public SchoolCalendarModel getPostInfo();

    /**
     * 产品获取本科生校历信息
     **/
    public SchoolCalendarModel getProductUnderInfo(int type, LocalDate date);

    /**
     * 产品获取研究生校历信息
     **/
    public SchoolCalendarModel getProductPostInfo(int type,LocalDate date);
}
