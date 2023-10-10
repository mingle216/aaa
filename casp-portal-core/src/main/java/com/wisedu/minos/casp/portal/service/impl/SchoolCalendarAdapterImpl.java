package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.dao.entity.ProductSchoolCalendarEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ProductSchoolCalendarMapper;
import com.wisedu.minos.casp.portal.model.SchoolCalendarModel;
import com.wisedu.minos.casp.portal.service.SchoolCalendarAdapter;
import com.wisedu.minos.util.MinosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SchoolCalendarAdapterImpl
 * @Description
 * @Date 2021/4/7 13:44
 * @Author zkpu
 * @Version 1.0
 **/
@Service
@Slf4j
public class SchoolCalendarAdapterImpl implements SchoolCalendarAdapter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductSchoolCalendarMapper productSchoolCalendarMapper;

    @Override
    public SchoolCalendarModel getUnderInfo() {
        SchoolCalendarModel result = null;
        try {
            result = jdbcTemplate.queryForObject(
                    "SELECT A.XN as xn,A.XQKSRQ as begDate,A.ZZC as weekNumber  FROM ( SELECT XN ,XQKSRQ,ZZC  FROM t_casp_calender_under WHERE XQKSRQ < SYSDATE ORDER BY XQKSRQ DESC ) A WHERE ROWNUM = 1",
                    (rs, rowNum) -> {
                        SchoolCalendarModel temp = new SchoolCalendarModel();
                        temp.setXn(rs.getString("xn"));
                        temp.setBegDate(rs.getDate("begDate").toLocalDate());
                        temp.setWeekNumber(rs.getInt("weekNumber"));
                        return temp;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("当前没有本科生校历信息:"+e);
            throw new MinosException("当前没有本科生校历信息");
        }
        return result;
    }

    @Override
    public SchoolCalendarModel getPostInfo() {
        SchoolCalendarModel result = null;
        try {
            result = jdbcTemplate.queryForObject(
                    "SELECT XNDM as xn,QSSJ as begDate,ZS as weekNumber FROM t_casp_calender_post WHERE SFDQXQ='1' AND (SYSDATE BETWEEN QSSJ AND JZSJ)",
                    (rs, rowNum) -> {
                        SchoolCalendarModel temp = new SchoolCalendarModel();
                        temp.setXn(rs.getString("xn"));
                        temp.setBegDate(rs.getDate("begDate").toLocalDate());
                        temp.setWeekNumber(rs.getInt("weekNumber"));
                        return temp;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("当前没有研究生校历信息："+e);
            throw new MinosException("当前没有研究生校历信息");
        }
        return result;
    }

    @Override
    public SchoolCalendarModel getProductUnderInfo(int type, LocalDate date) {
        SchoolCalendarModel result = null;
        QueryWrapper<ProductSchoolCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductSchoolCalendarEntity::getCalendarType, 0)
                .eq(ProductSchoolCalendarEntity::getIsDeleted, 0)
                .le(ProductSchoolCalendarEntity::getStartDate, date)
                .ge(ProductSchoolCalendarEntity::getEndDate, date);
        List<ProductSchoolCalendarEntity> list = productSchoolCalendarMapper
                .selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            ProductSchoolCalendarEntity schoolCalendar = list.get(0);
            result = new SchoolCalendarModel();
            result.setXn(schoolCalendar.getSchoolYear() + "");
            result.setXq(schoolCalendar.getTermCode() + "");
            result.setBegDate(schoolCalendar.getStartDate());
            Date begDate = Date.from(
                    schoolCalendar.getStartDate().atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            Date endDate = Date
                    .from(date.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            int weekNum;
            if(type==0){
                weekNum = calWeek(schoolCalendar.getStartDate(), date);
            }else{
                weekNum = calcWeekOffset(begDate, endDate, type);
            }

            result.setWeekNumber(weekNum);
        }
        return result;
    }

    @Override
    public SchoolCalendarModel getProductPostInfo(int type, LocalDate date) {

        SchoolCalendarModel result = null;
        QueryWrapper<ProductSchoolCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductSchoolCalendarEntity::getCalendarType, 1)
                .eq(ProductSchoolCalendarEntity::getIsDeleted, 0)
                .le(ProductSchoolCalendarEntity::getStartDate, date)
                .ge(ProductSchoolCalendarEntity::getEndDate, date);
        List<ProductSchoolCalendarEntity> list = productSchoolCalendarMapper
                .selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            ProductSchoolCalendarEntity schoolCalendar = list.get(0);
            result = new SchoolCalendarModel();
            result.setXn(schoolCalendar.getSchoolYear() + "");
            result.setXq(schoolCalendar.getTermCode() + "");
            result.setBegDate(schoolCalendar.getStartDate());
            Date begDate = Date.from(
                    schoolCalendar.getStartDate().atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            Date endDate = Date
                    .from(date.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
            int weekNum;
            if (type == 0) {
                weekNum = calWeek(schoolCalendar.getStartDate(), date);
            } else {
                weekNum = calcWeekOffset(begDate, endDate, type);
            }
            result.setWeekNumber(weekNum);
        }
        return result;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年

                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }

    /**
     * date2比date1多的周数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int calcWeekOffset(Date startTime, Date endTime, int type) {
        int dayOfWeek = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .getDayOfWeek().getValue();
        int i = dayOfWeek;
        if (type == 1) {
            i = 7 - dayOfWeek == 0 ? 1 : 7 - dayOfWeek;
        }
        int dayOffset = calcDayOffset(startTime, endTime) + 1;

        int weekOffset = (dayOffset + i) / 7;
        int a;
        if (dayOffset > 0) {
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }
        weekOffset = weekOffset + a;
        return weekOffset;
    }

    public static int calWeek(LocalDate startTime, LocalDate endTime) {
        int value = startTime.getDayOfWeek().getValue();
        //周一直接计算
        if (value == 1) {
            return (int) Math.ceil((double) (startTime.until(endTime, ChronoUnit.DAYS) + 1) / 7);
        }
        //开始时间所在的周末
        LocalDate startEndWeek = startTime.plusDays(7 - value);
        if (startEndWeek.isEqual(endTime)) {
            return 1;
        }
        //表示两个时间在一周
        if (startEndWeek.isAfter(endTime)) {
            return 1;
        }
        long l =startEndWeek.until(endTime, ChronoUnit.DAYS) + 1;
        int floor = (int) Math.ceil((double) l / 7);
        return floor + 1;
    }
}
